package com.haipai.common.service;

import com.haipai.common.usergroup.UserGroupInfo;

public interface UserInfoService {
	public UserGroupInfo getUserGroupInfo(String access_token, String openid);
}
