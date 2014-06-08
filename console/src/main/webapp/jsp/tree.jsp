<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/jsp/head.inc" %>
<%@ page contentType="text/html; charset=utf-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上海由趣内容管理系统</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery/treeview/jquery.treeview.css" />
<link href="${pageContext.request.contextPath}/css/bill.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery/treeview/jquery.treeview.js" type="text/javascript"></script>
<script>
  $(function() {
      $("#browser").treeview();
      $("a").click(function(){
    	  $("a").css("color","#0000FF");
    	  $(this).css("color","#FF0000");
      });
  });
  
  $(document).ready(function (){
	  $("li").each(function (){
		 $(this).bind("click",aclick);
	  })   
  })
  function aclick(){
	 /* $("li").each(function(){
		 $(this).attr("class","closed");
	 }) */
	  //$(this).attr("class","selected");
		 
  }
</script>
</head>
<body>
<table width="208" border="0" cellpadding="0" cellspacing="0" class="kuangnew3">
  <tr>
    <td class="title-text1"></td>
  </tr>
  <tr>
    <td>
    <table width="191" border="0" cellpadding="0" cellspacing="0">
      <tr><td valign="middle" ><table width="191" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td background="${pageContext.request.contextPath}/img/bj_1.gif" style="height:5px;"><img src="${pageContext.request.contextPath}/img/bj_1.gif" width="1" height="5" /></td>
    </tr>
    <ul id="browser" class="filetree">
    	<li class="closed"><span class="memu-b-2">游戏管理</span>
       		<ul>
       		    <li class="closed"><span class="file"><a title="AppEditor" href="${pageContext.request.contextPath}/web/app" target="mainFrame" class="navdimenu">基本信息</a></span></li>
	       		<li class="closed"><span class="file"><a title="AppVersion" href="${pageContext.request.contextPath}/web/appVersion" target="mainFrame" class="navdimenu">版本控制</a></span></li>
	       		<li class="closed"><span class="file"><a title="ResFile" href="${pageContext.request.contextPath}/web/resFile" target="mainFrame" class="navdimenu">资源文件</a></span></li>
       		  	<li class="closed"><span class="file"><a title="ResUpdate" href="${pageContext.request.contextPath}/web/resUpdate" target="mainFrame" class="navdimenu">资源升级</a></span></li>
	       		<li class="closed"><span class="file"><a title="AppConfig" href="${pageContext.request.contextPath}/web/appConfig" target="mainFrame" class="navdimenu">游戏配置</a></span></li>
	       		<li class="closed"><span class="file"><a title="Sale" href="${pageContext.request.contextPath}/web/sale" target="mainFrame" class="navdimenu">内购管理</a></span></li>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">服务管理</span>
        	<ul>
		       <li class="closed"><span class="file"><a title="Engine" href="${pageContext.request.contextPath}/web/engine" target="mainFrame" class="navdimenu">节点管理</a></span></li>
		       <li class="closed"><span class="file"><a title="Zone" href="${pageContext.request.contextPath}/web/zone" target="mainFrame" class="navdimenu">分区管理</a></span></li>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">游戏发布</span>
        	<ul>
		       <li class="closed"><span class="file"><a title="AppZone" href="${pageContext.request.contextPath}/web/appZone" target="mainFrame" class="navdimenu">总览信息</a></span></li>
		       <li class="closed"><span class="file"><a title="AppZone" href="${pageContext.request.contextPath}/web/appZone/toAdd" target="mainFrame" class="navdimenu">发布信息</a></span></li>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">公告管理</span>
        	<ul>
       		  <li class="closed"><span class="file"><a title="Placard" href="${pageContext.request.contextPath}/web/placard" target="mainFrame" class="navdimenu">基本信息</a></span>
       		  <li class="closed"><span class="file"><a title="PlacardTarget" href="${pageContext.request.contextPath}/web/placardTarget" target="mainFrame" class="navdimenu">公告发布</a></span>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">用户管理</span>
        	<ul>
       		   <li class="closed"><span class="file"><a title="Device" href="${pageContext.request.contextPath}/web/device" target="mainFrame" class="navdimenu">设备信息</a></span>
       		   <li class="closed"><span class="file"><a title="User" href="${pageContext.request.contextPath}/web/user" target="mainFrame" class="navdimenu">用户信息</a></span>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">推广管理</span>
        	<ul>
               <li class="closed"><span class="file"><a title="Promo" href="${pageContext.request.contextPath}/web/promo" target="mainFrame" class="navdimenu">推广配置</a></span>
               <li class="closed"><span class="file"><a title="PromoCount" href="${pageContext.request.contextPath}/web/promoCount" target="mainFrame" class="navdimenu">推广统计</a></span>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">其他设置</span>
        	<ul>
       		   <li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/avatarFile" target="mainFrame" class="navdimenu">用户头像</a></span>
            </ul>
        </li>
        <li class="closed"><span class="memu-b-2">报表管理</span>
        	<ul>
              	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/report/coc" target="mainFrame" class="navdimenu">每日统计</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/report/coc/queryUserByRate" target="mainFrame" class="navdimenu">统计比例</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/report/coc/keep" target="mainFrame" class="navdimenu">留存统计</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/order/order" target="mainFrame" class="navdimenu">充值明细</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/user/forbid" target="mainFrame" class="navdimenu">游戏查询</a></span>
       		   	<li class="closed"><span class="file"><a title="Slots" href="${pageContext.request.contextPath}/web/report/slots/slotsKeep" target="mainFrame" class="navdimenu">留存</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/order/slots" target="mainFrame" class="navdimenu">明细</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/event" target="mainFrame" class="navdimenu">活动管理</a></span>
       		   	<li class="closed"><span class="file"><a title="AvatarFile" href="${pageContext.request.contextPath}/web/product" target="mainFrame" class="navdimenu">商品信息</a></span>
            </ul>
        </li>
    </ul>
    <tr>
      <td valign="middle" class="leftul">&nbsp;</td>
    </tr>
  </table>
        </td>
  </tr>
    </table></td>
  </tr>

</table>
</body>
</html>