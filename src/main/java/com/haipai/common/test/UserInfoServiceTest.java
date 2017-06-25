package com.haipai.common.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.haipai.common.WeChatApplication;
import com.haipai.common.entity.UserGroupInfo;
import com.haipai.common.service.UserInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeChatApplication.class)
@WebAppConfiguration
public class UserInfoServiceTest {
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Test
	public void testFindBywechatPublicIdAndOpenId(){
		UserGroupInfo userGroupInfo = userInfoService.findBywechatPublicIdAndOpenId("gh_6aa7537a5c83","o8JhXwjonxQPaJgDxDxHOlM2IKMk");
		System.out.println(userGroupInfo.getGroupid());
	}
	
	@Test
	public void testAddSubscribeGroupUser(){
		userInfoService.addSubscribeGroupUser("o8JhXwjonxQPaJgDxDxHOlM2IKMk","gh_6aa7537a5c83");
	}

}
