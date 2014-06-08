<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误页面</title>
<script type="text/javascript" src="http://175.102.128.125/images/jquery-1.9.1.min.js"></script>
<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	left:0;
	top:470px;
	width:770px;
	height:25px;
	z-index:1;
}
body {
	background-color: #EFEFEF;
}
.a1:HOVER {
	cursor: pointer;
}

.a1:HOVER {
	cursor: pointer;
}
.a1{
font-size: 16px;
color: red;
}
-->
</style>

 <script type="text/javascript">
var parentUrl = self.parent.location + "";
var index = parentUrl.lastIndexOf("/");
parentUrl = parentUrl.substring(0, index);
 $(function(){
		var tis = 2;
		var url = $("#ssoUrl").val();
		var stm = window.setInterval(function(){
			$("#mes").html(tis);
			tis--;
			if(tis < 0){
				window.parent.location.href=parentUrl;
				window.clearInterval(stm);
			}
		},1000);	
	});	
	
	function tiaozhuan(){
		window.parent.location.href=parentUrl;
	}
  </script>

</head>
<body>
	<br>
	<br>
	<table width="80%" border="0" align="center" cellpadding="5" cellspacing="1">
	  <thead>
	  <tr>
	  <%-- <c:set var="sooUrl" value=""></c:set> --%>
	    <td height="25" align="center"><h4><font lang="宋体"><strong>
	    会话已超时，正在跳转<span id="mes">3</span>......<a class="a1" onclick="tiaozhuan()">重新登录</a>
	    </strong></font></h4></td>
	  </tr>
	  </thead>
</table>
<br>
</body>
</html>