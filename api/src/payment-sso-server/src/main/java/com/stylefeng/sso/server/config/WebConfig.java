package com.stylefeng.sso.server.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.stylefeng.sso.plugin.interceptor.SsoServerInterceptor;
import com.stylefeng.sso.plugin.properties.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author fengshuonan
 * @Date 2018/8/29 下午3:32
 */
@ControllerAdvice
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @ConfigurationProperties(prefix = SsoProperties.BEETLCONF_PREFIX)
    public SsoProperties ssoProperties() {
        return new SsoProperties();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SsoServerInterceptor(ssoProperties())).addPathPatterns("/**").excludePathPatterns("/login", "/ssoApi/authToken", "/logout", "/static/**"
                ,"/frontendLogin");
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder().build();
    }
}
