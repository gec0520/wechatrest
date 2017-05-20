package com.haipai.common.entity;

public class WeChatQRCode {
	private String ticket;
	private Integer expire_Seconds;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpire_Seconds() {
		return expire_Seconds;
	}

	public void setExpire_Seconds(Integer expire_Seconds) {
		this.expire_Seconds = expire_Seconds;
	}

}
