package com.haipai.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ID_SECRET_CONFIG")
public class AppIdSecretConfig implements Serializable{
	private static final long serialVersionUID = 7546695196237225179L;

	private Integer Id;
	private String wechatPublicId;
	private String wechatPublicIdDesc;
	private String appSecret;
	private String appId;
	private Date createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	@Column(name = "WECHAT_PUBLIC_ID")
	public String getWechatPublicId() {
		return wechatPublicId;
	}
	public void setWechatPublicId(String wechatPublicId) {
		this.wechatPublicId = wechatPublicId;
	}
	
	@Column(name = "WECHAT_PUBLIC_ID_DESC")
	public String getWechatPublicIdDesc() {
		return wechatPublicIdDesc;
	}
	public void setWechatPublicIdDesc(String wechatPublicIdDesc) {
		this.wechatPublicIdDesc = wechatPublicIdDesc;
	}
	
	@Column(name = "APP_SECRET")
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	@Column(name = "APP_ID")
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

	
	
	
	
}
