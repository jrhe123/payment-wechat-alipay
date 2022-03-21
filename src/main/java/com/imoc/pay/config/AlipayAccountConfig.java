package com.imoc.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayAccountConfig {

	private String appId;
	
	private String privateKey;
	
	private String publicKey;
	
	private String notifyUrl;
	
	private String returnUrl;
}
