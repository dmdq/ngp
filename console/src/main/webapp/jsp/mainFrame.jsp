<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上海由趣内容管理系统</title>
<script type="text/javascript">
<!--
	
//-->
</script>
</head>

<frameset rows="80,*" cols="*" frameborder="no" border="0" framespacing="0">
	<frame src="<c:url value='/jsp/top.jsp' />" name="topFrame" scrolling="no" noresize="noresize"/>
	<frameset cols="218,*" frameborder="no" border="0" framespacing="0">
		<frame src="<c:url value='/jsp/tree.jsp' />" name="menuFrame" scrolling="yes" noresize="noresize" />
		<frameset rows="*,20" cols="*" frameborder="no" border="0" framespacing="0">
			<frame src="<c:url value='/jsp/welcome.jsp' />" name="mainFrame"  noresize="noresize" scrolling="auto" />
			<frame src="<c:url value='/jsp/bottom.html' />" name="bottomFrame" noresize="noresize" scrolling="no" />
		</frameset>
	</frameset>
	<noframes>
		<body>
			<p align="center">
				<font lang="宋体" size="20" color="red">
					您的浏览器不支持Frame, 因此无法正常显示。
				</font>
			</p>
		</body>
	</noframes>
</frameset>

</html>