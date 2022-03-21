package com.imoc.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccountConfig {

	private String appId;
	
	private String mchId;
	
	private String mchKey;
	
	private String notifyUrl;
	
	private String returnUrl;
}
