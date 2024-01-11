package com.cmhk.ztpayment;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "wechat-accounts.yaml", groupId = "ms-wechatv3", type = ConfigType.YAML, autoRefreshed = true)
public class WechatApiApplication {

	public static void main(String[] args) {
		System.setProperty("nacos.logging.default.config.enabled", "false");
		
		SpringApplication.run(WechatApiApplication.class, args);
	}


}
