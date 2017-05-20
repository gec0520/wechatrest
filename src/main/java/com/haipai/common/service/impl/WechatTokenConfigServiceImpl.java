package com.haipai.common.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.repository.WechatTokenConfigRepository;
import com.haipai.common.service.WechatTokenConfigService;

@Service
@Transactional
public class WechatTokenConfigServiceImpl implements WechatTokenConfigService {

	@Autowired
	private WechatTokenConfigRepository wechatTokenConfigRepository;

	@Override
	public WechatTokenConfig findByTokenTypeAndAppId(String tokenType, String appId) {
		return wechatTokenConfigRepository.findByTokenTypeAndAppId(tokenType, appId);
	}

	@Override
	public void save(WechatTokenConfig wechatTokenConfig) {
		wechatTokenConfigRepository.save(wechatTokenConfig);
	}

	@Override
	public List<WechatTokenConfig> findAll() {
		return wechatTokenConfigRepository.findAll();
	}

	@Override
	public void saveAll(List<WechatTokenConfig> wechatTokenConfigList) {
		wechatTokenConfigRepository.save(wechatTokenConfigList);
	}
}
