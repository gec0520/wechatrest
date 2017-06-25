package com.haipai.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haipai.common.message.resp.Button;
import com.haipai.common.message.resp.CommonButton;
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

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		
		CommonButton commonButton15 = new CommonButton();
		commonButton15.setName("Wi_Fi密码");
		commonButton15.setType("click");
		commonButton15.setKey("11");
		
		ViewButton viewButton14 = new ViewButton();
		viewButton14.setName("门店信息");
		viewButton14.setType("view");
		viewButton14.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDUyMDY4OA==&tempkey=Z%2F%2BSoJbC3m3qQytRPAPU6vf7EzN5Z6DYxuXxy0Tfq9aLA%2FaHw9EgD5YEp3jBcZ9BSU4PVizIVdgrqqntacf8Y5XhnpiHA%2FwfyKUttuL5bmWGam8VI%2F56r0odSXLnEmwHdKBtYuRNq4atVWIBHh5qiQ%3D%3D&chksm=6a692e9b5d1ea78dca61b90786b4c55ff7b9d3afb358cfe99a6d27dacad1dbd12b7d951cd077#rd");
		
		ViewButton viewButton13 = new ViewButton();
		viewButton13.setName("餐厅菜单");
		viewButton13.setType("view");
		viewButton13.setUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/9SoGqvUNF7Y6qRcEzhXJmG2fWq4I1icZsiaPlc5qmymYvnXC9GLnc4MlibiakZE0tYVt2EFUVicLmXEm7wK2y7TSD2w/0?wx_fmt=jpeg");

		ViewButton viewButton12 = new ViewButton();
		viewButton12.setName("虾说往事");
		viewButton12.setType("view");
		viewButton12.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDUyMDY4OA==&tempkey=Z%2F%2BSoJbC3m3qQytRPAPU6vf7EzN5Z6DYxuXxy0Tfq9aXeL6YlS5ZJZ8zD5CJ%2B8q6oROF3O7nB0jUPzF%2FypXiXtL4eG4l%2ByR1hp8WTg2kmTeGam8VI%2F56r0odSXLnEmwHwZUIv1zCjPATdEI9cwsFfA%3D%3D&chksm=6a692e9c5d1ea78a084bdc05c86665bf8ed9cb7b30332dda8b93bd0abaaf2b3e8d39d29a6f2b#rd");

		ViewButton viewButton11 = new ViewButton();
		viewButton11.setName("品牌故事");
		viewButton11.setType("view");
		viewButton11.setUrl("http://mp.weixin.qq.com/s?__biz=MzI2MDUyMDY4OA==&mid=2247483657&idx=1&sn=9ea4559c83fe92dddf02918cd3a38c3b&chksm=ea692e96dd1ea78045d5c55338c5010244b06a75615aa54a79c3c5bd528bb13937ddf4dc453c&scene=18#wechat_redirect");

		ComplexButton complexButton1 = new ComplexButton();
		complexButton1.setName("关于虾说");
		complexButton1.setSub_button(new Button[] { viewButton11,viewButton12,viewButton13,viewButton14,commonButton15 });
		
		
		ViewButton viewButton23 = new ViewButton();
		viewButton23.setName("我的优惠券");
		viewButton23.setType("view");
		viewButton23.setUrl("http://haipai.duapp.com/redirect?wechatpublicid=gh_6aa7537a5c83");
		
		ViewButton viewButton22 = new ViewButton();
		viewButton22.setName("成长计划");
		viewButton22.setType("view");
		viewButton22.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDUyMDY4OA==&tempkey=Z%2F%2BSoJbC3m3qQytRPAPU6vf7EzN5Z6DYxuXxy0Tfq9YaeZsfybufMgyoDnJ2HBbcRlY2zBkY4I2MLMJrzGo4s8TzV8dEPsyKlYooMCQ9EPGGam8VI%2F56r0odSXLnEmwHuu6HgsrG667hKi8%2FAKxspw%3D%3D&chksm=6a692e9a5d1ea78c6a25455734375a1d8fcdf170314f2a272d753ee45d2e97fb035ea622ea33#rd");
		
		ViewButton viewButton21 = new ViewButton();
		viewButton21.setName("我的会员卡");
		viewButton21.setType("view");
		viewButton21.setUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/9SoGqvUNF7Y6qRcEzhXJmG2fWq4I1icZs1fqz99BefibyUtuNicmJzFqkicEHhCiconWwd1fLYtOUd6icE7UPCib7qUvQ/0?wx_fmt=jpeg");
		
		
		ComplexButton complexButton2 = new ComplexButton();
		complexButton2.setName("我的虾说");
		complexButton2.setSub_button(new Button[] { viewButton21,viewButton22,viewButton23 });
		
		ViewButton viewButton31 = new ViewButton();
		viewButton31.setName("最新活动");
		viewButton31.setType("view");
		viewButton31.setUrl("http://mp.weixin.qq.com/s?__biz=MzI2MDUyMDY4OA==&mid=2247483658&idx=1&sn=b750fd42bf626070c83692885b56f68d&chksm=ea692e95dd1ea783a91d18f6b2f827981ea229c6689e3cdc45c14a10f5d54e179b0a410b4069&scene=18#wechat_redirect");
		
		
		Menu menu = new Menu();
		menu.setButton(new Button[] { complexButton1,complexButton2,viewButton31});

		return menu;
	}

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx588656d56aa7849e";
		// 第三方用户唯一凭证密钥
		String appSecret = "04e9555cc936bba1f4f80dc439ef0d27";
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
