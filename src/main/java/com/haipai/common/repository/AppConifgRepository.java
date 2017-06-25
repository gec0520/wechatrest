package com.haipai.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haipai.common.entity.AppConfig;

public interface AppConifgRepository extends JpaRepository<AppConfig, String> {
	public AppConfig findByConfigCode(String configCode);
}
