package com.haipai.common.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haipai.common.entity.AppIdSecretConfig;
import com.haipai.common.entity.UserCoupon;
import com.haipai.common.ftp.FTPManager;
import com.haipai.common.message.resp.Article;
import com.haipai.common.message.resp.NewsMessage;
import com.haipai.common.repository.UserCouponRepository;
import com.haipai.common.service.AppIdSecretConfigService;
import com.haipai.common.service.UserCouponService;
import com.haipai.common.util.AccessToken;
import com.haipai.common.util.MessageUtil;
import com.haipai.common.util.QRCodeUtil;
import com.haipai.common.util.WechatUtil;

@Service
@Transactional
public class UserCouponServiceImpl implements UserCouponService {

	@Autowired
	private UserCouponRepository userCouponRepository;
	
	@Autowired
	private AppIdSecretConfigService appIdSecretConfigService;
	@Override
	public UserCoupon findByCouponId(Long couponId) throws Exception {
		return userCouponRepository.findByCouponId(couponId);
	}
	@Override
	public UserCoupon saveUserCoupon(UserCoupon userCoupon) throws Exception{
		return userCouponRepository.save(userCoupon);
	}
	
	@Override
	public List<UserCoupon> saveUserCoupons(List<UserCoupon> userCoupons) throws Exception{
		return userCouponRepository.save(userCoupons);
	}
	
	
	@Override
	public List<UserCoupon> findByFromUserNameAndToUserName(String fromUserName,String toUserName) throws Exception{
		List<UserCoupon> userCouponList = userCouponRepository.findByFromUserNameAndToUserNameAndValidateFlag(fromUserName,toUserName,1);
		SimpleDateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(userCouponList!=null && !userCouponList.isEmpty()){
			for(UserCoupon userCoupon :userCouponList){
				String couponValidate = userCoupon.getValidateFlag().intValue() == 0?"无效":"有效";
				userCoupon.setCouponValidate(couponValidate);
				userCoupon.setCouponCreateTime(dataFormat.format(userCoupon.getCreateTime()));
				userCoupon.setCouponExpireTime(dataFormat.format(userCoupon.getExpireTime()));
				/*if(userCoupon.getCouponType()!= null && userCoupon.getCouponType() == 3){
					reAssembleCouponUrl(userCoupon);
				}*/
			}
		}
		return userCouponList;
	}
	
	private void reAssembleCouponUrl(UserCoupon userCoupon){
		StringBuilder reAssembleCouponUrl = new StringBuilder("http://haipaipic.bj.bcebos.com/");
		String couponQRCode = userCoupon.getCouponQRCode();
		int picUrlIndex = couponQRCode.lastIndexOf('=');
		if(picUrlIndex>-1){
			reAssembleCouponUrl.append(couponQRCode.substring(picUrlIndex+1, couponQRCode.length())).append(".jpg");
			userCoupon.setCouponQRCode(reAssembleCouponUrl.toString());
		}
	}
	
