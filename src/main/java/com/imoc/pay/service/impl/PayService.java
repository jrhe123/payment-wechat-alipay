package com.imoc.pay.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imoc.pay.dao.PayInfoMapper;
import com.imoc.pay.enums.PayPlatformEnum;
import com.imoc.pay.pojo.PayInfo;
import com.imoc.pay.service.IPayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayService implements IPayService {
	
	@Autowired
	private BestPayService bestPayService;
	
	@Autowired
	private PayInfoMapper payInfoMapper;

	@Override
	public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
		PayInfo payInfo = new PayInfo(
				Long.parseLong(orderId),
				PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
				OrderStatusEnum.NOTPAY.name(),
				amount
				);
		payInfoMapper.insertSelective(payInfo);
		
		// 1. build request (create QR code)
		PayRequest request = new PayRequest();
		request.setOrderName("4559066-bestpay-sdk");
		request.setOrderId(orderId);
		request.setOrderAmount(amount.doubleValue());
		request.setPayTypeEnum(bestPayTypeEnum);
		// 2. receive qr response
		PayResponse response = bestPayService.pay(request);
		log.info("response: ", response);
		return response;
	}

	@Override
	@ResponseBody
	public String asyncNotify(String notifyData) {
		// 1. verify signature
		PayResponse response = bestPayService.asyncNotify(notifyData);
		log.info("response: ", response);
		
		// 2. check amount from DB
		PayInfo payInfo = payInfoMapper.selectByOrderNo(
				Long.parseLong(response.getOrderId())
				);
		if (payInfo == null) {
			throw new RuntimeException("Order no not found");
		}
		if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
			if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(response.getOrderAmount())) != 0) {
				throw new RuntimeException("Order amount not match, orderNo: " + response.getOrderId());
			}
			// 3. update payment status
			payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
			payInfo.setPlatformNumber(response.getOutTradeNo());
			payInfo.setUpdateTime(null);
			payInfoMapper.updateByPrimaryKeySelective(payInfo);
		}
		
		// TODO: MQ here!!!
		
		// 4. return success & stop notify
		if (response.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
			// wechat
			return "<xml>\n" + 
					"<return_code><![CDATA[SUCCESS]]></return_code>\n" + 
					"<return_msg><![CDATA[OK]]></return_msg>\n" + 
				   "</xml>";
		} else {
			// alipay
			return "success";
		}		
	}

	public PayInfo queryByOrderId(String orderId) {
		PayInfo payInfo = payInfoMapper.selectByOrderNo(
				Long.parseLong(orderId)
				);
		return payInfo;
	}

}
