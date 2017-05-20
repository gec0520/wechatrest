package com.haipai.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haipai.common.entity.WechatTokenConfig;

public interface WechatTokenConfigRepository extends JpaRepository<WechatTokenConfig, Integer> {

	public WechatTokenConfig findByTokenTypeAndAppId(String tokenType,String appId);
	
}
