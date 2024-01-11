package com.cmhk.ztpayment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI myOpenAPI(){
        return new OpenAPI()
            .components(new Components())
            .info(applicationInfo())
            .externalDocs(externalDocs())
            ;
    }

    private Info applicationInfo(){
        return new Info()
                .title("WeChat V3 Pay API")
                .description("支付中台微服務: 微信支付 「API」")
                .version("1.0")
                .contact(new Contact()
                    .email("itgeneralservice@hk.chinamobile.com")
                    .name("ZT IT General Service")
                    .url("http://10.0.27.225/payments/zt-payment/ms-wechatv3")
                )
            ;
    }

    private ExternalDocumentation externalDocs(){
        return new ExternalDocumentation()
                .description("微信支付 V3 官方文檔")
                .url("https://pay.weixin.qq.com/wiki/doc/apiv3/index.shtml")
                ;
    }
}
