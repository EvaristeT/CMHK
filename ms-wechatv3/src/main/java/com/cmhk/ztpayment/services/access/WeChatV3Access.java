package com.cmhk.ztpayment.services.access;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.config.WeChatMerchantProp;
import com.cmhk.ztpayment.config.WeChatMerchantProp.WeChatApiEnum;
import com.cmhk.ztpayment.config.WeChatMerchantProp.WeChatCertProp;
import com.cmhk.ztpayment.exceptions.WeChatCertificateException;
import com.cmhk.ztpayment.requests.wechat.WXAppNativeOrderRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;


/**
 * @author flanderldk
 * @date 2020-08-05
 * @desc 微信访问access层
 * 
 * 
 * @author keithlo
 * @date 2021-06-23
 * @desc 為升級 Java11 ＋ 拆分出來成為微信微服務 ＋ Nacos 配置化
 *  - 刪除儲存數據庫 `PaymentParamDao`
 *  - 刪除功能 ｀refreshCertificate｀
 * 
 */
@Component
@Slf4j
public class WeChatV3Access {

    /** 认证头部格式字符串 */
    private static final String AUTHORIZATION_FORMAT = "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",nonce_str=\"%s\",signature=\"%s\",timestamp=\"%s\",serial_no=\"%s\"";
    private static final int HTTP_TIMEOUT = 7;  //7 seconds

    /** HTTP Response Code */
    private String httpResponseCode;

    //===== Get application properties value =====
    int retryNumber = 1;
    int retryInterval = 0;

    /**
     * 微信app下单
     * @param wxUnifiedOrderRequest {@link com.cmhk.ztpayment.requests.wechat.WXAppOrderRequest}
     * @param weChatMerchantProp {@link com.cmhk.ztpayment.config.WeChatMerchantProp}
     * @param weChatCertProp {@link com.cmhk.ztpayment.pojo.config.WeChatCertProp}
     * @return
     * @throws Exception
     */
    public JSONObject appOrder(WXAppNativeOrderRequest wxUnifiedOrderRequest, WeChatMerchantProp weChatMerchantProp, WeChatCertProp weChatCertProp) {
       
        String httpMethod = weChatMerchantProp.getMethod().toString();
        String url = getFullUrl(wxUnifiedOrderRequest, weChatMerchantProp.getHost(), weChatMerchantProp.getApi());

        retryNumber = weChatMerchantProp.getRetry();
        retryInterval = weChatMerchantProp.getRetryInterval();
        
        log.info("WeChatV3->url {} {}", httpMethod, url);
        
        String orderUrl = parseUrl(weChatMerchantProp.getApi(), wxUnifiedOrderRequest);

        String body = JSON.toJSONString(wxUnifiedOrderRequest);        
        // Because WeChat do not required the fields that already mreged on URL
        // Remove those field values from the body
        body = removeBodyParams(body, weChatMerchantProp.getApi());

        String mchid = weChatCertProp.getMchid();
        String serialNo = weChatCertProp.getSerialNo();
        PrivateKey privateKey = readPrivateKey(weChatCertProp.getPrivateKey());

        String authorization = getAuthorization(orderUrl, body, httpMethod, mchid, serialNo, privateKey);

        if( !httpMethod.equalsIgnoreCase(WeChatApiEnum.GET.toString()) ){
            return callPost(url, body, authorization); 
        }else{
            return callGet(url, authorization);
        }
    }

    public String getHttpResponseCode() {
        return httpResponseCode;
    }

    private String removeBodyParams(String body, String url){
        Map<String, String> hashMap = getUrlParams(url);
        JSONObject jsonObject = JSON.parseObject(body);
        for( String key : hashMap.values() ){
            jsonObject.remove(key);

            log.info("Remove body params {}", key);
        }

        body = jsonObject.toString();

        return body;
    }

    private Map<String, String> getUrlParams(String url){
        final String pattern = "\\{(\\w*)\\}";
        final Matcher m = Pattern.compile(pattern).matcher(url);

        Map<String, String> hashMap = new HashMap<>();        
        while( m.find() ){
            String fieldName = m.group(0).replace("{", "").replace("}", "");
            
            hashMap.put(m.group(0), fieldName);
        }

        return hashMap;
    }

    private String parseUrl(String url, WXAppNativeOrderRequest wxUnifiedOrderRequest){
        final JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(wxUnifiedOrderRequest));