	@Override
	public Map<String,String> generateOneUserCouponHBAfterShared(String fromUserName, String toUserName) throws Exception{
		Map<String,String> result = new HashMap<>();
		result.put("result", "failed");
		List<UserCoupon> userCouponList = userCouponRepository.findByFromUserNameAndToUserNameAndCouponType(fromUserName, toUserName, 4);
		if(userCouponList == null || userCouponList.isEmpty()){//红包分享，再奖励一个红包只进行一次
			UserCoupon userCoupon = allocateCouponToUser(fromUserName, toUserName,3,10);
			AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
			AccessToken accessToken = WechatUtil.getAccessToken(appIdSecretConfig.getAppId(), appIdSecretConfig.getAppSecret());
			String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(),
					Long.toString(userCoupon.getCouponId()));//生成的二维码用来追踪分享人，在被分享用户使用该红包是追踪分享人并给予一张红包奖励
			String showUrl = QRCodeUtil.showQRcode(permanentORCode);
			userCoupon.setCouponQRCode(showUrl);
			saveUserCoupon(userCoupon);
			result.put("result", "success");
		}
		return result;
	}
	
	@Override
	public Map<String,String> generateOneUserCouponDKQAfterShared(String fromUserName, String toUserName) throws Exception{
		Map<String,String> result = new HashMap<>();
		result.put("result", "failed");
		List<UserCoupon> userCouponList = userCouponRepository.findByFromUserNameAndToUserNameAndCouponType(fromUserName, toUserName, 5);
		if(userCouponList == null || userCouponList.isEmpty()){//抵扣券分享，抵扣券加倍只奖励一次
			UserCoupon userCoupon = allocateCouponToUser(fromUserName, toUserName,1,10);
			AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
			AccessToken accessToken = WechatUtil.getAccessToken(appIdSecretConfig.getAppId(), appIdSecretConfig.getAppSecret());
			String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(),Long.toString(userCoupon.getCouponId()));//抵扣券不需要追踪分享人，生成的二维码只为关注公众号(sceneStr是123，在扫描时不会触发二维码使用事件)使用
			String showUrl = QRCodeUtil.showQRcode(permanentORCode);
			userCoupon.setCouponQRCode(showUrl);
			userCoupon.setCouponValue(userCoupon.getCouponValue()*2);//分享抵扣券后，加倍奖励
			saveUserCoupon(userCoupon);
			result.put("result", "success");
		}
		return result;
	}
	
	@Override
	public UserCoupon allocateCouponToUser(String fromUserName, String toUserName,Integer couponType,Integer couponValue) throws Exception {
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setCouponValue(couponValue);
		Date createTime = new Date();
		userCoupon.setCreateTime(createTime);
		userCoupon.setExpireTime(WechatUtil.plusDay(30, createTime));
		userCoupon.setFromUserName(fromUserName);
		userCoupon.setToUserName(toUserName);
		userCoupon.setValidateFlag(1);
		userCoupon.setCouponType(couponType);
		return saveUserCoupon(userCoupon);
	}
	
	@Override
	public void generateCouponsForRefereeAfterUseSharedCoupon(String fromUserName, String toUserName,Map<Integer,Map<Integer,Integer>> couponTypeAndNumAndValueMap)  throws Exception{
		AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
		AccessToken accessToken = WechatUtil.getAccessToken(appIdSecretConfig.getAppId(),
				appIdSecretConfig.getAppSecret());
		List<Article> articleList = new ArrayList<>();
		if(couponTypeAndNumAndValueMap!=null && !couponTypeAndNumAndValueMap.isEmpty()){
			Set<Integer> keys = couponTypeAndNumAndValueMap.keySet();
			for(Integer key : keys){
				Map<Integer,Integer> couponNumAndValueMap = couponTypeAndNumAndValueMap.get(key);
				assembleCouponMsg(fromUserName, toUserName, articleList, accessToken, key, couponNumAndValueMap);
			}
		}
	}
	
	@Override
	public NewsMessage generateCouponsAfterSubscribe(String fromUserName, String toUserName, List<Article> articleList,
			Map<Integer,Map<Integer,Integer>> couponMap)  throws Exception{
		NewsMessage newsMessage = new NewsMessage();
		AppIdSecretConfig appIdSecretConfig = appIdSecretConfigService.findByWechatPublicId(toUserName);
		AccessToken accessToken = WechatUtil.getAccessToken(appIdSecretConfig.getAppId(),
				appIdSecretConfig.getAppSecret());
		Article articleSummaryInfo = new Article();
		StringBuilder couponMsg = new StringBuilder("恭喜你获得");
		if(couponMap!=null && !couponMap.isEmpty()){
			Set<Integer> keys = couponMap.keySet();
			for(Integer key : keys){
				Map<Integer,Integer> couponNumberAndValue= couponMap.get(key);
				Set<Integer> couponNumberAndValueKeys = couponNumberAndValue.keySet();
				for(Integer couponNumberAndValueKey : couponNumberAndValueKeys){
					Integer couponNum = couponNumberAndValueKey;
					if(key == 1){
						couponMsg.append("抵扣券").append(couponNum).append("张.");
					}else if(key == 2){
						couponMsg.append("折扣券").append(couponNum).append("张.");
					}else if(key == 3){
						couponMsg.append("红包").append(couponNum).append("张.");
					}
				}
				assembleCouponMsg(fromUserName, toUserName, articleList, accessToken, key, couponNumberAndValue);
			}
		}
		articleSummaryInfo.setTitle(couponMsg.toString());
		articleSummaryInfo.setDescription("优惠券");
		articleSummaryInfo.setPicUrl("");
		articleSummaryInfo.setUrl("");
		articleList.add(articleSummaryInfo);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setFuncFlag(0);
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setArticles(articleList);
		return newsMessage;
	}
	private void assembleCouponMsg(String fromUserName, String toUserName, List<Article> articleList,
			AccessToken accessToken, Integer key, Map<Integer,Integer> couponNumAndValueMap) throws Exception {
		Article article = new Article();
		Set<Integer> couponNumAndValueMapKeys =  couponNumAndValueMap.keySet();
		for(Integer couponNumAndValueMapKey: couponNumAndValueMapKeys){
			Integer couponNum = couponNumAndValueMapKey;
			Integer couponValue = couponNumAndValueMap.get(couponNumAndValueMapKey);
			for (int i = 1; i <= couponNum; i++) {
				UserCoupon userCoupon = allocateCouponToUser(fromUserName, toUserName,key,couponValue);
				String showUrl="";
				String picUrl = "";
				if(key == 3){
					String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(),
							Long.toString(userCoupon.getCouponId()));
					showUrl = QRCodeUtil.showQRcode(permanentORCode);
					/*FTPManager.putObject2BOS("haipaipic", permanentORCode+".jpg", showUrl);*/
					userCoupon.setCouponQRCode(showUrl);
					picUrl ="http://haipai.duapp.com/wechatrest/resources/hb.jpg";
					article.setTitle("红包" + userCoupon.getCouponValue() + "元");
					article.setDescription("红包");
				}else if(key == 2){
					picUrl ="http://haipai.duapp.com/wechatrest/resources/zkq.jpg";
					article.setDescription("折扣券");
					article.setTitle("折扣券");
				}else if(key == 1){
					String permanentORCode = QRCodeUtil.createPermanentORCode(accessToken.getToken(),Long.toString(userCoupon.getCouponId()));
					showUrl = QRCodeUtil.showQRcode(permanentORCode);
					userCoupon.setCouponQRCode(showUrl);
					picUrl ="http://haipai.duapp.com/wechatrest/resources/dyq.jpg";
					article.setDescription("抵扣券");
					article.setTitle("抵扣券");
				}
				saveUserCoupon(userCoupon);
				article.setPicUrl(picUrl);
				article.setUrl(showUrl);
				articleList.add(article);
			}
		}
		
	}

	@Override
	public String useCoupon(UserCoupon userCoupon) throws Exception {
		if (userCoupon.getValidateFlag().intValue() == 0) {
			return "该优惠券已经被使用";
		}

		if (userCoupon.getExpireTime().before(new Date())) {
			return "该优惠券已经过期";
		}
		userCoupon.setValidateFlag(0);
		saveUserCoupon(userCoupon);
		return "success";
	}
	
	
	public static void main(String[] args){
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\wechatrest\\resources\\zkq.jpg");
//		File file = new File("src\\main\\webapp\\wechatrest\\resources\\zkq.jpg");

		System.out.println(file.exists());
	}
}
