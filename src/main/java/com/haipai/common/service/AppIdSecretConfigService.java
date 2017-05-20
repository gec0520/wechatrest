package com.haipai.common.service;

import java.util.List;

import com.haipai.common.entity.AppIdSecretConfig;

public interface AppIdSecretConfigService {
	public AppIdSecretConfig findByWechatPublicId(String wechatPublicId);
	public List<AppIdSecretConfig> findAll();
}
