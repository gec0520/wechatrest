package com.haipai.common.service;

import java.util.List;

import com.haipai.common.entity.WechatTokenConfig;

public interface WechatTokenConfigService {

	public WechatTokenConfig findByTokenTypeAndAppId(String tokenType,String appId);
	public void save(WechatTokenConfig wechatTokenConfig);
	public List<WechatTokenConfig> findAll();
	public void saveAll(List<WechatTokenConfig> wechatTokenConfigList);
}
