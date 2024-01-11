package com.cmhk.ztpayment.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.config.WeChatMerchantProp;
import com.cmhk.ztpayment.config.WeChatMerchantProp.WeChatCertProp;
import com.cmhk.ztpayment.exceptions.UnexpectedWeChatResponseExcpetion;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;
import com.cmhk.ztpayment.requests.wechat.WXAppNativeOrderRequest;
import com.cmhk.ztpayment.services.WeChatV3Service;
import com.cmhk.ztpayment.services.access.WeChatV3Access;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WeChatV3ServiceImpl implements WeChatV3Service{

    private WeChatMerchantProp weChatMerchantProp;
    private WeChatCertProp weChatCertProp;

    @Autowired
    private WeChatV3Access weChatV3Access;

    private JSONObject response;

    private static final String ERROR_WECHAT_SERVER = "Error from wechat server. ";
    
    @Override
    public JSONObject transactionNative(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException{
        //===== Append resource into wechat request body =====
        WXAppNativeOrderRequest wxAppOrderRequest = request.getResources();
        wxAppOrderRequest.setAppid(weChatCertProp.getAppid());
        wxAppOrderRequest.setMchid(weChatCertProp.getMchid());
        wxAppOrderRequest.setNotify_url(weChatCertProp.getNotifyUrl());


        //===== Call wechat server =====
        weChatMerchantProp.useApi("transactionNative");

        JSONObject weChatResponse = weChatV3Access.appOrder(wxAppOrderRequest, weChatMerchantProp, weChatCertProp);

        log.info("native response json = " + weChatResponse);

        //===== Try to get qr code =====
        String qrCodeUrl = weChatResponse.getString("code_url");

        if (qrCodeUrl == null || qrCodeUrl.isEmpty()) {
            String message = weChatResponse.getString("message");
            throw new UnexpectedWeChatResponseExcpetion(
                StringUtils.isNotBlank(message) ? message : ERROR_WECHAT_SERVER
            );
        }

        response = new JSONObject();
        response.put("code_url", qrCodeUrl);


        //===== Return QR code if success =====
        return response;
    }

    @Override
    public JSONObject transactionCheck(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException{
        //===== Append resource into wechat request body =====
        WXAppNativeOrderRequest wxAppOrderRequest = request.getResources();
        wxAppOrderRequest.setAppid(weChatCertProp.getAppid());
        wxAppOrderRequest.setMchid(weChatCertProp.getMchid());

        //===== Call wechat server =====
        weChatMerchantProp.useApi("transactionCheck");

        JSONObject weChatResponse = weChatV3Access.appOrder(wxAppOrderRequest, weChatMerchantProp, weChatCertProp);

        log.info("check response json = {} ", weChatResponse);

        //===== Try to get out trade number =====
        String outTradeNo = weChatResponse.getString("out_trade_no");

        if (outTradeNo == null || outTradeNo.isEmpty()) {
            String message = weChatResponse.getString("message");
            throw new UnexpectedWeChatResponseExcpetion(
                StringUtils.isNotBlank(message) ? message : ERROR_WECHAT_SERVER
            );
        }
        

        // transactionCheck response result should be same as WeChat server response.
        response = weChatResponse;

        return response;
    }
    
    @Override
    public JSONObject transactionClose(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException{
        //===== Append resource into wechat request body =====
        WXAppNativeOrderRequest wxAppOrderRequest = request.getResources();
        //!Important: We won't add appId into WXAppNativeOrderRequest as WeChat not required for it. 
        wxAppOrderRequest.setMchid(weChatCertProp.getMchid());

        //===== Call wechat server =====
        weChatMerchantProp.useApi("transactionClose");

        JSONObject weChatResponse = weChatV3Access.appOrder(wxAppOrderRequest, weChatMerchantProp, weChatCertProp);
        String httpResponseCode = weChatV3Access.getHttpResponseCode();

        //===== Pop exception if retry 3 times failure, else return httpStatus as json =====
        if( !httpResponseCode.equals("204") ) {  //Set as 205 to Force to fail
            throw new UnexpectedWeChatResponseExcpetion("WeChat return response code ->"+httpResponseCode);
        }

        log.info("close response json = {} , http response code = {} ", weChatResponse, httpResponseCode);

        //===== Only return http status =====
        response = new JSONObject();
        response.put("httpStatus", httpResponseCode);


        return response;
    }

    @Override
    public void setProperties(WeChatMerchantProp weChatMerchantProp, String appId, String mchId){
        setProperties(weChatMerchantProp);
        setMerchant(weChatMerchantProp.findWeChatCertByName(appId, mchId));
    }

    @Override
    public void setProperties(WeChatMerchantProp weChatMerchantProp){
        this.weChatMerchantProp = weChatMerchantProp;
    }

    @Override
    public void setMerchant(WeChatCertProp weChatCertProp){
        this.weChatCertProp = weChatCertProp;
    }
}
