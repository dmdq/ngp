<%-- 
	模块名称 :
	功能名称 :
	创建人 : limingjun                  创建日期 : 2013-4-25
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
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/messages_cn.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/i18n/jquery.ui.datepicker-zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/qunit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/pagination/jquery.pagination.js'/>"></script>

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
		$("#updateBtn").button();
		$("#addAppVersionBtn").button();
		$("#addAppBtn").button();
		
		var validator = $("#form1").validate({
			meta : "validate"});
		// 点击“保存”按钮时先验证，验证通过后方能保存
		$("#save").click(function() {
			if (validator.form()) { //若验证通过，则调用保存/修改方法
				submit();
			}
		});

		//customer
	});
	
	function systeminit() {
	}
	
	function submit(){
		document.forms[0].submit();
	}
	//-->
	
	function back() {
		var url = "${pageContext.request.contextPath}/web/placardTarget";
		$("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
	}
</script>

</head>
<body>
	<h2>PlacardTarget修改</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="placardTarget" modelAttribute="placardTarget"
	action="${pageContext.request.contextPath}/web/placardTarget/update" method="post" >
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<table class="entity-form" align="center">
		<tr>
			<th>Id&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<c:out value="${placardTarget.id }"></c:out>
			</td>
		</tr>
		<tr>
			<th>Placard Title&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:hidden path="id" />
				<c:out value="${placardTarget.placard.title }"></c:out>
			</td>
		</tr>
		<tr>
			<th>是否BaseId&nbsp;<font color="red">*</font>&nbsp</th>
			<td colspan="2">
				<input type="text" size="50" readonly="readonly" name="isBaid" value="${placardTarget.isBaid}" />
			</td>
		</tr>
		<tr id="appShow">
			<th>App&nbsp;&nbsp;</th>
			<td>
				<input type="text" size="50" readonly="readonly" name="appId" value="${placardTarget.appId}" />
			</td>
		</tr>
		<tr>
			<th>App Version&nbsp;&nbsp;</th>
			<td colspan="2">
				<input type="text" name="appVersion" id="appVersion" value="${placardTarget.appVersion}" readonly="readonly" size="50" maxlength="50" />
				<input type="button" id="addAppVersionBtn" onclick="selectView('<c:url value='/web/placardTarget/selectAppVersions'/>');" value="选择">
			</td>
		</tr>
	</table>
</form:form>
</div>
<div align="center">
<br>
	<!-- 按钮区域 -->
	<div class="btn-container">
		<button id="save">保存</button>
		<button id="back" onclick="back();">返回</button>
	</div>
	
<form:form id="searchAndPageForm" commandName="app" modelAttribute="app"
	action="" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form:form>

<div id="uploadDIV" title="Engine列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</div>
</body>
</html>