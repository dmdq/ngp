<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-4-25
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
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/img_list.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/pagination/pagination.css'/>" />

<style type="text/css">
<!--
-->
</style>

<!-- JAVASCRIPT AREA -->
<c:url value='' />
<script type="text/javascript" src="<c:url value='/js/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/i18n/jquery.ui.datepicker-zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/qunit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/pagination/jquery.pagination.js'/>"></script>

<script type="text/javascript">
	/**
	 * description : 页面初始回调函数
	 *
	 * author : system
	 * create on : 2013-4-18
	 * last modifier : 
	 */
	$(function() {
		//system
		systeminit();
		$("#addBtn").button();
		$("#searchBtn").button();
		$("#beginTime").datepicker({
			dateFormat : 'yy-mm-dd'
		});
		//customer

		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "failed") {
				alert("操作失败！");
			}
		}
	});

	function systeminit() {
	}

	function deletePlacard(placardId) {
		if (confirm("确定要删除该记录吗？")) {
			var url = '<c:url value="/web/placard/del/"/>' + placardId;
			$("#pageFormmethod").val("delete");
			$("#searchAndPageForm").attr("action", url);
			$("#searchAndPageForm").submit();
		}
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

	function newPlacard() {
		var url = '<c:url value="/web/placard/toAdd"/>';
		resetParams();
		$("#searchAndPageForm").attr("action", url);
		$("#searchAndPageForm").submit();
	}

	function searchSubmit() {
		resetParams();
		$("#current_page").val(0);
		$("#searchAndPageForm").submit();
	}

	function edit(name) {
		var url = '<c:url value="/web/placard/"/>' + name + "/toUpdate";
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
	<h2>Placard 列表</h2>
	<!-- 内容区域 -->
	<div id="content" align="center">
		<form:form id="searchForm" commandName="searchPlacard"
			modelAttribute="searchPlacard"
			action="${pageContext.request.contextPath}/web/placard" method="post">
			<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>标题</th>
						<td>
							<select name="id" id="id" style="width: 280px;">
								<option value="">-------请选择-------</option>
								<c:forEach items="${ps}" var="p">
									<option
										<c:if test="${p.id == searchPlacard.id}">selected</c:if> value="${p.id}">${p.title}</option>
								</c:forEach>
							</select>
						</td>
						<th>状态</th>
						<td>
							<select name="status" id="status" style="width: 280px;">
								<option value="">-------请选择-------</option>
								<option <c:if test= '${placard.status == 0}'>selected</c:if> value="0">有效</option>
								<option <c:if test= '${placard.status == -1}'>selected</c:if> value="-1">无效</option>
							</select>
						</td>
					</tr>
				</thead>
			</table>
			<div style="width: 100%; text-align: center; margin-top: 5px;">
				<input id="searchBtn" onclick="searchSubmit();" type="button" value="查询" />
			</div>
		</form:form>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<c:if test="${empty placards}">
			<table class="datagrid1" style="width: 80%;">
				<tr>
					<th>标题</th>
					<th>标题字体</th>
					<th>标题Color</th>
					<th>内容</th>
					<th>内容字体</th>
					<th>内容Color</th>
					<th>创建时间</th>
					<th>创建人</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>操作</th>
				</tr>
				<tr>
					<td colspan="11" style="text-align: center;">没有找到相关数据</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${!empty placards}">
			<display:table name="placards" defaultsort="1"
				requestURI="/web/placard" class="datagrid1" style="width:80%;"
				defaultorder="descending" id="placard">
				<display:column property="title" title="标题" maxLength="10" maxWords="10" />
				<display:column property="titleSize" title="字体大小" />
				<display:column property="titleColor" title="标题颜色" />
				<display:column property="body" title="内容" maxLength="10" maxWords="10" />
				<display:column property="bodySize" title="字体大小" />
				<display:column property="bodyColor" title="内容颜色" />
				<display:column title="状态">
					<c:choose>
						<c:when test="${placard.status == 0 }">有效</c:when>
						<c:when test="${placard.status == -1 }">无效</c:when>
					</c:choose>
				</display:column>
				<display:column property="createBy" title="创建人" />
				<display:column title="开始时间">
					<fmt:formatDate value="${placard.startTime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				<display:column title="结束时间">
					<fmt:formatDate value="${placard.endTime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				<display:column title="操作">
					<a href="#" onclick="edit('${placard.id}');">编辑</a>
					<a href="#" onclick="javascript:deletePlacard('${placard.id }');">删除</a>
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
		<button id="addBtn" onclick="javascript:newPlacard();">新增</button>
	</div>
	<form id="deleteForm" method="post">
		<input type="hidden" name="_method" value="delete" />
	</form>

	<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/placard" method="post">
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