<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei              创建日期 : 2013-5-14
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/head.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<title>内容管理系统</title>

<!-- CSS AREA -->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/theme/style.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/theme/jquery-ui-1.8.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/pagination/pagination.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/img_list.css'/>" />

<style type="text/css">
<!--
  label.error {
    color: red;
    font-weight: bold;
    padding-left: 20px;
  }
-->
</style>

<!-- JAVASCRIPT AREA -->
<c:url value='' />
<script type="text/javascript" src="<c:url value='/js/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/messages_cn.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.bgiframe-2.1.1.js'/>"></script>

<script type="text/javascript">
	/**
	 * description : 页面初始回调函数
	 *
	 * author : system
	 * create on : 2010-6-17
	 * last modifier : 
	 */
	$(function() {
		//system
		systeminit();
		$("#save").button();
		$("#back").button();
		$("#addEngineBtn").button();
		
		var validator = $("#form1").validate({
			meta : "validate",
			rules : {
				level : {
					required : true,
					min : 1,
				},
				nick : {
					required : true,
				},
				level : {
					required : true,
					min : 1,
				},
				gem : {
					required : true,
					min : 1,
				},
				trophy : {
					required : true,
					min : 1,
				},
			},
		});
		
		// 点击“保存”按钮时先验证，验证通过后方能保存
		$("#save").click(function() {
			if (validator.form()) { //若验证通过，则调用保存/修改方法
				submit();
			}
		});

		//customer
	});
	
	function submit() {
		document.forms[0].submit();
	}

	function back() {
        $("#searchAndPageForm").submit();
	}

//-->
</script>

</head>
<body>
	<h2>Update GameData</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="searchUserData" modelAttribute="searchUserData"
	action="${pageContext.request.contextPath}/web/user/updateGameData" method="post">
	<table class="entity-form" align="center">
		<tr>
			<th>编号&nbsp;&nbsp;</th>
			<td colspan="2">
				${userData.userId}
				<input type="hidden" name="userId" value="${userData.userId}"/>
			</td>
		</tr>
		<tr>
			<th>昵称&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="nick" size="50" maxlength="50" value="${userData.nick}"/>
			</td>
		</tr>
		<tr>
			<th>等级&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="level" size="50" maxlength="50" value="${userData.level}"/>
			</td>
		</tr>
		<tr>
			<th>宝石&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="gem" value="${jsonMap['Gem']}" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>奖杯&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="trophy" value="${userData.trophy}" size="50" maxlength="50"/>
			</td>
		</tr>
	</table>
</form:form>
</div>
<br>

<div align="center">
	<!-- 按钮区域 -->
	<div class="btn-container">
		<button id="save">保存</button>
		<button id="back" onclick="javascript:back();">返回</button>
	</div>
</div>
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/user/toAddGameData" method="post">
</form>
</body>
</html>