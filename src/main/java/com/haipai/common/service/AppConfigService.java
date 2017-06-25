package com.haipai.common.service;

import com.haipai.common.entity.AppConfig;

public interface AppConfigService {
	public AppConfig findByConfigCode(String configCode);
	public void clearAllCache();
}
