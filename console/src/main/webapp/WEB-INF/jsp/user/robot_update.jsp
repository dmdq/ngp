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
				monsterId : {
					required : true,
					range : [1,300],
				},
				robotName : {
					required : true,
				},
				curLev : {
					required : true,
					range : [1,99],
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
	<h2>Create Robot</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="searchDragonData" modelAttribute="searchDragonData"
	action="${pageContext.request.contextPath}/web/user/updateRobot" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<table class="entity-form" align="center">
		<tr>
			<th>怪物ID&nbsp;&nbsp;</th>
			<td colspan="2">
				${jsonMap['ID'] }
				<input type="hidden" name="monsterId" value="${jsonMap['ID'] }"/>
				<input type="hidden" value="${id}" name="u_id" id="u_id"/>
			</td>
		</tr>
		<tr>
			<th>怪物Lev&nbsp;</th>
			<td colspan="2">
				<input name="curLev" size="50" maxlength="50" value="${jsonMap['curLev']}"/>
			</td>
		</tr>
		<tr>
			<th>机器人Lev&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="level" value="${jsonMap['rolelev']}" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>机器人Name&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="robotName" value="${jsonMap['rolename']}" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>机器人Type&nbsp;</th>
			<td colspan="2">
				<select name="robotType" style="width: 270px;">
					<option <c:if test="${jsonMap['roletype'] == 0}">selected</c:if> value="0">0</option>
					<option <c:if test="${jsonMap['roletype'] == 1}">selected</c:if> value="1">1</option>
					<option <c:if test="${jsonMap['roletype'] == 2}">selected</c:if> value="2">2</option>
				</select>
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/user/robot" method="post">
</form>
</body>
</html>