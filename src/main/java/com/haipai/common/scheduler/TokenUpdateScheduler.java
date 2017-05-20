package com.haipai.common.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.WechatTokenConfigService;
import com.haipai.common.util.AccessToken;
import com.haipai.common.util.WechatUtil;
@Component
public class TokenUpdateScheduler {

	@Autowired
	private WechatTokenConfigService wechatTokenConfigService;
	
	@Autowired
	private AppIdSecretConfigService appIdSecretConfigService;
	
	@Scheduled(fixedDelay = 6000000)
	public void updateTocken(){

		List<AppIdSecretConfig> appIdSecretConfigList = appIdSecretConfigService.findAll();
		List<WechatTokenConfig> wechatTokenConfigList = new ArrayList<>();
		if(appIdSecretConfigList!=null && !appIdSecretConfigList.isEmpty()){
			for(AppIdSecretConfig appIdSecretConfig : appIdSecretConfigList){
				String appId = appIdSecretConfig.getAppId();
				String appSecret = appIdSecretConfig.getAppSecret();
				WechatTokenConfig wechatTokenConfigAccessToken = wechatTokenConfigService.findByTokenTypeAndAppId("access_token", appId);
				if(appId==null || appSecret==null){
					continue;
				}
				AccessToken accessToken = WechatUtil.getAccessToken(appId, appSecret);
				if(wechatTokenConfigAccessToken==null ){
					wechatTokenConfigAccessToken = new WechatTokenConfig();
					wechatTokenConfigAccessToken.setAppId(appId);
					wechatTokenConfigAccessToken.setAppSecret(appSecret);
					wechatTokenConfigAccessToken.setTokenValue(accessToken.getToken());
					wechatTokenConfigAccessToken.setCreateDate(new Date());
					wechatTokenConfigAccessToken.setExpireDate(WechatUtil.plusMinutes(100, new Date()));
				}else{
					wechatTokenConfigAccessToken.setTokenValue(accessToken.getToken());
					wechatTokenConfigAccessToken.setCreateDate(new Date());
					wechatTokenConfigAccessToken.setExpireDate(WechatUtil.plusMinutes(100, new Date()));
				}
				wechatTokenConfigAccessToken.setTokenType("access_token");
				wechatTokenConfigList.add(wechatTokenConfigAccessToken);
				
				
				WechatTokenConfig wechatTokenConfigJsApiTicket = wechatTokenConfigService.findByTokenTypeAndAppId("jsapi_ticket", appId);
				String jsApiTicket = WechatUtil.getJSApiTicket(accessToken.getToken());
				if(wechatTokenConfigJsApiTicket==null ){
					wechatTokenConfigJsApiTicket = new WechatTokenConfig();
					wechatTokenConfigJsApiTicket.setAppId(appId);
					wechatTokenConfigJsApiTicket.setAppSecret(appSecret);
					wechatTokenConfigJsApiTicket.setTokenValue(jsApiTicket);
					wechatTokenConfigJsApiTicket.setCreateDate(new Date());
					wechatTokenConfigJsApiTicket.setExpireDate(WechatUtil.plusMinutes(100, new Date()));
				}else{
					wechatTokenConfigJsApiTicket.setTokenValue(jsApiTicket);
					wechatTokenConfigJsApiTicket.setCreateDate(new Date());
					wechatTokenConfigJsApiTicket.setExpireDate(WechatUtil.plusMinutes(100, new Date()));
				}
				wechatTokenConfigJsApiTicket.setTokenType("jsapi_ticket");
				wechatTokenConfigList.add(wechatTokenConfigJsApiTicket);
			}
		}
		wechatTokenConfigService.saveAll(wechatTokenConfigList);
	
	}
}
