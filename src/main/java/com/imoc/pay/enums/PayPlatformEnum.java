package com.imoc.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;

import lombok.Getter;

@Getter
public enum PayPlatformEnum {

	// 1. alipay
	// 2. wechat
	
	ALIPAY(1),
	WX(2);
	
	Integer code;
	
	PayPlatformEnum(Integer code) {
		this.code = code;
	}
	
	public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
//		if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name())) {
//			return PayPlatformEnum.ALIPAY;
//		} else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX.name())) {
//			return PayPlatformEnum.WX;
//		}
//		return null;
		for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
			if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
				return payPlatformEnum;
			}
		}
		throw new RuntimeException("Invalid payment platform" + bestPayTypeEnum.name());
	}
}
