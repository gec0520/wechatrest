<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD//XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
String qrCodeUrl = request.getParameter("qrCodeUrl");
%>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="wechatrest/css/outer.css" type="text/css">
    
      <style type="text/css">
        .loginBtn{
            display:block;
            cursor: pointer;
            height: 32px;
            margin-bottom: 1px;
            width: 100px;
        }
    </style>
</head>
<body>
<div class="rich_media">
  <div class=rich_media_area_primary>	
	<div class="rich_media_inner">
	<div class="rich_media_area_primary">
		<div>
			<h2 class="rich_media_title">品牌故事</h2>
			 <div class = "rich_media_area_primary">
			    <div class = "rich_media_content">
					<img style="vertical-align: middle; box-sizing: border-box; width: auto !important; height: auto !important; visibility: visible !important;" class=" " data-src="/wechatrest/resources/xs.jpg" data-ratio="0.6851520572450805" data-w="559" data-type="jpeg" src="/wechatrest/resources/xs.jpg" data-fail="0">
			    </div>
			 </div>
			 <div style="box-sizing: border-box;">
				 <div style="text-align:center;font-size:16px;line-height:1.4;box-sizing: border-box;">
				 	<p style="box-sizing: border-box;">说起&nbsp
				 		<span style="color:rgb(255,104,39);">虾</span>
				 	</p>
				 	<p style="box-sizing: border-box;">每个人都熟悉</p>
				 	<p style="box-sizing: border-box;">在美国</p>
				 	<p style="box-sizing: border-box;">东有
				 		<span style="color:rgb(255,104,39);">波士顿龙虾</span>
				 	</p>
				 	<p style="box-sizing: border-box;">西有路易斯安娜
				 		<span style="color:rgb(255,104,39);">小龙虾</span>
				 	</p>
				 	<p style="box-sizing: border-box;">我叫马修</p>
				 	<p style="box-sizing: border-box;">来佛罗里达坦帕</p>
				 	<p style="box-sizing: border-box;">来中国寻找</p>
				 	<p style="box-sizing: border-box;">世界上最好吃的虾</p>
				 	<p style="box-sizing: border-box;">
				 		<br style="box-sizing: border-box;">
				 	</p>
				 	<p style="box-sizing: border-box;">我花了两年的时间</p>
				 	<p style="box-sizing: border-box;">找到了
				 		<span style="color:rgb(255,104,39);">四位做虾大师</span>
				 	</p>
				 	<p style="box-sizing: border-box;">他们来自四大名虾产地</p>
				 	<p style="box-sizing: border-box;">传承了正宗的烹虾技艺</p>
				 	<p style="box-sizing: border-box;">
				 		<br style="box-sizing: border-box;">
				 	</p>
				 </div>
			 </div>
			 

			  <div style="box-sizing: border-box;">
			  	 <div style="margin-top: 0.5em;margin-bottom: 0.5em;box-sizing: border-box;">
			  	 	<div style="background-color:rgb(234,27,27);height:2px;box-sizing: border-box;"></div>
			  	 </div>
			  </div>
			  <div style="box-sizing: border-box;">
			  <div style="box-sizing: border-box;">
			  	<div style="text-align:center;font-size:18px;box-sizing: border-box;">
			  		<p style="box-sizing: border-box;">
				 		<br style="box-sizing: border-box;">
				 	</p>
				 	<p style="box-sizing: border-box;">
				 		<span style="font-size:21px;box-sizing: border-box;">四位烹虾大师</span>
				 	</p>
				 	<p style="box-sizing: border-box;">
				 		<span style="font-size:21px;box-sizing: border-box;">敬你一盘好虾</span>
				 	</p>
			  	</div>
			  </div>
			  <div class = "rich_media_area_primary">
			    <div class = "rich_media_content">
					<img style="vertical-align: middle; box-sizing: border-box; width: auto !important; height: auto !important; visibility: visible !important;" class=" " data-src="/wechatrest/resources/pxds.png" data-ratio="0.6851520572450805" data-w="559" data-type="png" src="/wechatrest/resources/pxds.png" data-fail="0">
			    </div>
			 </div>
		</div>
	</div>
	 <div class = "qr_code_pc_outer" style="display:block;">
	 	<div class = "qr_code_pc_inner">
	     <div class = "qr_code_pc">
	    	<img class = "qr_code_pc_img" src=<%=qrCodeUrl%> >
	    	<p>
	    	"微信扫一扫"
	    	<br>
	    	"关注该公众号"
	    	</p>
	    </div>
	   </div>
	  </div>
	</div>
  </div>
</div>
</body>
</html>