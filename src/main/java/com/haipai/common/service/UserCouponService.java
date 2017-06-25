package com.haipai.common.service;

import java.util.List;
import java.util.Map;

import com.haipai.common.entity.UserCoupon;
import com.haipai.common.message.resp.Article;
import com.haipai.common.message.resp.NewsMessage;

public interface UserCouponService {
	public UserCoupon findByCouponId(Long couponId) throws Exception;
	public UserCoupon saveUserCoupon(UserCoupon userCoupon) throws Exception;
	public List<UserCoupon> saveUserCoupons(List<UserCoupon> userCoupons) throws Exception;
	public List<UserCoupon> findByFromUserNameAndToUserName(String fromUserName,String toUserName) throws Exception;
	public Map<String,String> generateOneUserCouponHBAfterShared(String fromUserName, String toUserName) throws Exception;
	public UserCoupon allocateCouponToUser(String fromUserName, String toUserName,Integer couponType,Integer couponValue) throws Exception;
	public NewsMessage generateCouponsAfterSubscribe(String fromUserName, String toUserName, List<Article> articleList,
			Map<Integer,Map<Integer,Integer>> couponMap)  throws Exception;
	public String useCoupon(UserCoupon userCoupon) throws Exception;
	public void generateCouponsForRefereeAfterUseSharedCoupon(String fromUserName, String toUserName,Map<Integer,Map<Integer,Integer>> map)  throws Exception;
	public Map<String,String> generateOneUserCouponDKQAfterShared(String fromUserName, String toUserName) throws Exception;
}
