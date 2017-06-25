package com.haipai.common.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.AppConfig;
import com.haipai.common.repository.AppConifgRepository;
import com.haipai.common.service.AppConfigService;

@Service
@Transactional
public class AppConifgServiceImpl implements AppConfigService{
	private static Logger logger = LoggerFactory.getLogger(AppConifgServiceImpl.class);

	@Autowired
	private AppConifgRepository appConifgRepository;
	public static final String APP_CONIFG = "appConfig";
	@Override
	@Cacheable(value = APP_CONIFG)
	public AppConfig findByConfigCode(String configCode) {
		logger.info("get appconif directly");
		return appConifgRepository.findByConfigCode(configCode);
	}
	
	@Override
	@Caching(evict={
			@CacheEvict(value=APP_CONIFG,allEntries=true,beforeInvocation=true)
	})
	public void clearAllCache(){
		logger.info("clear ehcache at {}", new Date());
	}

}
