<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-5-22
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
	src="<c:url value='/js/jquery/ui/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/ui/external/jquery.cookie.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/ui/external/jquery.metadata.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery/ui/external/qunit.js'/>"></script>
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
				alert("id已存在，操作失败！");
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
		var type = "<c:out value='${choice}' />";
		var checkBaId = "<c:out value='${checkBaId}' />";
		var checkId = "<c:out value='${checkId}' />"
		var baid = "<c:out value='${baid}'/>";
		if (type == 'app') {
			$("#appShow").show();
			$("#showBaseId").show();
			$("#zoneShow").hide();
			$("#showBase").show();
			$("#term").html("App");

			checkBaids(baid);
			
		} else if (type == 'zone') {
			$("#appShow").hide();
			$("#zoneShow").show();
			$("#showBaseId").hide();
			$("#showBase").hide();
			$("#term").html("Zone");
		}
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

	function searchSubmit(){
    	$("#current_page").val(0);
    	resetParams();
    	$("#searchAndPageForm").submit();
    }

	function hide(type) {
		$("#p_choice").val(type);
		if (type == 'app') {
			$("#appShow").show();
			$("#showBaseId").show();
			$("#zoneShow").hide();
			$("#showBase").show();
			$("#term").html("App");
		} else if (type == 'zone') {
			$("#appShow").hide();
			$("#zoneShow").show();
			$("#showBaseId").hide();
			$("#showBase").hide();
			$("#term").html("Zone");
		}
	}
	
	function checkBase(app) {
		var a = app.value;
		$("#p_id").val(a.split(",")[0]);
		$("#p_checkBaId").val(a.split(",")[1]);
		var strs= new Array()
		strs = a.split(",")
		if(strs[0] == strs[1]) {
			$("#baid").attr("disabled","");
		} else {
			$("#baid").attr("disabled","disabled");
		}
	}
	
	function resetParams(){
    	if ($("#baids").val() == -1) {
    	}
    	if ($("#baids").val() == 0) {
    		$("#baids").val(0);
    	}
    	if ($("#baids").val() == 1) {
    		$("#baids").val(1);
    	}
    	$("#p_baid").val($("#baid").val());
    }
	
	function checkBaids(baids) {
    	if(baids == 1) {
    		$("#s1").css("display","none");
    		$("#s2").css("display","none");
    		$("#s3").css("display","block");
    	} else if (baids == 0) {
    		$("#s1").css("display","none");
    		$("#s2").css("display","block");
    		$("#s3").css("display","none");
    	} else {
    		$("#s1").css("display","block");
    		$("#s2").css("display","none");
    		$("#s3").css("display","none");
    	}
    }
	
	function checkZoneId(zoneId){
		$("#p_zoneId").val(zoneId.value);
	}
//-->
</script>

