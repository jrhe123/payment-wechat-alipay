package com.imoc.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.imoc.pay.pojo.PayInfo;
import com.imoc.pay.service.impl.PayService;
import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private WxPayConfig wxPayConfig;
	
	@Autowired
	private AliPayConfig aliPayConfig;
	
	@GetMapping("/create")
	public ModelAndView create(
			@RequestParam("orderId") String orderId,
			@RequestParam("amount") BigDecimal amount,
			@RequestParam("payType") BestPayTypeEnum payType
			) {
		PayResponse response = payService.create(orderId, amount, payType);
		
		Map<String, String> map = new HashMap<>();
		map.put("orderId", orderId);
		// WeChat use codeUrl
		// Alipay use body
		if (payType == BestPayTypeEnum.WXPAY_NATIVE) {
			// map.put("codeUrl", response.getCodeUrl());
			map.put("returnUrl", wxPayConfig.getReturnUrl());
			map.put("codeUrl", "weixin://wxpay/bizpayurl?pr=YDFK0kn");
			return new ModelAndView("createForWechatNative", map);
		} else if (payType == BestPayTypeEnum.ALIPAY_PC) {
			map.put("body", response.getBody());
			map.put("returnUrl", aliPayConfig.getReturnUrl());
			return new ModelAndView("createForAlipayPc", map);
		}
		throw new RuntimeException("Not support payment method");
	}
	
	@PostMapping("/notify")
	@ResponseBody
	public void asyncNotify(@RequestBody String notifyData) {
		log.info("notifyData={}", notifyData);
		payService.asyncNotify(notifyData);
	}
	
	@GetMapping("/queryByOrderId")
	@ResponseBody
	public PayInfo queryByOrderId(@RequestParam String OrderId) {
		return payService.queryByOrderId(OrderId);
	}

}
