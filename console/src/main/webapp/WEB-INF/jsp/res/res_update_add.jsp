<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-5-6
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/head.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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

<script type="text/javascript">
	$(function() {
		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "failed") {
				alert("操作失败！");
			} else if(msg = "faileds") {
				alert('该图片已存在！');
			}
		}
		
		systeminit();
		$("#save").button();
		$("#back").button();
		$("#addAppBtn").button();
		$("#addPhotoBtn").button();
		
		var validator = $("#form1").validate({meta : "validate"});
		
		// 点击“保存”按钮时先验证，验证通过后方能保存
		$("#save").click(function() {
			setMultiSelectOption('resUpdateNames');
			if (validator.form()) { //若验证通过，则调用保存/修改方法
				submit();
			}
		});
		
	});
	
	function submit() {
		document.forms[0].submit();
	}
	
	function systeminit() {
	}
	
	function setMultiSelectOption(selectName){
		var obj = document.getElementById(selectName);
		
	    for(var i = 0; i < obj.length; i++){
	        obj[i].selected = true;
	    }
	}
	
	function selectView(url, type) {
		if(type == "app") {
			var appId = $("#appId").val() == null ? "" : $("#appId").val();
			var suffixUrl = appId == "" ? "" : "?appId=" + appId;
		} else if(type == "photo") {
			setMultiSelectOption('resUpdateNames');
			var resUpdateNames = $("#resUpdateNames").val() == null ? "" : $("#resUpdateNames").val();
			var suffixUrl = resUpdateNames == "" ? "" : "?resUpdateNames=" + resUpdateNames;
		}
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
	function back() {
		$("#searchAndPageForm").submit();
	}
</script>

</head>
<body>
	<h2>Create ResUpdate</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="searchResUpdate" modelAttribute="searchResUpdate"
	action="${pageContext.request.contextPath}/web/resUpdate/add" enctype="multipart/form-data" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="${p_appId}" name="p_appId" id="p_appId"/>
	<table class="entity-form" align="center">
		<tr>
			<th>AppId&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input type="hidden" name="appId" id="appId" />
				<input type="text" name="appName" id="appName" readonly="readonly" class="required" size="30" maxlength="30" />
				<input type="button" id="addAppBtn" onclick="selectView('<c:url value='/web/resUpdate/selectApp'/>', 'app');" value="选择">
			</td>
		</tr>
		<tr>
			<th>OldVersion&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="oldVersion" size="30" class="required digits" maxlength="30"/>
			</td>
		</tr>
		<tr>
			<th>NewVersion&nbsp;<font color="red">*</font>&nbsp;</th>
			<td colspan="2">
				<input name="newVersion" class="required digits" size="30" maxlength="30"/>
			</td>
		</tr>
		<tr>
			<th>文件&nbsp;&nbsp;</th>
			<td>
				<select name="resUpdateNames" id="resUpdateNames" multiple="multiple" style="width:100%" size="10"></select>
			</td>
			<td>
				<input type="button" onclick="selectView('<c:url value='/web/resFile/selectResFiles'/>', 'photo');" id="addPhotoBtn" value="选择">
			<br>
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/resUpdate" method="post">
</form>
<div id="uploadDIV" title="列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>