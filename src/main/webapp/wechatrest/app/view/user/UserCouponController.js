Ext.define('WeChatRest.view.user.UserCouponController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.usercouponcontroller',
	userClick : function(view, record, item, index, e, eOpts) {
		if(record.data.couponType == 3){//红包
			var win_Watch_hb = Ext.create('Ext.Window', {
				width : 250,
				height : 250,
				minHeight : 100,
				minWidth : 100,
				maximizable : true,
				title : '红包二维码',
				layout : "fit", // 窗口布局类型
				modal : true, // 是否模态窗口，默认为false
				resizable : false,
				closeAction : 'destroy',
				plain : true,
				draggable : true,
				border : false,
				id:'usercouponhbwindow',
				items : [ Ext.create('Ext.Img', {
					height : 220,
					width : 200,
					src : record.data.couponQRCode
				})],
			    buttons: [
			       // {
			        	//xtype: "button", text: "分享", handler: function () {
			        		//wx.ready(function () {
			        		/* wx.onMenuShareTimeline({
			        		      title: '互联网之子',
			        		      link: 'http://movie.douban.com/subject/25785114/',
			        		      imgUrl: 'http://img3.douban.com/view/movie_poster_cover/spst/public/p2166127561.jpg',
			        		      trigger: function (res) {
			        		        alert('用户点击分享到朋友圈');
			        		      },
			        		      success: function (res) {
			        		        alert('已分享');
			        		      },
			        		      cancel: function (res) {
			        		        alert('已取消');
			        		      },
			        		      fail: function (res) {
			        		        alert(JSON.stringify(res));
			        		      }
			        		    });*/
			        		
			        	    /*wx.onMenuShareAppMessage({
			        	        title: '互联网之子',
			        	        desc: '在长大的过程中，我才慢慢发现，我身边的所有事，别人跟我说的所有事，那些所谓本来如此，注定如此的事，它们其实没有非得如此，事情是可以改变的。更重要的是，有些事既然错了，那就该做出改变。',
			        	        link: 'http://movie.douban.com/subject/25785114/',
			        	        imgUrl: 'https://ws2.sinaimg.cn/large/5e9ed7c7ly1fdimnlne5hj20nc0cfdh3.jpg',
			        	        trigger: function (res) {
			        	          alert('用户点击发送给朋友');
			        	        },
			        	        success: function (res) {
			        	          alert('已分享');
			        	        },
			        	        cancel: function (res) {
			        	          alert('已取消');
			        	        },
			        	        fail: function (res) {
			        	          alert(JSON.stringify(res));
			        	        }
			        	      });*/
		        			    //alert('已注册获取“发送给朋友”状态事件');
			        		//});
			        	//}
			      //  },
			        { 
			        	xtype: "button", text: "取消", handler: function () { 
			        		win_Watch_hb.close(); 
			        	} 
			        }
			        
			    ],
			    listeners:{
			    	"show":function(){ 
			    		wx.onMenuShareAppMessage({
	      			      title: '分享红包',
	      			      desc: '点击获取红包二维码',
	      			      link: 'http://haipai.duapp.com/main.jsp?qrCodeUrl='+record.data.couponQRCode,
	      			      imgUrl: record.data.couponQRCode,
	      			      type: 'link', // 分享类型,music、video或link，不填默认为link
	      			      dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	      			      success: function (res) {
	      			    	Ext.Ajax.request({
	      						timeout : 3000000,
	      						url : '/generateOneUserCouponAfterShared',
	      						params : 'sharedQRCodeId='+record.data.couponId,
	      						success : function(resp,opts) {
	      							var json=JSON.parse(resp.responseText);  
	      							if(json.result == "success"){
	      								alert('在您分享的朋友关注该公众号后,您将再获赠一个红包奖励');
	      							}else{
	      								alert('您已经分享过优惠券，不予优惠券奖励');
	      							}
	      							location.reload();
	      						}
	      			    	});
	      			      },
	      			      fail: function (res) {
	      			    	location.reload();
	      			      }
	      			    });
		    		}
			    }
			});
			win_Watch_hb.show();
		}
		if(record.data.couponType == 1){//抵用券分享金额翻倍
			var win_Watch_dyq = Ext.create('Ext.Window', {
				width : 250,
				height : 250,
				minHeight : 100,
				minWidth : 100,
				maximizable : true,
				title : '抵扣券二维码',
				layout : "fit", // 窗口布局类型
				modal : true, // 是否模态窗口，默认为false
				resizable : false,
				closeAction : 'destroy',
				plain : true,
				draggable : true,
				border : false,
				id:'usercoupondyqwindow',
				items : [ Ext.create('Ext.Img', {
					height : 220,
					width : 200,
					src : record.data.couponQRCode,
				})],
			    buttons: [
			        {
			        	xtype: "button", text: "取消", handler: function () {
			        		win_Watch_dyq.close(); 
			        	}
			        }
			        
			    ],
			    listeners:{
			    	"show":function(){ 
			    		wx.onMenuShareAppMessage({
	      			      title: '分享抵扣券',
	      			      desc: '点击获取抵扣券二维码',
	      			      link: 'http://haipai.duapp.com/main.jsp?qrCodeUrl='+record.data.couponQRCode,
	      			      imgUrl: record.data.couponQRCode,
	      			      type: 'link', // 分享类型,music、video或link，不填默认为link
	      			      dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	      			      success: function (res) {
	      			    	Ext.Ajax.request({
	      						timeout : 3000000,
	      						url : '/doubleCouponValueAfterShared',
	      						params : 'sharedQRCodeId='+record.data.couponId,
	      						success : function(resp,opts) {
	      							var json=JSON.parse(resp.responseText); 
	      							if(json.result == "success"){
	      								alert('恭喜,您分享的抵扣券已经加倍');
	      							}else{
	      								alert('该抵扣券已经被分享');
	      							}
	      							location.reload();
	      						}
	      			    	});
	      			      },
	      			      fail: function (res) {
	      			    	location.reload();
	      			      }
	      			    });
		    		}
			    }
			});
			win_Watch_dyq.show();
		}
	},
	reloadStore:function(view, record, item, index, e, eOpts){
		var task = {
			run : function() {
				if (getCookie("reloaded") == null) {
					alert("若您分享一张优惠券，将获赠相应的奖励");
					location.reload();
					setCookie("reloaded", "true", 10000);
				}else{
					Ext.TaskManager.stop(task);
				}
			},
			interval : 1000
		};
		Ext.TaskManager.start(task);
	}
});