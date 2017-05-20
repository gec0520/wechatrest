Ext.define('WeChatRest.model.UserCouponModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'couponId',
		type : 'int'
	}, {
		name : 'couponValue',
		type : 'string'
	}, {
		name : 'fromUserName',
		type : 'string'
	}, {
		name : 'toUserName',
		type : 'string'
	},{
		name : 'couponQRCode',
		type : 'string'
	}, {
		name : 'couponValidate',
		type : 'string'
	}, {
		name : 'couponCreateTime',
		type : 'string'
	}, {
		name : 'couponExpireTime',
		type : 'string'
	}]
});