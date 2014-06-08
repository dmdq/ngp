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
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/style.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/jquery-ui-1.8.css'/>" />
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
		
		jQuery.validator.addMethod("image", function(value, element) {
			var image = new RegExp(/(?:gif|jpg|png|bmp)$/);
			return this.optional(element) || (image.test(value));
		}, "上传文件应为图片类型");

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
	
	/**
	 * 使多项框中内容都被选中
	 */
	function setMultiSelectOption(selectName){
		var obj = document.getElementById(selectName);
	    for(var i = 0; i < obj.length; i++){
	        obj[i].selected = true;
	    }
	}
	
	function submit() {
		document.forms[0].submit();
	}

	function back() {
		$("#searchAndPageForm").submit();
	}

	function selectView(url) {
		var baseId = $("#baseId").val() == null ? "" : $("#baseId").val();
		var suffixUrl = baseId == "" ? "" : "?baseId=" + baseId;
		$("#upload-iframe").attr("src", url +  suffixUrl);
		$("#uploadDIV").dialog({
			resizable : true,
			height : 600,
			width : 600,
			autoOpen : true,
			modal : true,
			open: function() {
			},
			close: function() {
			},
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			}
				
		}); 
	}
//-->
</script>

</head>
<body>
	<h2>Create Promo</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="searchPromo" modelAttribute="searchPromo" 
	action="${pageContext.request.contextPath}/web/promo/add" enctype="multipart/form-data" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="${p_id}" name="p_id" id="p_id"/>
	<table class="entity-form" align="center">
		<tr>
			<th>名称&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:input path="name" cssClass="required" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>Link&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:input path="link" size="50" cssClass="required" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>Index&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<form:input path="idx" size="50" cssClass="required digits" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>Promo Icon&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="file" name="icons" class="required image" id="icon" size="50" />
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/promo" method="post">
</form>
<div id="uploadDIV" title="列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>
</html>