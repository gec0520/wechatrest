package com.haipai.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haipai.common.service.CoreService;
import com.haipai.common.util.SignUtil;

@RestController
@RequestMapping(value = "/coreController")
public class CoreController {

	@Autowired
	private CoreService coreService;
	/**
	 * 确认请求来自微信服务器
	 */

	private static Logger log = LoggerFactory.getLogger(CoreController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String checkSignature(@RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "nonce", required = false) String nonce,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "echostr", required = false) String echostr) {
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			log.info("接入成功");
			return echostr;
		}
		log.error("接入失败");
		return "";
	}

	/**
	 * 处理微信服务器发来的消息
	 */

	// 调用核心业务类接收消息、处理消息跟推送消息
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String post(HttpServletRequest request) {
		return coreService.processRequest(request);
	}

}
