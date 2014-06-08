<%-- 
	模块名称 :
	功能名称 :
	创建人 : limingjun                  创建日期 : 2013-4-18
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

<style type="text/css">
<!--
  label.error {
    color: red;
    font-weight: bold;
    padding-left: 20px;
  }
  
  jsonItem {
  	width: 100px;
  }
  
  red {
  	color: red;
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
		
		var validator = $("#form1").validate({meta : "validate"});
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
	
	var jsonIndex = 2;
	
	function addJsonItem() {
    	var td = document.getElementById("add");
    	var br = document.createElement("br");
    	var keySpan = document.createElement("span");
    	var keyInput = document.createElement("input");
    	var valSpan = document.createElement("span");
    	var valInput = document.createElement("input");
    	var span = document.createElement("span");
    	var submit = document.createElement("input");
    	
    	keyInput.type = "text";
    	keyInput.className = "required";
    	keyInput.style.width = "100px";
    	keyInput.name = "key" + jsonIndex;
    	
    	valInput.type = "text";
    	valInput.className = "required";
    	valInput.style.width = "100px";
    	valInput.name = "value" + jsonIndex;
    	
    	keySpan.innerHTML = "Key:";
    	valSpan.innerHTML = "Value:";
    	
    	span.style.color = "red";
    	span.innerHTML = "*";
    	submit.type = "button";
    	submit.value = "remove";

    	submit.onclick = function() {
    		td.removeChild(br);
    		td.removeChild(keySpan);
    		td.removeChild(keyInput);
    		td.removeChild(valSpan);
    		td.removeChild(valInput);
    		td.removeChild(span);
    		td.removeChild(submit);
    	}
    	td.appendChild(br);
    	td.appendChild(keySpan);
    	td.appendChild(keyInput);
    	td.appendChild(valSpan);
    	td.appendChild(valInput);
    	td.appendChild(span);
    	td.appendChild(submit);
    	jsonIndex ++;
    }

    function remove(id) {
		var div = document.getElementById(id);
		div.innerHTML = "";
    }
    
    function selectView(url) {
		var appId = $("#appId").val() == null ? "" : $("#appId").val();
		var suffixUrl = appId == "" ? "" : "?appId=" + appId;
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
				if($("#status").val() == 0){
					$("#st option:first").attr('selected','selected');
					$("#st").attr("disabled","disabled");
				} else if($("#status").val() == -1) {
					$("#st option:last").attr('selected','selected');
					$("#st").attr("disabled","disabled");
				}
			},
			buttons: {
				Ok: function() {
					if($("#status").val() == 0){
						$("#st option:first").attr('selected','selected');
						$("#st").attr("disabled","disabled");
					} else if($("#status").val() == -1) {
						$("#st option:last").attr('selected','selected');
						$("#st").attr("disabled","disabled");
					}
					$(this).dialog('close');
				}
			}
		}); 
	}

//-->
</script>

</head>
<body>
	<h2>Create App Config</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="appConfig" modelAttribute="appConfig"
	action="${pageContext.request.contextPath}/web/appConfig/add" method="post">
	<table class="entity-form" align="center">
		<tr>
			<th>AppId&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<input type="hidden" name="appId" id="appId" />
				<input type="text" name="appName" id="appName" readonly="readonly" class="required" size="30" />
				<input type="hidden" name="status" id="status" />
				<input type="button" id="selectApp" onclick="selectView('<c:url value='/web/appConfig/selectApp'/>');" value="选择">
			</td>
		</tr>
		<tr>
			<th>是否公用&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<select name="st" id="st" style="width: 200px;" disabled="disabled">
					<option value="0">是</option>
					<option value="-1">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Json Data&nbsp;<font color="red">*</font>&nbsp;</th>
			<td id="add">
				Key:<input type="text" name="key1" id="key1" class="required" style="width: 100px;">Value:<input type="text" name="value1" id="value1" class="required" style="width: 100px;">
				<span style="color: red;">*</span>
				<input type="button" id="addItem" value="Add..." onclick="addJsonItem()">
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
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/appConfig" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form>

<div id="uploadDIV" title="App列表" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>