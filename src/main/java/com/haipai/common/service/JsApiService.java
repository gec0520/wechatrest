package com.haipai.common.service;

import java.util.Map;

public interface JsApiService {
	public String getJSApiTicket(String appId);
	
	public Map<String, String> getSignature(String jsapi_ticket, String url);
}