</head>
<body>
	<h2>AppZone 列表</h2>
	<!-- 内容区域 -->
	<div id="content" align="center">
		<form:form id="searchForm" commandName="searchAppZone" modelAttribute="searchAppZone" action="${pageContext.request.contextPath}/web/appZone" method="post">
			<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>选择</th>
						<td>
							<select name="choice" id="choice" onchange="hide(this.value);" style="width: 180px;">
								<option <c:if test="${choice == 'app'}">selected</c:if> value="app">App</option>
								<option <c:if test="${choice == 'zone'}">selected</c:if> value="zone">Zone</option>
							</select>
						</td>
						<th id="term">App<input type="hidden" name="baids" id="baids" value="-1" /></th>
						<td id="appShow">
							<span style="display: block;" id="s1">
							<select name="id1" id="id1" style="width: 280px;" onchange="checkBase(this);">
								<option value="-1,-1">-------请选择-------</option>
								<c:forEach items="${aps}" var="app">
									<option <c:if test='${app.id == searchAppZone.appId}'>selected</c:if> value="${app.id},${app.baseId}">${app.name}</option>
								</c:forEach>
							</select>
							</span>
							<span style="display: none;" id="s2">
								<select name="id2" id="id2" style="width: 280px;" onchange="checkBase(this);">
									<option value="-1,-1">-------请选择-------</option>
									<c:forEach items="${aps}" var="app">
										<c:if test="${app.id != app.baseId}">
											<option <c:if test='${app.id == searchAppZone.appId}'>selected</c:if> value="${app.id},${app.baseId}">${app.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</span>
							<span style="display: none;" id="s3">
								<select name="id3" id="id3" style="width: 280px;" onchange="checkBase(this);">
									<option value="-1,-1">-------请选择-------</option>
									<c:forEach items="${aps}" var="app">
										<c:if test="${app.id == app.baseId}">
											<option <c:if test='${app.id == searchAppZone.appId}'>selected</c:if> value="${app.id},${app.baseId}">${app.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</span>
						</td>
						<td id="zoneShow" style="display: none;">
							<select name="zoneId" id="zoneId" onchange="checkZoneId(this)">
								<option value="">----------请选择---------</option>
								<c:forEach items="${zs}" var="zone">
									<option <c:if test="${zone.id == searchAppZone.zoneId}">selected</c:if> value="${zone.id}">${zone.name}</option>
								</c:forEach>
							</select>
						</td>
						<th id="showBase">是否BaseId</th>
						<td id="showBaseId">
							<select name="baid" id="baid" style="width: 250px;" onchange="checkBaids(this.value);">
								<option value="-1">-------请选择-------</option>
								<option <c:if test="${baid == '1'}">selected</c:if> value="1">是</option>
								<option <c:if test="${baid == '0'}">selected</c:if> value="0">否</option>
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
		<c:if test="${empty apps && empty zones}">
			<table class="datagrid1" style="width: 80%;">
				<tr>
					<th>AppId</th>
					<th>AppName</th>
					<th>ZoneId</th>
					<th>ZoneName</th>
					<th>是否BaseId</th>
				</tr>
				<tr>
					<td colspan="5" style="text-align: center;">没有找到相关数据</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${!empty apps || !empty zones}">
			<c:if test="${choice == 'app'}">
				<display:table name="apps" defaultsort="1" requestURI="/web/appZone" class="datagrid1" style="width:80%;" defaultorder="descending" id="app">
					<display:column property="id" title="App" />
					<display:column property="name" title="AppName" />
					<display:column title="Zone">
						<c:forEach items="${app.zones}" var="en" varStatus="status">
							<c:out value="${en.name }" />(<c:out value="${en.id}" />)<c:if
								test="${(status.index + 1) != fn:length(app.zones)}">,</c:if>
						</c:forEach>
					</display:column>
				</display:table>
			</c:if>
			<c:if test="${choice == 'zone'}">
				<display:table name="zones" defaultsort="1" requestURI="/web/appZone" class="datagrid1" style="width:80%;" defaultorder="descending" id="zone">
					<display:column property="id" title="Zone" />
					<display:column property="name" title="Name" />
					<display:column title="AppName">
						<c:forEach items="${zone.apps}" var="en" varStatus="status">
							<c:out value="${en.name }" />(<c:out value="${en.id}" />)<c:if
								test="${(status.index + 1) != fn:length(zone.apps)}">,</c:if>
						</c:forEach>
					</display:column>
				</display:table>
			</c:if>
		</c:if>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<div id="Pagination" style="margin-left: 10%;"></div>
	</div>
	<br>
	<br>
	<!-- 按钮区域 -->
	<div class="tinbox"></div>
	<form id="deleteForm" method="post">
		<input type="hidden" name="_method" value="delete" />
	</form>

	<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/appZone" method="post">
		<input type="hidden" id="pageFormmethod" name="_method" value="get" />
		<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric" /> 
		<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric" />
		<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric" /> 
		<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric" /> 
		<input type="hidden" value="Prev" name="prev_text" id="prev_text" /> 
		<input type="hidden" value="Next" name="next_text" id="next_text" />
		<input type="hidden" name="p_choice" id="p_choice" value="${choice}" />
		<input type="hidden" name="p_id" id="p_id" />
		<input type="hidden" name="p_baid" id="p_baid" />
		<input type="hidden"name="p_checkBaId" id="p_checkBaId">
		<input type="hidden" name="p_zoneId" id="p_zoneId" />
	</form>
</body>
</html>