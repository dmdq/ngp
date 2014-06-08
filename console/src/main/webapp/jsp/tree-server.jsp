<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<c:url value='/css/jquery/treeview/jquery.treeview.css'></c:url>" />
<link rel="stylesheet" href="<c:url value='/css/jquery/treeview/screen.css'></c:url>" />

<script src="<c:url value='/js/jquery/jquery-1.4.2.js'></c:url>" type="text/javascript"></script>
<script src="<c:url value='/js/jquery/external/jquery.cookie.js'></c:url>" type="text/javascript"></script>
<script src="<c:url value='/js/jquery/treeview/jquery.treeview.js'></c:url>" type="text/javascript"></script>
<script src="<c:url value='/js/jquery/treeview/jquery.treeview.async.js'></c:url>" type="text/javascript"></script>

<script type="text/javascript">
	/**
	 * 页面初始化
	 * autor : wangnan
	 * create on : 2010-03-26
	 */
	$(function() {
		loadMenu($("#roleId").val());
		$("#roleId").change(function() {
			window.parent.frames['mainFrame'].frameElement.src = "jsp/welcome.jsp";
			var roleId = $(this).val();
			loadMenu(roleId);
		});
		
	})
	
	/**
	 * 加载菜单
	 * autor : wangnan
	 * create on : 2010-03-26
	 */
	function loadMenu(roleId) {
		$("#browser").empty();
		$("#browser").treeview({
			collapsed: true,
			control:"#sidetreecontrol",
			animated: "fast",
			persist: "location",
			url: "<c:url value='/web/system/menu'></c:url>" + "/" + roleId
		});
	}

</script>
</head>
	
<body>
	<div id="main">
		<div id="sidetreecontrol" align="left"><font size="2"> <a href="?#">全部收缩</a> | <a href="?#">全部展开</a> </font></div>
		<br>
		<img alt="" src="<c:url value='/images/system.gif'></c:url>">&nbsp;
		<select id="roleId" name="roleId">
			<c:forEach items="${sessionScope.user.roles}" var="role">
				<c:choose>
					<c:when test="${role.value.roleId == sessionScope.user.defaultRoleId}">
					<option value="${role.value.roleId }" selected="selected">${role.value.roleName }</option>
					</c:when>
					<c:otherwise>
					<option value="${role.value.roleId }">${role.value.roleName }</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<ul id="browser" class="filetree">
		</ul>
	</div>
</body>
</html>