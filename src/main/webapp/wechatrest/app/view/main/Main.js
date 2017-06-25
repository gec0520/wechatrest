Ext.define('WeChatRest.view.main.Main', {
	extend : 'Ext.panel.Panel',
	xtype : 'app-main',
	requires : [ 'WeChatRest.view.user.UserCouponGrid',
	             'WeChatRest.view.main.MainController',
	             'WeChatRest.view.main.MainViewModel'],
	controller:'maincontroller',
	viewModel:'mainviewmodel',
	layout:'center',
	plugins:'viewport',
	height: window.innerHeight,
    width: window.innerWidth,
	initComponent:function(){
		this.items=[{
			xtype : 'usercoupongrid'
		}];
		this.callParent(arguments);
	}
	/*listeners:{
		afterrender:{
   			fn : 'addGridAfterRender',
   			scope : "controller"
   		}
	}*/
	
});
