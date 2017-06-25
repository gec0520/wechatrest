package com.haipai.common.service;

import com.haipai.common.entity.UserGroupInfo;

public interface UserInfoService {
	public UserGroupInfo generateUserGroupInfo(String accessToken, String openId,String wechatpublicId);
	public UserGroupInfo findBywechatPublicIdAndOpenId(String wechatpublicId,String openId);
	public void addNewUserGroupInfo(UserGroupInfo userGroupInfo);
	public void deleteUserGroupInfo(UserGroupInfo userGroupInfo);
	public void deleteSubscribeGroupUser(String fromUserName, String toUserName);
	public void addSubscribeGroupUser(String fromUserName, String toUserName);
}
