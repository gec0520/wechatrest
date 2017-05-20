package com.haipai.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.UserCoupon;
import com.haipai.common.repository.UserCouponRepository;
import com.haipai.common.service.UserCouponService;

@Service
@Transactional
public class UserCouponServiceImpl implements UserCouponService {

	@Autowired
	private UserCouponRepository userCouponRepository;
	@Override
	public UserCoupon findByCouponId(Long couponId) throws Exception {
		return userCouponRepository.findByCouponId(couponId);
	}
	@Override
	public UserCoupon saveUserCoupon(UserCoupon userCoupon) throws Exception{
		return userCouponRepository.save(userCoupon);
	}
	@Override
	public List<UserCoupon> findByFromUserName(String fromUserName) throws Exception{
		List<UserCoupon> userCouponList = userCouponRepository.findByFromUserName(fromUserName);
		 SimpleDateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(userCouponList!=null && !userCouponList.isEmpty()){
			for(UserCoupon userCoupon :userCouponList){
				String couponValidate = userCoupon.getValidateFlag().intValue() == 0?"无效":"有效";
				userCoupon.setCouponValidate(couponValidate);
				userCoupon.setCouponCreateTime(dataFormat.format(userCoupon.getCreateTime()));
				userCoupon.setCouponExpireTime(dataFormat.format(userCoupon.getExpireTime()));
			}
		}
		return userCouponList;
	}

}
