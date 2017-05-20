package com.haipai.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haipai.common.entity.AppIdSecretConfig;

public interface AppIdSecretConfigRepository extends JpaRepository<AppIdSecretConfig, Integer> {

	public AppIdSecretConfig findByWechatPublicId(String wechatPublicId);
}
