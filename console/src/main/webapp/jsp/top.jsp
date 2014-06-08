<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="${pageContext.request.contextPath}/css/bill.css" rel="stylesheet" type="text/css">
</head>
<script type="text/javascript">
	function logout() {
		document.form1.submit();
	}
</script>
<body>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="60" background="${pageContext.request.contextPath}/img/h-bg.gif">
    <img src="${pageContext.request.contextPath}/img/banner.jpg"  height="60"></td>
  </tr>
  <tr>
    <td height="5" class="memu-bg33">
    <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
       <td><a href="javascript:logout();">点此退出</a></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<form name="form1" action="${pageContext.request.contextPath}/web/system/logout" target="_top" method="post"></form>
</body>
</html>
