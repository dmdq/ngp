<%-- 
	模块名称 :
	功能名称 :
	创建人 : limingjun                  创建日期 : 2013-4-15
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
	//-->
	
	function back() {
		var url = "${pageContext.request.contextPath}/web/engine";
		$("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
	}
	
	function checkStatusLock(statusLock){
		if(statusLock.value == 0){
			$("#s").attr("disabled", "");
			$("#status").val($("#s").val());
		} else {
			$("#s").attr("disabled", "disabled");
			$("#status").val($("#s").val());
		}
	}
	
	function checkStatus(status) {
		$("#status").val(status.value);
	}
	
	$(document).ready(function(){
		var statusLock = $("#statusLock").val();
		if(statusLock == 1){
			$("#s").attr("disabled", "disabled");
		}
		$("#status").val($("#s").val());
    });
</script>

</head>
<body>
	<h2>Engine修改</h2>
	
	<!-- 内容区域 -->
	<div id="content" align="center">
<form:form id="form1" commandName="engine" modelAttribute="engine"
	action="${pageContext.request.contextPath}/web/engine/update" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="${p_id}" name="p_id" id="p_id"/>
	<input type="hidden" value="${p_status}" name="p_status" id="p_status"/>
	<table class="entity-form" align="center">
		<tr>
			<th>名称&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<input type="hidden" name="id" id="id" value="${engine.id}" />
				<input type="hidden" name="status" id="status" />
				<form:input path="name" cssClass="required" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>Status&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<select name="s" id="s" style="width: 280px;" onchange="checkStatus(this)">
					<c:forEach items="${engineStatusMap }" var="item">
						<c:choose>
							<c:when test="${item.key == engine.status }">
								<option value="${item.key }" selected="selected">${item.value }</option>
							</c:when>
							<c:otherwise>
								<option value="${item.key }">${item.value }</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>Status描述&nbsp;&nbsp;</th>
			<td>
				<form:input path="statusDesc" size="50" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<th>Status锁定&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<select name="statusLock" id="statusLock" class="requeired" style="width: 280px;" onchange="checkStatusLock(this)">
						<option <c:if test='${engine.statusLock == 0 }'>selected</c:if> value="0">不锁定</option>
						<option <c:if test='${engine.statusLock == 1 }'>selected</c:if> value="1">锁定</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>HOST&nbsp;<font color="red">*</font>&nbsp;</th>
			<td>
				<form:input path="host" cssClass="required" size="50" maxlength="50"/>
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