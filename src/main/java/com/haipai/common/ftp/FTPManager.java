package com.haipai.common.ftp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.haipai.common.service.impl.UserCouponServiceImpl;
import com.haipai.common.util.AccessToken;
import com.haipai.common.util.QRCodeUtil;
import com.haipai.common.util.WechatUtil;

public class FTPManager {
	public final static String ACCESS_KEY_ID = "9cd799fb566142b7bfdc2dcaf8c0fd32";
	public final static String SECRET_ACCESS_KEY = "67deff7053524290aa4141a1930f9a9f";
	private static Logger logger = LoggerFactory.getLogger(FTPManager.class);
	
	public void getObject(BosClient client, String bucketName, String objectKey) throws IOException {
		BosObject object = client.getObject(bucketName, objectKey);
		object.getObjectMetadata();
		InputStream objectContent = object.getObjectContent();
		objectContent.close();
	}

	public static void putObject2BOS(String bucketName, String objectKey,String showUrl) throws IOException {
		try{
			BosClientConfiguration config = new BosClientConfiguration();
			config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
			BosClient client = new BosClient(config);
			if(!client.doesBucketExist(bucketName)){
				client.createBucket(bucketName);
			}
			URL url = new URL(showUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5 * 1000);
			InputStream is = con.getInputStream();
			client.putObject(bucketName, objectKey, is);
			is.close();
		}catch(Exception e){
			logger.error("putObject error",e);
		}
		 
	}

	public static void main(String[] args) throws IOException{
		FTPManager manager = new FTPManager();
	    // 初始化一个BosClient
	    AccessToken accessToken = WechatUtil.getAccessToken("wx666b675805356a12", "25466e932ac4b4c3b5216ad782d5dde1");
		String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(), "123");
		String showUrl = QRCodeUtil.showQRcode(permanentORCode);
		manager.putObject2BOS("haipaipic", permanentORCode+".jpg",showUrl);
	}
}
