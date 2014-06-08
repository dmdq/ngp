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
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/qunit.js'/>"></script>
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

		var validator = $("#form1").validate({meta : "validate"});
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
	
	function back() {
		var url = "${pageContext.request.contextPath}/web/appConfig";
		$("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
	}
	
	function removeItem(id) {
		var div = document.getElementById(id);
		div.innerHTML = "";
		
    }
	
	var jsonData = "<c:out value='${jsonMap}' />";
	
	var jsonIndex = parseInt(jsonData.split(",").length) + 1;
	
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
	
	//-->
</script>

</head>
<body>
	<h2>App Config修改</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="appConfig" modelAttribute="appConfig"
	action="${pageContext.request.contextPath}/web/appConfig/update" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<table class="entity-form" align="center">
		<tr>
			<th>AppId&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<c:out value="${appConfig.appId }"></c:out>
			</td>
		</tr>
		<tr>
			<th>App Name&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<c:out value="${appConfig.app.name }"></c:out>
			</td>
		</tr>
		<tr>
			<th>是否公用&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<c:if test="${mes == '是'}"><input type="hidden" value="0" name="mes" /></c:if>
				<c:if test="${mes != '是'}"><input type="hidden" value="-1" name="mes" /></c:if>
				<c:out value="${mes}"></c:out>
			</td>
		</tr>
		<tr>
			<th>Json Data&nbsp;<font color="red">*</font>&nbsp;</th>
			<td id="add">
				<form:hidden path="appId"/>
				<c:forEach items="${jsonMap}" var="entry" varStatus="status">
				    <div id="item${status.index + 1}">
					    Key:<input type="text" name="key${status.index + 1}" id="key${status.index + 1}" value="${entry.key }" class="required" style="width: 100px;">Value:<input type="text" name="value${status.index + 1}" id="value${status.index + 1}" value="${entry.value }" class="required" style="width: 100px;">
						<span style="color: red;">*</span>
						<input type="button" id="button" value="remove" onclick="removeItem('item${status.index + 1}');"><br/>
				    </div>
				</c:forEach>
				<input type="button" id="addItem" value="Add..." onclick="addJsonItem()">
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
	
<form:form id="searchAndPageForm" commandName="song" modelAttribute="song"
	action="" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form:form>

</div>
</body>
</html>