        return parseUrl(url, jsonObject);
    }

    private String parseUrl(String url, JSONObject jsonObject){

        final Map<String, String> hashMap = getUrlParams(url);

        for( Map.Entry<String, String> entry : hashMap.entrySet() ){

            String fullName = entry.getKey();
            String fieldName = entry.getValue();
            String value = StringUtils.isNotBlank(jsonObject.getString(fieldName)) ? jsonObject.getString(fieldName) : "";

            url = url.replace(fullName, value);
        }

        return url;
    }

    /**
     * 获取微信下单完整路径
     * @param wxUnifiedOrderRequest
     * @param paramMap
     * @param code
     * @return
     */
    private String getFullUrl(WXAppNativeOrderRequest wxUnifiedOrderRequest, String hostUrl, String orderUrl){
        orderUrl = parseUrl(orderUrl, wxUnifiedOrderRequest);

        return  hostUrl + orderUrl;
    }

    private PrivateKey readPrivateKey(String content){
        try{
            String privKeyPEM = content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "").trim();

            byte[] decoded = Base64.decodeBase64(privKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            
            return kf.generatePrivate(keySpec);
        }catch(Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 获取认证头部
     * @param url
     * @param body
     * @param method
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public String getAuthorization(String url, String body, String method, String mchid, String serialNo, PrivateKey privateKey) {
        String nonce = RandomStringUtils.randomAlphanumeric(32);
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

        String signatureStr = buildMessage(method, url, body, timestamp, nonce);

        String signature = null;
        try{
            signature = sign(signatureStr.getBytes(StandardCharsets.UTF_8), privateKey);
            signature = signature.replaceAll(System.getProperty("line.separator"), "");
        }catch(Exception e){
            throw new WeChatCertificateException(e.getMessage());
        }

        return String.format(AUTHORIZATION_FORMAT, mchid, nonce, signature, timestamp, serialNo);
    }

    /**
     * 对数据进行签名
     * @param message    签名信息
     * @param privateKey 私钥
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    String sign(byte[] message, PrivateKey privateKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {

        Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(message);

		byte[] result = signature.sign();

        return Base64.encodeBase64String(result);
    }

    /**
     * 微信签名字符串生成
     * @param wechatpayTimestamp    微信返回的时间戳
     * @param wechatpayNonce        微信返回的随机值
     * @param response              微信返回的数据报文
     * @return
     */
    public static String signatureStr(String wechatpayTimestamp, String wechatpayNonce, String response){
        return  wechatpayTimestamp + "\n"
                + wechatpayNonce + "\n"
                + response + "\n";
    }


    /**
     * 生成签名串
     * @param method  调用方法
     * @param url     调用url
     * @param body     消息体
     * @return
     */
    private String buildMessage(String method, String url, String body, long currentTime, String nonce) {
        StringBuilder sb = new StringBuilder();

        sb.append(method).append("\n");
        sb.append(url).append("\n");
        sb.append(currentTime).append("\n");
        sb.append(nonce).append("\n");

        if( !method.equalsIgnoreCase(HttpMethod.GET.name()) && !body.isEmpty() ){
            sb.append(body);
        }
        sb.append("\n");
        
        return sb.toString();
    }

    // This method returns filter function which will log request data
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if( log.isDebugEnabled() ){
                log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse(){
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if( log.isDebugEnabled() ){
                log.info("Response: {}", clientResponse.statusCode().value());
            }

            httpResponseCode = String.valueOf(clientResponse.statusCode().value());

            return Mono.just(clientResponse);
        });
    }

    private WebClient getWebClient(){
        return WebClient.builder()
        .filters(exchangeFilterFunctions -> {
            exchangeFilterFunctions.add(logRequest());
            exchangeFilterFunctions.add(logResponse());
        })
        .build();
    }

    /**
     * Get response body from WebClient retrieve()
     * 
     * @param responseEntity
     * @return String
     */
    private String getResponseBody(ResponseEntity<String> responseEntity){
        String responseBody = null;
        if( responseEntity != null ){
            responseBody = responseEntity.getBody();
        }else{
            log.error("Cannot retrieve WeChat API response. Please check the connection.");
        }

        log.info("response body: {}", responseBody);

        return responseBody;
    }

    /**
     * 访问微信方法,post方法
     * @param uri 访问微信url
     * @param message   访问微信消息
     * @return
     */
    private JSONObject callPost(String uri, String requestBody, String authorization){

        log.info(">>> {} - WebClient : POST >>>", this.getClass().getSimpleName());        

        ResponseEntity<String> responseEntity = getWebClient().post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(  //Don't treat 400 - 499 response as error
                status -> status.value() >= 400 && status.value() <= 499,
                clientResponse -> Mono.empty()
            )
            .toEntity(String.class)
            .timeout(Duration.ofSeconds(HTTP_TIMEOUT))
            .retryWhen(Retry.fixedDelay(retryNumber, Duration.ofSeconds(retryInterval)))
            .block();       

        log.info("Request body: {}", requestBody);
        
        String responseBody = getResponseBody(responseEntity);

        log.info("<<< {} - WebClient : POST <<<", this.getClass().getSimpleName());
        
        return JSON.parseObject(responseBody);
    }


    /**
     * 访问微信方法,get方法
     * @param uri 访问微信url
     * @return
     */
    private JSONObject callGet(String uri, String authorization){

        log.info(">>> {} - WebClient : GET >>>", this.getClass().getSimpleName()); 

        ResponseEntity<String> responseEntity = getWebClient().get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(  //Don't treat 400 - 499 response as error
                status -> status.value() >= 400 && status.value() <= 499,
                clientResponse -> Mono.empty()
            )
            .toEntity(String.class)
            .timeout(Duration.ofSeconds(HTTP_TIMEOUT))
            .retryWhen(Retry.fixedDelay(retryNumber, Duration.ofSeconds(retryInterval)))
            .block();

        String responseBody = getResponseBody(responseEntity);

        log.info("<<< {} - WebClient : GET <<<", this.getClass().getSimpleName());

        return JSON.parseObject(responseBody);
    }


}
