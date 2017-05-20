Ext.define('WeChatRest.view.user.UserCouponController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.usercouponcontroller',
	userClick : function(view, record, item, index, e, eOpts) {
		var win_Watch = Ext.create('Ext.Window', {
			width : 250,
			height : 200,
			minHeight : 100,
			minWidth : 100,
			maximizable : true,
			title : '二维码大图',
			layout : "fit", // 窗口布局类型
			modal : true, // 是否模态窗口，默认为false
			resizable : false,
			closeAction : 'hide',
			plain : true,
			draggable : true,
			border : false,
			id:'usercouponwindow',
			items : [ Ext.create('Ext.Img', {
				height : 220,
				width : 200,
				src : record.data.couponQRCode
			})],
		    buttons: [
		        {
		        	xtype: "button", text: "分享", handler: function () {
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
		        	}
		        },
		        { 
		        	xtype: "button", text: "取消", handler: function () { 
		        		this.up("window").close(); 
		        	} 
		        }
		        
		    ],
		    listeners:{
		    	"show":function(){ 
		    		wx.onMenuShareAppMessage({
      			      title: '分享优惠券',
      			      desc: '优惠券',
      			      link: 'http://haipai.duapp.com/index.html',
      			      imgUrl: record.data.couponQRCode,
      			      type: 'link', // 分享类型,music、video或link，不填默认为link
      			      dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
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
      			    });
	    		}
		    }
		});
		win_Watch.show();
	},
	reloadStore:function(view, record, item, index, e, eOpts){
		Ext.ComponentQuery.query('usercoupongrid')[0].getStore().reload();
	}
});