package com.haipai.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class WechatUtil {
	private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	// 获取access_token的接口地址（GET） 限200(次/天)
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public final static String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	private WechatUtil() {

	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.GET_METHOD, null);
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				logger.info("getAccessToken error:", e);
			}
		}
		return accessToken;
	}

	public static String getJSApiTicket(String accessToken) {
		String ticketUrl = ticket_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(ticketUrl, HttpRequestUtil.GET_METHOD, null);
		if (null != jsonObject) {
			try {
				return jsonObject.getString("ticket");
			} catch (JSONException e) {
				logger.info("getJSApiTicket error:", e);
			}
		}
		return "";
	}

	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuilder buffer = new StringBuilder();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.", ce);
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		}
		return jsonObject;
	}

	public static Date plusDay(int num, Date currentDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(currentDate);
		ca.add(Calendar.DATE, num);
		return ca.getTime();
	}

	public static Date plusMinutes(int num, Date currentDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(currentDate);
		ca.add(Calendar.MINUTE, num);
		return ca.getTime();
	}

}
