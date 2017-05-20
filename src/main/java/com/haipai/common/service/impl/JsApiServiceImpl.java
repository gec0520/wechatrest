package com.haipai.common.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.service.JsApiService;
import com.haipai.common.service.WechatTokenConfigService;

@Service
@Transactional
public class JsApiServiceImpl implements JsApiService {
	private static final Logger logger = LoggerFactory.getLogger(JsApiServiceImpl.class);
	@Autowired
	private WechatTokenConfigService wechatTokenConfigService;

	@Override
	public String getJSApiTicket(String appId) {
		WechatTokenConfig wechatTokenConfig = wechatTokenConfigService.findByTokenTypeAndAppId("jsapi_ticket", appId);
		return wechatTokenConfig.getTokenValue();
	}

	@Override
	public Map<String, String> getSignature(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		logger.info(string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error("getSignature NoSuchAlgorithmException error:",e);
		} catch (UnsupportedEncodingException e) {
			logger.error("getSignature UnsupportedEncodingException error:",e);
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}
	
	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
