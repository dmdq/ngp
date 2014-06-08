<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-5-28
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
		$("#selectApp").button();
		$("#addZoneBtn").button();
		
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
	
	function submit() {
		document.forms[0].submit();
	}

	function back() {
		$("#searchAndPageForm").submit();
	}
	
	$(document).ready(function(){
    	var cs = "<c:out value='${event.curStatus}'/>";
    	var cu = "<c:out value='${event.curUnit}'/>";
    	var ct = "<c:out value='${event.curType}'/>";
    	editCurType(ct);
    	editCurStatus(cs);
    	editCurUnit(cu);
	});
	
	function editCurStatus(cs) {
		$("#curStatus").val(cs);
		if (cs[0] == 1) {
			$("#cs").attr("disabled","disabled");
			$("#cu").attr("disabled","disabled");
			$("#ct").attr("disabled","disabled");
		}
	}
	
	function editCurUnit(cu) {
		if (cu == "1day") {
			cu = 1;
		} else if (cu == "2day") {
			cu = 2;
		} else if (cu == "3day") {
			cu = 3;
		} else if (cu == "4day") {
			cu = 4;
		} else if (cu == "5day") {
			cu = 5;
		} else if (cu == "6day") {
			cu = 6;
		} else if (cu == "week") {
			cu = "week";
		}
		$("#curUnit").val(cu);
	}
	
	function editCurType (ct) {
		$("#curType").val(ct);
	}
//-->
</script>

</head>
<body>
	<h2>Update Event</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="event" modelAttribute="event" 
	action="${pageContext.request.contextPath}/web/event/update" method="post">
	<table class="entity-form" align="center">
		<tr>
			<th>Id&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" name="id" value="${event.id}" />
				<c:out value="${event.id}"/>
			</td>
		</tr>
		<tr>
			<th>本周活动周期&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" id="curUnit" name="curUnit" />
				<select id="cu" name="cu" style="width: 280px;" onchange="editCurUnit(this.value);">
					<option <c:if test="${event.curUnit == '1day'}">selected</c:if> value="1">1天</option>
					<option <c:if test="${event.curUnit == '2day'}">selected</c:if> value="2">2天</option>
					<option <c:if test="${event.curUnit == '3day'}">selected</c:if> value="3">3天</option>
					<option <c:if test="${event.curUnit == '4day'}">selected</c:if> value="4">4天</option>
					<option <c:if test="${event.curUnit == '5day'}">selected</c:if> value="5">5天</option>
					<option <c:if test="${event.curUnit == '6day'}">selected</c:if> value="6">6天</option>
					<option <c:if test="${event.curUnit == 'week'}">selected</c:if> value="week">1周</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>本周活动状态&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" id="curStatus" name="curStatus" />
				<select id="cs" name="cs" onchange="editCurStatus(this.value)" style="width: 280px;">
					<option <c:if test="${event.curStatus == 1}">selected</c:if> value="1">开启</option>
					<option <c:if test="${event.curStatus == 0}">selected</c:if> value="0">关闭</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>本周排行类型&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" id="curType" name="curType" />
				<select name="ct" id="ct" onchange="editCurType(this.value)" style="width: 280px;">
					<option <c:if test="${event.curType == 0}">selected</c:if> value="0">积分</option>
					<option <c:if test="${event.curType == 1}">selected</c:if> value="1">收集时间</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>下周活动周期&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<select name="nextUnit" style="width: 280px;">
					<option <c:if test="${event.nextUnit == '1day'}">selected</c:if> value="1">1天</option>
					<option <c:if test="${event.nextUnit == '2day'}">selected</c:if> value="2">2天</option>
					<option <c:if test="${event.nextUnit == '3day'}">selected</c:if> value="3">3天</option>
					<option <c:if test="${event.nextUnit == '4day'}">selected</c:if> value="4">4天</option>
					<option <c:if test="${event.nextUnit == '5day'}">selected</c:if> value="5">5天</option>
					<option <c:if test="${event.nextUnit == '6day'}">selected</c:if> value="6">6天</option>
					<option <c:if test="${event.nextUnit == 'week'}">selected</c:if> value="week">1周</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>下周活动状态&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<select name="nextStatus" onchange="editNextStatus(this)" style="width: 280px;">
					<option <c:if test="${event.nextStatus == 1}">selected</c:if> value="1">开启</option>
					<option <c:if test="${event.nextStatus == 0}">selected</c:if> value="0">关闭</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>下周排行类型&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<select name="nextType" onchange="editNextStatus(this)" style="width: 280px;">
					<option <c:if test="${event.nextType == 0}">selected</c:if> value="0">积分</option>
					<option <c:if test="${event.nextType == 1}">selected</c:if> value="1">收集时间</option>
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
		<button id="save">修改</button>
		<button id="back" onclick="javascript:back();">返回</button>
	</div>
</div>
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/event" method="post">
</form>
<div id="uploadDIV" title="列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>
</html>