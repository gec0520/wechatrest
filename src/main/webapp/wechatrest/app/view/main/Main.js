Ext.define('WeChatRest.view.main.Main', {
	extend : 'Ext.container.Container',

	xtype : 'app-main',

	requires : [ 'WeChatRest.view.user.UserCouponGrid' ],
	layout : {
		type : 'center'
	},

	items : {
		xtype : 'usercoupongrid'
	}
});
