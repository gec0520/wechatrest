package com.haipai.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haipai.common.entity.WeChatQRCode;

import net.sf.json.JSONObject;

public class QRCodeUtil {

	private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

	private QRCodeUtil(){
		
	}
	/**
	 * 创建临时带参数二维码
	 * 
	 * @param accessToken
	 * @expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
	 * @param sceneId
	 *            场景Id
	 * @return
	 */
	public static WeChatQRCode createTemporaryORCode(String accessToken, String expireSeconds, int sceneId) {
		WeChatQRCode weChatQRCode = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
		requestUrl = requestUrl.replace("TOKEN", accessToken);
		// 需要提交的JSON数据
		String outputStr = "{\"expire_seconds\": %s,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\"：%d}}}";
		// 创建临时带参数二维码
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.POST_METHOD,
				String.format(outputStr, expireSeconds, sceneId));
		if (null != jsonObject) {
			try {
				weChatQRCode = new WeChatQRCode();
				weChatQRCode.setTicket(jsonObject.getString("ticket"));
				weChatQRCode.setExpire_Seconds(jsonObject.getInt("expire_seconds"));
				logger.info("创建临时带参二维码成功,ticket=" + weChatQRCode.getTicket() + ",expire_seconds="
						+ weChatQRCode.getExpire_Seconds());
			} catch (Exception e) {
				logger.info("createTemporaryORCode error",e);
			}
		}
		return weChatQRCode;
	}

	/**
	 * 创建永久二维码
	 * 
	 * @param accessToken
	 * @param sceneId
	 *            场景Id
	 * @param sceneStr
	 *            场景IdsceneStr
	 * @return
	 */
	// 数字ID用这个{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene":
	// {"scene_id": 123}}}
	// 或者也可以使用以下POST数据创建字符串形式的二维码参数：
	// 字符ID用这个{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene":
	// {"scene_str": "hfrunffgha"}}}
	public static String createPermanentORCode(String accessToken, String sceneStr) {
		String ticket = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
		requestUrl = requestUrl.replace("TOKEN", accessToken);
		String outputStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\":{\"scene\": {\"scene_str\":%s}}}";
		logger.info("createPermanentORCode sceneStr {}",sceneStr);
		JSONObject jsonObject = HttpRequestUtil.httpRequestJSONObject(requestUrl, HttpRequestUtil.POST_METHOD,
				String.format(outputStr, sceneStr));
		if (null != jsonObject) {
			try {
				ticket = jsonObject.getString("ticket");
			} catch (Exception e) {
				logger.info("createPermanentORCode error:",e);
			}
		}
		return ticket;
	}

	public static String showQRcode(String ticket) {
		String showUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		showUrl = showUrl.replace("TICKET", HttpRequestUtil.urlEncode(ticket, "utf-8"));
		return showUrl;
	}

	public static InputStream getQRCode(String urlString) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();
		return is;
	}

	public static void download(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}
	public static void main(String[] args) throws Exception{
		AccessToken accessToken = WechatUtil.getAccessToken("wx588656d56aa7849e", "04e9555cc936bba1f4f80dc439ef0d27");
		System.out.println(String.format("AccessToken is %s", accessToken));
		String permanentORCode = createPermanentORCode(accessToken.getToken(), "340");
		// WeChatQRCode weChatQRCode =
		// WechatUtil.createTemporaryORCode(accessToken.getToken(),
		// Integer.toString(accessToken.getExpiresIn()), 123);
		// System.out.println(weChatQRCode.getTicket());
		String showUrl = showQRcode(permanentORCode);
		download(showUrl, "testQRCode.jpg", "C:\\Users\\Thinkpad\\Desktop");
	}
}
