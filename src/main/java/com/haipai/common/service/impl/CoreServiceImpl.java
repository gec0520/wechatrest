package com.haipai.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.entity.UserCoupon;
import com.haipai.common.entity.WechatTokenConfig;
import com.haipai.common.message.resp.Article;
import com.haipai.common.message.resp.NewsMessage;
import com.haipai.common.message.resp.TextMessage;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.CoreService;
import com.haipai.common.service.UserCouponService;
import com.haipai.common.service.UserInfoService;
import com.haipai.common.service.WechatTokenConfigService;
import com.haipai.common.usergroup.UserGroupInfo;
import com.haipai.common.util.AccessToken;
import com.haipai.common.util.MessageUtil;
import com.haipai.common.util.QRCodeUtil;
import com.haipai.common.util.WechatUtil;

@Service
@Transactional
public class CoreServiceImpl implements CoreService {
	
	private static Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private AppIdSecretConfigService appIdSecretConfigService;

	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private WechatTokenConfigService wechatTokenConfigService;
	
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			List<Article> articleList = new ArrayList<>();
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "您发送的是文本消息！";
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					int couponNum = 3;
					NewsMessage newsMessage  = generateCoupons(fromUserName, toUserName, articleList, couponNum);
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
					return respMessage;
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
					respContent = "点击菜单！";
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					if (requestMap.get("EventKey") != null && "123".equals(requestMap.get("EventKey"))) {
						return "";
					}
					
					String appId = (String) request.getSession().getAttribute("appId");
					WechatTokenConfig wechatTokenConfig = wechatTokenConfigService.findByTokenTypeAndAppId("access_token", appId);
					UserGroupInfo userGroupInfo = userInfoService.getUserGroupInfo(wechatTokenConfig.getTokenValue(), fromUserName);
					if(userGroupInfo!=null && "1".equals(userGroupInfo.getGroupid())){
						return userGroupInfo+"|"+fromUserName;
					}
					if (requestMap.get("EventKey") != null && !"".equals(requestMap.get("EventKey"))) {
						Long couponId = Long.parseLong((requestMap.get("EventKey")));
						UserCoupon userCoupon = userCouponService.findByCouponId(couponId);
						String result = useCoupon(userCoupon);
						if ("success".equals(result)) {
							respContent = "您正在使用" + userCoupon.getCouponValue() + "元优惠券";
						} else {
							respContent = result;
						}
					}
				} else {
					respContent = "";
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			respMessage = "exception";
			logger.error("processRequest error:",e);
		}

		return respMessage;
	}

	private NewsMessage generateCoupons(String fromUserName, String toUserName, List<Article> articleList, int couponNum) {
		NewsMessage newsMessage = new NewsMessage();
		AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
		AccessToken accessToken = WechatUtil.getAccessToken(appIdSecretConfig.getAppId(), appIdSecretConfig.getAppSecret());
		Article articleSummaryInfo = new Article();
		articleSummaryInfo.setTitle("恭喜你获得优惠券" + couponNum + "张");
		articleSummaryInfo.setDescription("优惠券");
		articleSummaryInfo.setPicUrl("");
		articleSummaryInfo.setUrl("");
		articleList.add(articleSummaryInfo);
		try {
			for (int i = 1; i <= couponNum; i++) {
				UserCoupon userCoupon;
				userCoupon = allocateCouponToUser(fromUserName, toUserName);
				String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(),
						Long.toString(userCoupon.getCouponId()));
				String showUrl = QRCodeUtil.showQRcode(permanentORCode);
				userCoupon.setCouponQRCode(showUrl);
				userCouponService.saveUserCoupon(userCoupon);
				Article article = new Article();
				article.setTitle("优惠券" + userCoupon.getCouponValue() + "元");
				article.setDescription("优惠券");
				article.setPicUrl("");
				article.setUrl(showUrl);
				articleList.add(article);
			}
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			newsMessage.setArticleCount(articleList.size());
			newsMessage.setArticles(articleList);
		} catch (Exception e) {
			logger.error("generateCoupons error",e);
		}
		return newsMessage;
	}

	private UserCoupon allocateCouponToUser(String fromUserName, String toUserName) throws Exception {
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setCouponValue(10);
		Date createTime = new Date();
		userCoupon.setCreateTime(createTime);
		userCoupon.setExpireTime(WechatUtil.plusDay(30, createTime));
		userCoupon.setFromUserName(fromUserName);
		userCoupon.setToUserName(toUserName);
		userCoupon.setValidateFlag(1);
		return userCouponService.saveUserCoupon(userCoupon);
	}



	private String useCoupon(UserCoupon userCoupon) throws Exception {
		if (userCoupon.getValidateFlag().intValue() == 0) {
			return "该优惠券已经被使用";
		}

		if (userCoupon.getExpireTime().before(new Date())) {
			return "该优惠券已经过期";
		}

		userCoupon.setValidateFlag(0);
		userCouponService.saveUserCoupon(userCoupon);
		return "success";
	}
}
