package com.haipai.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user_coupon")
public class UserCoupon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3922543630951395714L;
	private Long couponId;
	private Integer couponValue;
	private String fromUserName;
	private String toUserName;
	private String couponQRCode;
	private Integer validateFlag;
	private Date createTime;
	private Date expireTime;
	private String couponCreateTime;
	private String couponExpireTime;
	private String couponValidate;
	private Integer couponType;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "coupon_id")
	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	@Column(name = "coupon_value")
	public Integer getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(Integer couponValue) {
		this.couponValue = couponValue;
	}
	@Column(name = "from_user_name")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@Column(name = "to_user_name")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
	@Column(name = "coupon_qr_code")
	public String getCouponQRCode() {
		return couponQRCode;
	}

	public void setCouponQRCode(String couponQRCode) {
		this.couponQRCode = couponQRCode;
	}
	@Column(name = "validate_flag")
	public Integer getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(Integer validateFlag) {
		this.validateFlag = validateFlag;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "expire_time")
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Column(name = "coupon_type")
	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	@Transient
	public String getCouponCreateTime() {
		return couponCreateTime;
	}

	public void setCouponCreateTime(String couponCreateTime) {
		this.couponCreateTime = couponCreateTime;
	}

	@Transient
	public String getCouponExpireTime() {
		return couponExpireTime;
	}

	public void setCouponExpireTime(String couponExpireTime) {
		this.couponExpireTime = couponExpireTime;
	}

	@Transient
	public String getCouponValidate() {
		return couponValidate;
	}

	public void setCouponValidate(String couponValidate) {
		this.couponValidate = couponValidate;
	}
}
