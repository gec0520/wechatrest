package com.haipai.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haipai.common.message.resp.Button;
import com.haipai.common.message.resp.ComplexButton;
import com.haipai.common.message.resp.Menu;
import com.haipai.common.message.resp.ViewButton;

import net.sf.json.JSONObject;

public class MenuManage {

	private static Logger logger = LoggerFactory.getLogger(MenuManage.class);

	// 菜单创建（POST） 限100（次/天）
	public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	private MenuManage() {

	}

	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = WechatUtil.httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				logger.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}

		return result;
	}

	private static Menu getMenu() {

		ViewButton userCouponBtn = new ViewButton();
		userCouponBtn.setName("我的红包");
		userCouponBtn.setType("view");
		userCouponBtn.setUrl("http://haipai.duapp.com/redirect?wechatpublicid=gh_9d99e4535d08");
		ComplexButton userAccountBtn = new ComplexButton();
		userAccountBtn.setName("我的账户");
		userAccountBtn.setSub_button(new Button[] { userCouponBtn });

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { userAccountBtn });

		return menu;
	}

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx666b675805356a12";
		// 第三方用户唯一凭证密钥
		String appSecret = "25466e932ac4b4c3b5216ad782d5dde1";
		// 调用接口获取access_token
		AccessToken at = WechatUtil.getAccessToken(appId, appSecret);
		if (null != at) {
			// 调用接口创建菜单
			int result = createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result) {
				logger.info("菜单创建成功！");
			} else {
				logger.info("菜单创建失败，错误码：{}", result);
			}
		}
	}

}
