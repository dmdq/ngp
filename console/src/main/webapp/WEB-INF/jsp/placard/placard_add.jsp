<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-4-25
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
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/jquery-ui-1.10.3.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/pagination/pagination.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/img_list.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-timepicker-addon.css'/>" />

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
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.10.3.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-ui-sliderAccess.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-ui-timepicker-addon.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/i18n/jquery-ui-timepicker-zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/messages_cn.js'/>"></script>
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
		$("#addBtn").button();
		$("#beginTime").datetimepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:'HH:mm:ss',
			stepHour: 1,
			stepMinute: 10,
			stepSecond: 10});
		$("#overTime").datetimepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:'HH:mm:ss',
			stepHour: 1,
			stepMinute: 10,
			stepSecond: 10});
		
		var validator = $("#form1").validate({
			meta : "validate",
			rules : {
				id : {
					required : true,
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
	
	function systeminit(){}
	
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
	<h2>Create Placard</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="placard" modelAttribute="placard"
	action="${pageContext.request.contextPath}/web/placard/add" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="${p_id}" name="p_id" id="p_id"/>
	<input type="hidden" value="${p_status}" name="p_status" id="p_status"/>
	<table class="entity-form" align="center">
		<tr>
			<th>标题&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:input path="title" cssClass="required" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>标题字体&nbsp;</th>
			<td colspan="2">
				<form:input path="titleSize" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>标题颜色&nbsp;&nbsp;</th>
			<td colspan="2">
				<form:input path="titleColor" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>内容&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:textarea path="body" cssClass="required" cols="50" rows="10"/>
			</td>
		</tr>
		<tr>
			<th>内容字体&nbsp;</th>
			<td colspan="2">
				<form:input path="bodySize" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>内容颜色&nbsp;</th>
			<td colspan="2">
				<form:input path="bodyColor" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>状态&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:select path="status" cssClass="required">
					<form:option value="">-----请选择------</form:option>
					<form:option value="0">有效</form:option>
					<form:option value="-1">无效</form:option>
				</form:select>
			</td>
		</tr>
		<tr>
			<th>开始时间&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="beginTime" id="beginTime" class="required" readonly="readonly" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>结束时间&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<input name="overTime" id="overTime" class="required" readonly="readonly" size="50" maxlength="50"/>
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/placard" method="post">
</form>
</body>
</html>