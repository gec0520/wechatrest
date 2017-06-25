package com.haipai.common.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.entity.UserGroupInfo;
import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.repository.UserGroupInfoRepository;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.UserInfoService;
import com.haipai.common.service.WechatTokenConfigService;
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
	
	@Autowired
	private UserGroupInfoRepository userGroupInfoRepository;

	@Autowired
	private AppIdSecretConfigService appIdSecretConfigService;
	
	@Override
	public UserGroupInfo generateUserGroupInfo(String accessToken, String openId,String wechatPublicId) {
		UserGroupInfo userGroupInfo = null;
		String requestUrl = user_group_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID",
				openId);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.GET_METHOD, null);
		logger.info("generateUserGroupInfo jsonObject {}",jsonObject);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				userGroupInfo = new UserGroupInfo();
				userGroupInfo.setSubscribe(jsonObject.getString("subscribe"));
				userGroupInfo.setOpenId(jsonObject.getString("openid"));
				userGroupInfo.setSex(jsonObject.getString("sex"));
				userGroupInfo.setLanguage(jsonObject.getString("language"));
				userGroupInfo.setCity(jsonObject.getString("city"));
				userGroupInfo.setProvince(jsonObject.getString("province"));
				userGroupInfo.setCountry(jsonObject.getString("country"));
				userGroupInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
				userGroupInfo.setSubscribe_time(jsonObject.getString("subscribe_time"));
				userGroupInfo.setRemark(jsonObject.getString("remark"));
				userGroupInfo.setGroupid(jsonObject.getString("groupid"));
				userGroupInfo.setWechatPublicId(wechatPublicId);
			} catch (JSONException e) {
				logger.error("getUserGroupInfo error", e);
				return null;
			}
		}
		return userGroupInfo;
	}

	@Override
	public UserGroupInfo findBywechatPublicIdAndOpenId(String wechatPublicId, String openId) {
		return userGroupInfoRepository.findByWechatPublicIdAndOpenId(wechatPublicId, openId);
	}
	
	@Override
	public void addNewUserGroupInfo(UserGroupInfo userGroupInfo) {
		userGroupInfoRepository.saveAndFlush(userGroupInfo);
		userGroupInfoRepository.flush();
	}
	
	@Override
	public void deleteUserGroupInfo(UserGroupInfo userGroupInfo) {
		userGroupInfoRepository.delete(userGroupInfo);
		userGroupInfoRepository.flush();
	}
	
	public static void main(String[] args){
		UserGroupInfo userGroupInfo = null;
		String requestUrl = user_group_url.replace("ACCESS_TOKEN", "3DVX5CwByn6qxFnRJqHv0Ca9kB3z7qd8T6aPMKov8qG4ctOcsv95juBBPjLDwk3pKnbbJH-emBYruYSjuOlPKejZvWFWgZ93ZauByTmeM-38hMKhOqCCBIG2lX28HtbYDIHgAGASXW").replace("OPENID","oi88H1Y8n5sj5b3yrJeVtVBAB6uQ");
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.GET_METHOD, null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				userGroupInfo = new UserGroupInfo();
				userGroupInfo.setSubscribe(jsonObject.getString("subscribe"));
				userGroupInfo.setOpenId(jsonObject.getString("openid"));
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
	@Override
	public void deleteSubscribeGroupUser(String fromUserName, String toUserName) {
		UserGroupInfo userGroupInfo = findBywechatPublicIdAndOpenId(toUserName, fromUserName);
		if (userGroupInfo != null) {
			deleteUserGroupInfo(userGroupInfo);
		}
	}
	
	@Override
	public void addSubscribeGroupUser(String fromUserName, String toUserName) {
		logger.info("addSubscribeGroupUser toUserName {} fromUserName {}",toUserName,fromUserName);
		UserGroupInfo userGroupInfo = findBywechatPublicIdAndOpenId(toUserName, fromUserName);
		logger.info("findBywechatPublicIdAndOpenId userGroupInfo  {} ",userGroupInfo);
		if (userGroupInfo == null) {
			AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
			WechatTokenConfig wechatTokenConfig = wechatTokenConfigService.findByTokenTypeAndAppId("access_token",
					appIdSecretConfig.getAppId());
			userGroupInfo = generateUserGroupInfo(wechatTokenConfig.getTokenValue(), fromUserName,
					toUserName);
			if(userGroupInfo!=null){
				addNewUserGroupInfo(userGroupInfo);
			}
		}
	}
}
