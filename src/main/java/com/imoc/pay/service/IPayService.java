package com.imoc.pay.service;

import java.math.BigDecimal;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

public interface IPayService {
	
	/**
	 * create payment
	 */
	
	PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

	/**
	 * async notify
	 */
	String asyncNotify(String notifyData);
}
