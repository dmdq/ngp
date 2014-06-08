<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-5-2
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

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<title>内容管理系统</title>

<!-- CSS AREA -->
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/style.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/jquery-ui-1.8.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/img_list.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/uploadify.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/pagination/pagination.css'/>" />

<style type="text/css">
<!--
-->
</style>

<!-- JAVASCRIPT AREA -->
<script type="text/javascript" src="<c:url value='/js/pms/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/pms/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.uploadify.v2.1.4.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/swfobject.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/i18n/jquery.ui.datepicker-zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/pagination/jquery.pagination.js'/>"></script>

<script type="text/javascript">
	$(function() {
		systeminit();
		$("#addBtn").button();
		$("#searchBtn").button();
		
		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "failed") {
				alert("操作失败！");
			} else if (msg = "exist") {
				alert('该图片已存在！');
			}
		}

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
		$("#addBtn").click(function() {
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
	
	$(document).ready(function() {
        $("#uploadify").uploadify({
            'uploader' : '<%=request.getContextPath()%>/img/uploadify.swf',
            'script' : '<%=request.getContextPath()%>/web/avatarFile/add',//后台处理的请求
            'cancelImg' : '<%=request.getContextPath()%>/img/cancel.png',
            'fileDataName':'urn',//服务器端根据这个接收文件
            'queueID' : 'fileQueue',//与下面的id对应
            'queueSizeLimit' : 30,
            'fileDesc' : '图片文件',
            'fileExt' : '*.png;*.jpg;*.gif;*.bmp', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
            'auto' : false,
            'multi' : true,
            'simUploadLimit' : 2,
            'buttonText' : 'BROWSE',
            'onError' : function(event, queueID, fileObj, errorObj) {
                alert(" 上传失败" + errorObj.info + "错误类型" + errorObj.type);
            },
            'onComplete' : function(event, queueID, fileObj, response, data) {
            	var msg = response;
            	if (msg != '') {
        			if (msg == "success") {
        				alert("操作成功！");
        			} else if (msg == "failed") {
        				alert("操作失败！");
        			} else if (msg = "exist") {
        				alert('该图片已存在！');
        			}
        		}
            }
        });

    });
</script>

</head>
<body>
<div style="width: 500px; height: 400px; margin: 0px auto;">
	<h2>上传图片</h2>
	<div style="width: 500px; height: 30px;">
		<form:form id="form1" commandName="avatarFile" modelAttribute="avatarFile"
			action="${pageContext.request.contextPath}/web/avatarFile/add" enctype="multipart/form-data" method="post">
			<!-- 内容区域 -->
			<table class="datagrid1" style="width: 100%;">
				<tr>
					<td>
						<div id="fileQueue"></div>
						<input type="file" name="urn" id="uploadify" /><br/>
						<a href="javascript:$('#uploadify').uploadifyUpload()">上传</a>|
						<a href="javascript:$('#uploadify').uploadifyClearQueue()"> 取消上传</a>
					</td>
				</tr>
			</table>
		</form:form>
	</div>
	<div style="height: 20px;margin-top: 20px;" class="btn-container"></div>
	<!-- 按钮区域 -->
	<div class="tinbox">
	</div>
</div>
</body>
</html>