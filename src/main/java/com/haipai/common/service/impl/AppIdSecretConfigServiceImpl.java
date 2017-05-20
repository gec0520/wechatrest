package com.haipai.common.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.repository.AppIdSecretConfigRepository;
import com.haipai.common.service.AppIdSecretConfigService;

@Service
@Transactional
public class AppIdSecretConfigServiceImpl implements AppIdSecretConfigService {

	@Autowired
	private AppIdSecretConfigRepository appIdSecretConfigRepository;
	@Override
	public AppIdSecretConfig findByWechatPublicId(String wechatPublicId) {
		return appIdSecretConfigRepository.findByWechatPublicId(wechatPublicId);
	}
	
	@Override
	public List<AppIdSecretConfig> findAll() {
		return appIdSecretConfigRepository.findAll();
	}
}
