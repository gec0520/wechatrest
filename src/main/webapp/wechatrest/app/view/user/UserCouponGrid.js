Ext.define('WeChatRest.view.user.UserCouponGrid',{
	extend : 'Ext.grid.Panel',
	requires : ['WeChatRest.view.user.UserCouponController'],
	xtype : 'usercoupongrid',
	controller : 'usercouponcontroller',
	title : '用户红包列表',
	store : 'UserCouponStore',
	border : true,
	height : document.documentElement.clientHeight,
	width : '100%',
	loadMask: true,
	columns : [
   		{
   			text : '红包Id',
   			dataIndex : 'couponId',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		},
   		{
   			text : '红包金额',
   			dataIndex : 'couponValue',
   			flex : 1,
   			align : 'center',
   			hidden : false
   		},
   		{
   			text : '用户名称',
   			dataIndex : 'fromUserName',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		},
   		{
   			text : '公众号',
   			dataIndex : 'toUserName',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		},
   		{
   			xtype : 'gridcolumn',
   			width : 280,
   			text : '红包二维码',
   			dataIndex : 'couponQRCode',
   			flex : 1,
   			align : 'center',
   			hidden : false,
   			renderer : function(value) {
   				return '<input id="show" type=image style="width : 50px; height : 50px;" src="/wechatrest/resources/QRCode.jpg" title="show"/>';
   			}
   		},
   		{
   			text : '红包是否有效',
   			dataIndex : 'couponValidate',
   			flex : 1,
   			align : 'center',
   			hidden : false
   		}, {
   			text : '红包创建时间',
   			dataIndex : 'couponCreateTime',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		}, {
   			text : '红包失效时间',
   			dataIndex : 'couponExpireTime',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		}],
   		listeners : {
	   		itemclick : {
	   			fn : 'userClick',
	   			scope : "controller"
	   		},
	   		afterRender:{
	   			fn : 'reloadStore',
	   			scope : "controller"
	   		}
	   	}
});