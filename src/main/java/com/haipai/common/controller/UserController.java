package com.haipai.common.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.JsApiService;
import com.haipai.common.service.UserCouponService;
import com.haipai.common.util.HttpRequestUtil;

import net.sf.json.JSONObject;

@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private JsApiService jsApiService;
	
	@Autowired
	private AppIdSecretConfigService appIdSecretConfigService;
	
	@RequestMapping(value = "/getSignature", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> getSignature(HttpServletRequest request, HttpServletResponse response)  {
		String appId = (String) request.getSession().getAttribute("appId");
		String url = request.getParameter("url");
		String jsapi_ticket = jsApiService.getJSApiTicket(appId);
		Map<String, String> resp = jsApiService.getSignature(jsapi_ticket, url);
		resp.put("appId", appId);
		return resp;
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String weixinDirect(HttpServletRequest request, HttpServletResponse response) {
		String wechatPublicId = request.getParameter("wechatpublicid");
		AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(wechatPublicId);
		request.getSession().setAttribute("appId", appIdSecretConfig.getAppId());
		request.getSession().setAttribute("appSecret", appIdSecretConfig.getAppSecret());
		request.getSession().setAttribute("wechatPublicId", wechatPublicId);
		StringBuilder redirectUrl = new StringBuilder(
				"redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=");
		redirectUrl.append(appIdSecretConfig.getAppId()).append("&redirect_uri=")
				.append(URLEncoder.encode("http://haipai.duapp.com/oauth?appId=" + appIdSecretConfig.getAppId() + "&appSecret=" + appIdSecretConfig.getAppSecret()))
				.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		return redirectUrl.toString();
	}

	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public String weixinOAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 得到code
		String appId = request.getParameter("appId");
		String appSecret = request.getParameter("appSecret");
		String code = request.getParameter("code");
		logger.info("================= weixinOAuth appId:{},appSecret:{},code:{}", appId, appSecret, code);
		// 换取access_token 其中包含了openid
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
				.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(URL, HttpRequestUtil.GET_METHOD, null);
		logger.info("================= weixinOAuth jsonObject:{}", jsonObject);
		String openid = jsonObject.get("openid").toString();
		request.getSession().setAttribute("wechatUser", openid);
		return "redirect:wechatrest/userCoupon.html";
	}
}
