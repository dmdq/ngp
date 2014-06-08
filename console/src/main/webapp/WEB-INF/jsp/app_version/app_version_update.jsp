<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei              创建日期 : 2013-5-20
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
		$("#addAppBtn").button();
		
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

//-->
</script>

</head>
<body>
	<h2>Update AppVersion</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="appVersion" modelAttribute="appVersion"
	action="${pageContext.request.contextPath}/web/appVersion/update" enctype="multipart/form-data" method="post">
	<table class="entity-form" align="center">
		<tr>
			<th>App&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" name="id" value="${appVersion.id}" />
				<input type="text" name="appId" id="appId" value="${appVersion.appId }" readonly="readonly" class="required" size="50" maxlength="30" />
			</td>
		</tr>
		<tr>
			<th>Version&nbsp;&nbsp;</th>
			<td colspan="2">
				<form:input path="version" size="50" maxlength="50"/>
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/appVersion" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form>
<div id="uploadDIV" title="Engine列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>
</html>