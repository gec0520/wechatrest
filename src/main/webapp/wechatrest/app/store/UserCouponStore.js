Ext.define('WeChatRest.store.UserCouponStore', {
	extend : 'Ext.data.Store',
	model : 'WeChatRest.model.UserCouponModel',
	autoLoad : true,
	proxy : {
		type : "ajax",
		url : "/userCoupon",
		reader : {
			type : "json",
			rootProperty : "result"
		},
		extraParams: {
			fromUserName:'oi88H1Y8n5sj5b3yrJeVtVBAB6uQ'
		}
	},
	pageSize : 50,
});
