package com.cmhk.ztpayment.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
// @ConfigurationProperties(prefix = "wechat")
@Data
@Slf4j
@NacosConfigurationProperties(prefix = "wechat", dataId = "wechat-accounts.yaml", groupId = "ms-wechatv3", type = ConfigType.YAML, autoRefreshed = true)
public class WeChatMerchantProp {

    // 微信服務器路徑
    private String host;
    
    // 對應連接微信服務器, 超時重試次數
    private int retry = 2;
    // 對應連接微信服務器, 超時間隔秒
    private int retryInterval = 1;

    // 目標 API HTTP 請求 （使用 ｀useApi｀ 設定）
    private WeChatApiEnum method;
    // 目標 API Restful 路徑 （使用 ｀useApi｀ 設定）
    private String api;
    // API Restful 路徑配置表
    private Map<String, WeChatApiProp> apis;
    // 微信賬戶證書配置表
    private List<WeChatCertProp> certs;

    public WeChatCertProp findWeChatCertByName(String appId, String mchId){
        
        for( WeChatCertProp cert : certs ){
            if( appId.equals(cert.getAppid()) && mchId.equals(cert.getMchid()) ){
                return cert;
            }
        }

        log.error("Cannot found Cert:: appId->{}, mchId->{}", appId, mchId);

        return new WeChatCertProp();
    }

    public void useApi(String name){
        if( !apis.isEmpty() ){
            for( Entry<String, WeChatApiProp> e : apis.entrySet() ){
                if( e.getKey().equalsIgnoreCase(name) ){
                    // Set API
                    api = e.getValue().getApi();
                    method = e.getValue().getMethod();
                }
            }
        }
    }

    public enum WeChatApiEnum{
        GET, POST
    }

    @Data
    public static class WeChatApiProp {
        private WeChatApiEnum method;
        private String api;
    }

    @Data
    public static class WeChatCertProp {

        private String name;
        private String mchid;
        private String appid;
        private String notifyUrl;
        private String serialNo;
        private String clientCert;
        private String privateKey;

    }

}
