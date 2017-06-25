package com.haipai.common.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haipai.common.entity.UserCoupon;
import com.haipai.common.service.UserCouponService;

@Controller
public class UserCouponController {

	private static final String SUCCESS = "success";

	private static final String WECHAT_PUBLIC_ID = "wechatPublicId";

	private static final String WECHAT_USER = "wechatUser";

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserCouponService userCouponService;
	
	@RequestMapping(value = "/generateOneUserCouponAfterShared")
	@ResponseBody
	public Map<String,String> generateOneUserCouponAfterShared(String sharedQRCodeId, HttpSession session) {
		Map<String,String> result = new HashMap<>();
		try {
			UserCoupon userCoupon= userCouponService.findByCouponId(Long.parseLong(sharedQRCodeId));
			if(userCoupon!=null){
				String fromUserNameFromSession = (String) session.getAttribute(WECHAT_USER);
				String wechatPublicIdFromSession = (String) session.getAttribute(WECHAT_PUBLIC_ID);
				//分享后，奖励一个新的红包并且只进行一次
				result = userCouponService.generateOneUserCouponHBAfterShared(fromUserNameFromSession, wechatPublicIdFromSession);
				if(result.get("result").equals(SUCCESS)){
					userCoupon.setCouponType(4);//将当前的红包分享出去后，对自己不可见
					userCoupon.setValidateFlag(0);//对自己不可见
					userCouponService.saveUserCoupon(userCoupon);
				}else{//如果再次将自己的红包分享出去，则将分享出去的红包设置为4对自己不可见
					userCoupon.setCouponType(6);//将当前的红包分享出去后，对自己不可见
					userCoupon.setValidateFlag(0);//对自己不可见
					userCouponService.saveUserCoupon(userCoupon);
					Map<Integer,Map<Integer,Integer>> couponTypeAndNumMapForReferee = new HashMap<>();
					Map<Integer,Integer> couponNumAndValue = new HashMap<>();
					couponNumAndValue.put(1, 10);
					couponTypeAndNumMapForReferee.put(3, couponNumAndValue);//红包一张
					userCouponService.generateCouponsForRefereeAfterUseSharedCoupon(fromUserNameFromSession, wechatPublicIdFromSession, couponTypeAndNumMapForReferee);

				}
			}
			
		} catch (Exception e) {
			logger.error("generateOneUserCouponAfterShared error", e);
		}
		return result;
	}
	
	
	@RequestMapping(value = "/doubleCouponValueAfterShared")
	@ResponseBody
	public Map<String,String> doubleCouponValueAfterShared(String sharedQRCodeId, HttpSession session) {
		Map<String,String> result = new HashMap<>();
		try {
			UserCoupon userCoupon= userCouponService.findByCouponId(Long.parseLong(sharedQRCodeId));
			logger.info("doubleCouponValueAfterShared sharedQRCodeId {}",sharedQRCodeId);
			if(userCoupon!=null){
				String fromUserNameFromSession = (String) session.getAttribute(WECHAT_USER);
				String wechatPublicIdFromSession = (String) session.getAttribute(WECHAT_PUBLIC_ID);
				result = userCouponService.generateOneUserCouponDKQAfterShared(fromUserNameFromSession, wechatPublicIdFromSession);
				if(result.get("result").equals(SUCCESS)){
					userCoupon.setCouponType(5);//将当前的抵扣券分享出去
					userCoupon.setValidateFlag(0);//对自己不可见
					userCouponService.saveUserCoupon(userCoupon);
				}else{
					userCoupon.setCouponType(5);//将当前的抵扣券分享出去
					userCoupon.setValidateFlag(0);//对自己不可见
					userCouponService.saveUserCoupon(userCoupon);
					Map<Integer,Map<Integer,Integer>> couponTypeAndNumMapForReferee = new HashMap<>();
					Map<Integer,Integer> couponDKQNumAndValue = new HashMap<>();
					couponDKQNumAndValue.put(1, 20);
					couponTypeAndNumMapForReferee.put(1, couponDKQNumAndValue);//抵扣券一张
					userCouponService.generateCouponsForRefereeAfterUseSharedCoupon(fromUserNameFromSession, wechatPublicIdFromSession, couponTypeAndNumMapForReferee);
				}
			}
		} catch (Exception e) {
			logger.error("doubleCouponValueAfterShared error", e);
		}
		logger.info("doubleCouponValueAfterShared result {}",result);
		return result;
	}
	
	@RequestMapping(value = "/userCoupon")
	@ResponseBody
	public List<UserCoupon> getUserCoupon(HttpSession session) {
		try {
			String fromUserNameFromSession = (String) session.getAttribute(WECHAT_USER);
			String wechatPublicIdFromSession = (String) session.getAttribute(WECHAT_PUBLIC_ID);
			logger.info("getUserCoupon fromUserNameFromSession:{}", fromUserNameFromSession);
			return userCouponService.findByFromUserNameAndToUserName(fromUserNameFromSession,wechatPublicIdFromSession);
		} catch (Exception e) {
			logger.error("getUserCoupon error", e);
		}
		return Collections.emptyList();
	}
}
