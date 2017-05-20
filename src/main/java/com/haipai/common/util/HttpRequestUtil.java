package com.haipai.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * Http请求工具类
 * 
 * @author snowfigure
 * @since 2014-8-24 13:30:56
 * @version v1.0.1
 */
public class HttpRequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

	static boolean proxySet = false;
	static String proxyHost = "127.0.0.1";
	static int proxyPort = 8087;
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";

	private HttpRequestUtil(){
		
	}
	/**
	 * 编码
	 * 
	 * @param source
	 * @return
	 */
	public static String urlEncode(String source, String encode) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error("urlEncode error:",e);
			return "0";
		}
		return result;
	}

	public static String urlEncodeGBK(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "GBK");
		} catch (UnsupportedEncodingException e) {
			logger.error("urlEncodeGBK error:",e);
			return "0";
		}
		return result;
	}

	/**
	 * 发起http请求获取返回结果
	 * 
	 * @param req_url
	 *            请求地址
	 * @return
	 */
	public static String httpRequest(String req_url) {
		StringBuilder builder = new StringBuilder();
		try {
			URL url = new URL(req_url);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				builder.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			httpUrlConn.disconnect();

		} catch (Exception e) {
			logger.error("httpRequest error:",e);
		}
		return builder.toString();
	}

	/**
	 * 发送http请求取得返回的输入流
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @return InputStream
	 */
	public static InputStream httpRequestIO(String requestUrl) {
		InputStream inputStream = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			// 获得返回的输入流
			inputStream = httpUrlConn.getInputStream();
		} catch (Exception e) {
			logger.error("httpRequestIO error:",e);
		}
		return inputStream;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("sendGet error:",e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error("sendGet error:",e2);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param isproxy
	 *            是否使用代理模式
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, boolean isproxy) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			if (isproxy) {// 使用代理模式
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				conn = (HttpURLConnection) realUrl.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// 打开和URL之间的连接

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); // POST方法

			// 设置通用的请求属性

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("sendPost error:",e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error("sendPost error:",ex);
			}
		}
		return result;
	}

	public static JSONObject httpRequestJSONObject(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// 创建SSLContext对象
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式
			conn.setRequestMethod(requestMethod);

			if (GET_METHOD.equalsIgnoreCase(requestMethod)) {
				conn.connect();
			}

			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			StringBuilder builder = new StringBuilder();
			while ((str = bufferedReader.readLine()) != null) {
				builder.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			conn.disconnect();
			jsonObject = JSONObject.fromObject(builder.toString());
		} catch (Exception e) {
			logger.info("httpRequestJSONObject: ", e);
		}
		return jsonObject;
	}

	public static void main(String[] args) {
		// demo:代理访问
		String url = "http://api.adf.ly/api.php";
		String para = "key=youkeyid&youuid=uid&advert_type=int&domain=adf.ly&url=http://somewebsite.com";

		String sr = HttpRequestUtil.sendPost(url, para, true);
		System.out.println(sr);
	}

}
