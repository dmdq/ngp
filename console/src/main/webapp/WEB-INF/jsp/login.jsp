<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/jsp/head.inc"%>
<%@ page contentType="text/html; charset=utf-8"%>
<title>上海由趣内容管理系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css" />
<script src="${pageContext.request.contextPath}/js/jquery.js"  type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/util.js"    type="text/javascript"></script>
<script type="text/javascript">
    if (self == top) {
      var bodyMain = document.getElementsByTagName('body')[0];
      bodyMain.style.display = "block";
    } else {
      top.location = self.location;
    }
</script>
</head>
<body class="login" >
	<div class="login_panel" onkeypress="javascript:event.keyCode == 13 ? document.forms[0].submit() : null;">
		<form  action="${pageContext.request.contextPath}/web/system/login" method="post">
		<div class="input">
			<table>
			<tr><td colspan="2">
				<c:if test="${error eq 'yes'}">
				  <font color="red"> 用户名或者密码错误。</font>
				</c:if>
			</td></tr>
			<tr><td>用户名：</td>
				<td><input type="text" id="username" name="username"  class="required" style="width:130px;" >
				</td></tr>
				<tr><td>密码：</td>
				<td><input type="password" id="password" name="password"  class="required" style="width:130px;" >
				<font color="red"><c:out value="${message}" /></font></td></tr>
			</table>
			
		</div>
		<div class="btns">
		    
			<input type="submit" class="btn" value="登 录"  onclick="document.forms[0].submit();"/>
			<input type="reset" class="btn" value="重 置" />
		</div>
		</form>
	
</div>
</body>
</html>
