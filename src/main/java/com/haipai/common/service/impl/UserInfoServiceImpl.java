package com.haipai.common.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.service.UserInfoService;
import com.haipai.common.service.WechatTokenConfigService;
import com.haipai.common.usergroup.UserGroupInfo;
import com.haipai.common.util.HttpRequestUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	public static final String user_group_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	@Autowired
	private WechatTokenConfigService wechatTokenConfigService;

	@Override
	public UserGroupInfo getUserGroupInfo(String appId, String openid) {
		UserGroupInfo userGroupInfo = null;
		WechatTokenConfig wechatTokenConfig = wechatTokenConfigService.findByTokenTypeAndAppId("access_token", appId);
		String requestUrl = user_group_url.replace("ACCESS_TOKEN", wechatTokenConfig.getTokenValue()).replace("OPENID",
				openid);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.GET_METHOD, null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				userGroupInfo = new UserGroupInfo();
				userGroupInfo.setSubscribe(jsonObject.getString("subscribe"));
				userGroupInfo.setOpenid(jsonObject.getString("openid"));
				userGroupInfo.setSex(jsonObject.getString("sex"));
				userGroupInfo.setLanguage(jsonObject.getString("language"));
				userGroupInfo.setCity(jsonObject.getString("city"));
				userGroupInfo.setProvince(jsonObject.getString("province"));
				userGroupInfo.setCountry(jsonObject.getString("country"));
				userGroupInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
				userGroupInfo.setSubscribe_time(jsonObject.getString("subscribe_time"));
				userGroupInfo.setRemark(jsonObject.getString("remark"));
				userGroupInfo.setGroupid(jsonObject.getString("groupid"));
			} catch (JSONException e) {
				logger.error("getUserGroupInfo error", e);
			}
		}
		return userGroupInfo;
	}
	
	public static void main(String[] args){
		UserGroupInfo userGroupInfo = null;
		String requestUrl = user_group_url.replace("ACCESS_TOKEN", "btQWtffVicW-pAx0ZbljNY9ChdLtkPLKmfsFIp4T58KfCmso1URgWij-QFx1OIP4L3AHtCppO3oIHI_yv4fBd3VUNAFuIhbRd9zSAhufkHaSMlTk61HXo3JQoxUCnIF5CQYhACAIWE").replace("OPENID","oi88H1Y8n5sj5b3yrJeVtVBAB6uQ");
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.GET_METHOD, null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				userGroupInfo = new UserGroupInfo();
				userGroupInfo.setSubscribe(jsonObject.getString("subscribe"));
				userGroupInfo.setOpenid(jsonObject.getString("openid"));
				userGroupInfo.setSex(jsonObject.getString("sex"));
				userGroupInfo.setLanguage(jsonObject.getString("language"));
				userGroupInfo.setCity(jsonObject.getString("city"));
				userGroupInfo.setProvince(jsonObject.getString("province"));
				userGroupInfo.setCountry(jsonObject.getString("country"));
				userGroupInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
				userGroupInfo.setSubscribe_time(jsonObject.getString("subscribe_time"));
				userGroupInfo.setRemark(jsonObject.getString("remark"));
				userGroupInfo.setGroupid(jsonObject.getString("groupid"));
			} catch (JSONException e) {
				logger.error("getUserGroupInfo error", e);
			}
		}
	}

}
