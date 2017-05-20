package com.haipai.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haipai.common.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
	public UserCoupon findByCouponId(Long couponId);
	public List<UserCoupon> findByFromUserName(String fromUserName);
}
