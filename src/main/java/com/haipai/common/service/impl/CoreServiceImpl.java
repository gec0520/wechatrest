package com.haipai.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.constant.WeChatRestConstant;
import com.haipai.common.entity.AppConfig;
import com.haipai.common.entity.UserCoupon;
import com.haipai.common.entity.UserGroupInfo;
import com.haipai.common.message.resp.Article;
import com.haipai.common.message.resp.NewsMessage;
import com.haipai.common.message.resp.TextMessage;
import com.haipai.common.service.AppConfigService;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.CoreService;
import com.haipai.common.service.UserCouponService;
import com.haipai.common.service.UserInfoService;
import com.haipai.common.service.WechatTokenConfigService;
import com.haipai.common.util.MessageUtil;

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
	private UserInfoService userInfoService;

	@Autowired
	private AppConfigService appConfigService;

	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";

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
				respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "感谢您对本公众号的关注，优惠活动请至菜单查询";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					userInfoService.addSubscribeGroupUser(fromUserName, toUserName);
					String eventKey = requestMap.get("EventKey");
					logger.info("processRequest eventKey {}",eventKey);//二维码的参数
					if(eventKey!=null && !"".equals(eventKey)){
						String[] eventKeyArray = eventKey.split("_");
						if(eventKeyArray!=null && eventKeyArray.length==2){
							Long couponId = Long.parseLong(eventKeyArray[1]);
							UserCoupon userCoupon = userCouponService.findByCouponId(couponId);
							if(userCoupon!=null && userCoupon.getCouponType()==4){//被分享用户扫描分享红包的二维码则追踪分享者,给予一个红包奖励
								String refereeUserId = userCoupon.getFromUserName();
								if(!fromUserName.equals(refereeUserId)){//被分享的用户与分享的用户不是同一人才给予分享者红包奖励，防止自己将红包分享给自己
									Map<Integer,Map<Integer,Integer>> couponMapForReferee = new HashMap<>();
									Map<Integer,Integer> couponHBNumAndValue = new HashMap<>();
									couponHBNumAndValue.put(1, 10);
									couponMapForReferee.put(3, couponHBNumAndValue);//10元红包一张
									userCouponService.generateCouponsForRefereeAfterUseSharedCoupon(refereeUserId, toUserName, couponMapForReferee);
								}
							}
						}
					}
					
					Map<Integer,Map<Integer,Integer>> couponMap = new HashMap<>();
					Map<Integer,Integer> couponDKQNumAndValue = new HashMap<>();
					couponDKQNumAndValue.put(1, 10);//10元抵扣券一张
					couponMap.put(1, couponDKQNumAndValue);
					Map<Integer,Integer> couponZKQNumAndValue = new HashMap<>();
					couponZKQNumAndValue.put(1, 10);//10元折扣券一张
					couponMap.put(2, couponZKQNumAndValue);//折扣券一张
					Map<Integer,Integer> couponHBNumAndValue = new HashMap<>();
					couponHBNumAndValue.put(1, 10);//10元红包一张
					couponMap.put(3, couponHBNumAndValue);//红包一张
					List<UserCoupon> userCouponList = userCouponService.findByFromUserNameAndToUserName(fromUserName,toUserName);
					if(userCouponList==null || userCouponList.isEmpty()){//重复关注不给予优惠券
						NewsMessage newsMessage = userCouponService.generateCouponsAfterSubscribe(fromUserName, toUserName, articleList,
								couponMap);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					respContent="";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
					return respMessage;
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					userInfoService.deleteSubscribeGroupUser(fromUserName, toUserName);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
					respContent = "点击菜单！";
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					if (requestMap.get("EventKey") != null && "123".equals(requestMap.get("EventKey"))) {
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}
					UserGroupInfo userGroupInfo = userInfoService.findBywechatPublicIdAndOpenId(toUserName,
							fromUserName);

					AppConfig appConfig = appConfigService
							.findByConfigCode(WeChatRestConstant.QRCODE_SCAN_GROUP.getName());
					if (appConfig == null) {
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}

					String[] QRCodeScanGroups = appConfig.getConfigValue().split(",");
					if (QRCodeScanGroups == null || QRCodeScanGroups.length == 0) {
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}

					if (userGroupInfo == null) {
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}

					if (requestMap.get("EventKey") == null || "".equals(requestMap.get("EventKey"))) {
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}
					
					String eventKey = requestMap.get("EventKey");
					Long couponId = Long.parseLong(eventKey);
					Boolean isScanUserInScanGroup = false;
					UserCoupon userCoupon = userCouponService.findByCouponId(couponId);
					if(userCoupon == null){
						respContent="";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						return respMessage;
					}
					if(userCoupon.getCouponType()==1 || userCoupon.getCouponType()==3){//只有关注该公众号并且由系统赠送的红包/抵扣券才可以使用,分享出去的红包/抵扣券不能使用
						for (String QRCodeScanGroup : QRCodeScanGroups) {
							logger.info("QRCodeScanGroup  {} userGroupInfo.getGroupid() {}",QRCodeScanGroup,userGroupInfo.getGroupid());
							if (QRCodeScanGroup.equals(userGroupInfo.getGroupid())) {
								String result = userCouponService.useCoupon(userCoupon);
								if ("success".equals(result)) {
									respContent = "您正在使用" + userCoupon.getCouponValue() + "元优惠券";
								} else {
									respContent = result;
								}
								isScanUserInScanGroup= true;
								break;
							}
						}
						if(!isScanUserInScanGroup){
							respContent="您没有扫描使用优惠券的权限，请联系该店的服务人员扫描使用";
						}
					}else{
						respContent="该优惠券已被分享无法使用,请至'我的优惠券'菜单中使用可用的优惠券";
					}
				} else {
					respContent = "";
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			respMessage = "exception";
			logger.error("processRequest error:", e);
		}

		return respMessage;
	}
}
