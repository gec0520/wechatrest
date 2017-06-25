Ext.define('WeChatRest.view.user.UserCouponGrid',{
	extend : 'Ext.grid.Panel',
	requires : ['WeChatRest.view.user.UserCouponController'],
	xtype : 'usercoupongrid',
	controller : 'usercouponcontroller',
	title : '用户红包列表',
	store : 'UserCouponStore',
	//border : true,
	height : document.documentElement.clientHeight,
	width : '100%',
	autoWidth:true,
	loadMask: true,
	renderTo:Ext.get("orgGrid"),
	columns : [
   		{
   			text : '红包Id',
   			dataIndex : 'couponId',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		},
   		{
   			text : '优惠券金额',
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
   			text : '优惠券',
   			dataIndex : 'couponType',
   			flex : 1,
   			align : 'center',
   			hidden : false,
   			renderer : function(value) {
   				if(value==1){
   					return '<input id="show" type=image style="width : 50px; height : 50px;" src="/wechatrest/resources/dyq.jpg" title="show"/>';
   				}else if(value ==2){
   					return '<input id="show" type=image style="width : 50px; height : 50px;" src="/wechatrest/resources/zkq.jpg" title="show"/>';
   				}else if(value ==3){
   					return '<input id="show" type=image style="width : 50px; height : 50px;" src="/wechatrest/resources/hb.jpg" title="show"/>';
   				}
   			}
   		},
   		{
   			text : '是否有效',
   			dataIndex : 'couponValidate',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		}, 
   		{
   			text : '创建时间',
   			dataIndex : 'couponCreateTime',
   			flex : 1,
   			align : 'center',
   			hidden : true
   		}, 
   		{
   			text : '截止有效期',
   			dataIndex : 'couponExpireTime',
   			flex : 1,
   			align : 'center',
   			hidden : false
   		}],
   		listeners : {
	   		itemclick : {
	   			fn : 'userClick',
	   			scope : "controller"
	   		},
	   		afterrender:{
	   			fn : 'reloadStore',
	   			scope : "controller"
	   		}
	   	}
});