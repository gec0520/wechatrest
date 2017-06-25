package com.haipai.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haipai.common.entity.UserGroupInfo;

public interface UserGroupInfoRepository extends JpaRepository<UserGroupInfo, Long> {
	public UserGroupInfo findByWechatPublicIdAndOpenId(String wechatpublicId,String openId);
}
