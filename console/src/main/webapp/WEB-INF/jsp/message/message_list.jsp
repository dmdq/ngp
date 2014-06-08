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

<title>游戏管理系统</title>

<!-- CSS AREA -->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/theme/style.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/theme/jquery-ui-1.8.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/theme/img_list.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/jquery/pagination/pagination.css'/>" />

<style type="text/css">
<!--
-->
</style>

<!-- JAVASCRIPT AREA -->
<c:url value='' />
<script type="text/javascript" src="<c:url value='/js/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/util.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/ui/i18n/jquery.ui.datepicker-zh-CN.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/external/jquery.cookie.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/external/qunit.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/pagination/jquery.pagination.js'/>"></script>

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
		$("#addBtn").button();
		$("#searchBtn").button();

		//customer

		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "same") {
				alert("AppId已存在，操作失败！");
			} else if (msg == "failed") {
				alert("操作失败！");
			}
		}
	});

	function systeminit() {
	}

	/**
	 * Callback function that displays the content.
	 *
	 * Gets called every time the user clicks on a pagination link.
	 *
	 * @param {int}page_index New Page index
	 * @param {jQuery} jq the container with the pagination links as a jQuery object
	 */
	var init = 0;
	function pageselectCallback(page_index, jq) {
		// Get number of elements per pagionation page from form
		$('#current_page').val(page_index);
		resetParams();
		if (init != 0)
			$("#searchAndPageForm").submit();
		init = 1;
		return false;
	}

	$(document).ready(function() {
		$("#Pagination").pagination(<c:out value="${totalNum }"/>, {
			callback : pageselectCallback,
			items_per_page : $("#items_per_page").val(),
			num_display_entries : $("#num_display_entries").val(),
			num_edge_entries : $("#num_edge_entries").val(),
			current_page : parseInt($("#current_page").val()),
			prev_text : $("#prev_text").val(),
			next_text : $("#next_text").val()
		});
	});

	function newApp() {
		var url = '<c:url value="/web/app/toAdd"/>';
		$("#searchAndPageForm").attr("action", url);
		$("#searchAndPageForm").submit();
	}

	function searchSubmit() {
		$("#current_page").val(0);
		resetParams();
		$("#searchAndPageForm").submit();
	}

	function edit(appId) {
		var url = '<c:url value="/web/app/"/>' + appId + "/toUpdate";
		resetParams();
		$("#searchAndPageForm").attr("action", url);
		$("#searchAndPageForm").submit();
	}

	function resetParams() {
		$("#p_id").val($("#id").val());
		$("#p_status").val($("#status").val());
	}
//-->
</script>

</head>
<body>
	<h2>Message 列表</h2>
	<!-- 内容区域 -->
	<div id="content" align="center">
		<form:form id="searchForm" commandName="searchMessage"
			modelAttribute="searchMessage"
			action="${pageContext.request.contextPath}/web/message" method="post">
			<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>Title</th>
						<td>
							<select name="id" id="id" style="width: 280px;">
								<option value="">-------请选择-------</option>
								<c:forEach items="${mess}" var="mes">
									<option <c:if test='${mes.id == searchMessage.id}'>selected</c:if> value="${mes.id}">${mes.title}</option>
								</c:forEach>
							</select>
						</td>
						<th>Status</th>
						<td>
							<select name="status" id="status" style="width: 280px;">
								<option value="">-------请选择-------</option>
								<option <c:if test="${searchMessage.status == '1'}">selected</c:if> value="1">已读</option>
								<option <c:if test="${searchMessage.status == '0'}">selected</c:if> value="0">未读</option>
							</select>
						</td>
					</tr>
				</thead>
			</table>
			<div style="width: 100%; text-align: center; margin-top: 5px;">
				<input id="searchBtn" onclick="searchSubmit();" type="submit"
					value="查询" />
			</div>
		</form:form>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<c:if test="${empty messages}">
			<table class="datagrid1" style="width: 80%;">
				<tr>
					<th>Icon</th>
					<th>name</th>
					<th>AppId</th>
					<th>baseId</th>
					<th>baseName</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<tr>
					<td colspan="7" style="text-align: center;">没有找到相关数据</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${!empty messages}">
			<display:table name="messages" defaultsort="1" requestURI="/web/message" class="datagrid1" style="width:80%;" defaultorder="descending" id="message">
				<display:column property="from" title="发送人" />
				<display:column property="to" title="接收人" />
				<display:column property="title" title="Title" />
				<display:column property="body" title="Body" />
				<display:column property="attach" title="Attach" />
				<display:column property="type" title="Type" />
				<display:column title="状态">
					<c:if test="${message.status == '1'}">已读</c:if>
					<c:if test="${message.status == '0'}">未读</c:if>
				</display:column>
			</display:table>
		</c:if>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<div id="Pagination" style="margin-left: 10%;"></div>
	</div>
	<br>
	<br>
	<!-- 按钮区域 -->
	<div class="tinbox">
		<!-- <button id="addBtn" onclick="javascript:newApp();">新增</button> -->
	</div>
	<form id="deleteForm" method="post">
		<input type="hidden" name="_method" value="delete" />
	</form>
	<div id="uploadDIV" title="图片详细" style="display: none;">
		<iframe id="upload-iframe" scrolling="yes" name="upload-iframe" src=""
			width="100%" height="100%" frameborder="0"></iframe>
	</div>

	<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/message" method="post">
		<input type="hidden" id="pageFormmethod" name="_method" value="get" />
		<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric" /> 
		<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric" />
		<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric" /> 
		<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric" /> 
		<input type="hidden" value="Prev" name="prev_text" id="prev_text" /> 
		<input type="hidden" value="Next" name="next_text" id="next_text" /> 
		<input type="hidden" name="p_id" id="p_id" />
		<input type="hidden" name="p_status" id="p_status" />
	</form>
</body>
</html>