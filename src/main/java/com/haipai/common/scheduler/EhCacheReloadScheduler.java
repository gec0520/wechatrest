package com.haipai.common.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.haipai.common.service.AppConfigService;

@Component
public class EhCacheReloadScheduler {
	
	@Autowired
	private AppConfigService appConfigService;
	
	@Scheduled(fixedDelay = 60*1000)
	public void updateEhCache(){
		appConfigService.clearAllCache();
	}
}
