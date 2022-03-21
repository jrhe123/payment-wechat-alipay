package com.imoc.pay.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imoc.pay.PayApplicationTests;
import com.imoc.pay.service.impl.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;

public class PayServiceTest extends PayApplicationTests {

	@Autowired
	private PayService payService;
	
	@Test
	public void create() {
		payService.create("123123123", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
	}
}
