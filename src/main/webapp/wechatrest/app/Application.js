Ext.define('WeChatRest.Application', {
	extend : 'Ext.app.Application',
	name : 'WeChatRest',
	stores : [ 'UserCouponStore' ],
	launch : function() {
		Ext.Ajax.request({
			timeout : 3000000,
			url : '/getSignature',
			params : 'url=http://haipai.duapp.com/wechatrest/userCoupon.html',
			success : function(resp,opts) {
				
				var timestamp = Ext.util.JSON.decode(resp.responseText).timestamp;//时间戳
				var nonceStr = Ext.util.JSON.decode(resp.responseText).nonceStr;//随机串
				var signature = Ext.util.JSON.decode(resp.responseText).signature;//签名
				var appId = Ext.util.JSON.decode(resp.responseText).appId;
				
				 wx.config({
					  debug: false,
				      appId:appId,
				      timestamp: timestamp,
				      nonceStr: nonceStr,
				      signature: signature,
				      jsApiList: [
//				        'checkJsApi',
//				        'onMenuShareTimeline',
				        'onMenuShareAppMessage'
//				        'onMenuShareQQ'
				      ]
				  });
				 /*wx.ready(function () {
					 wx.hideMenuItems({
				      menuList: [
				        'menuItem:share:timeline', // 分享到朋友圈
				      ]				
				    });
				 });*/
			}
		});
	}
});