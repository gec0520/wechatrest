package com.haipai.common.service;

import java.util.List;

import com.haipai.common.entity.UserCoupon;

public interface UserCouponService {
	public UserCoupon findByCouponId(Long couponId) throws Exception;
	public UserCoupon saveUserCoupon(UserCoupon userCoupon) throws Exception;
	public List<UserCoupon> findByFromUserName(String fromUserName) throws Exception;
}
