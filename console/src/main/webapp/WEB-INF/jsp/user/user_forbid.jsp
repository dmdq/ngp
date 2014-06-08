<%-- 
	模块名称 :
	功能名称 :
	创建人 : limingjun                  创建日期 : 2013-4-22
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
	span.redBtn {
		color: red;
	}
-->
</style>

<!-- JAVASCRIPT AREA -->
<c:url value='' />
<script type="text/javascript" src="<c:url value='/js/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.10.2.js'/>"></script>
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
	 * create on : 2010-6-17
	 * last modifier : 
	 */
	$(function() {
		//system
		systeminit();
		$("#searchBtn").button();
		$("#button1").button();
		$("#button2").button();

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

	

	/**
     * Callback function that displays the content.
     *
     * Gets called every time the user clicks on a pagination link.
     *
     * @param {int}page_index New Page index
     * @param {jQuery} jq the container with the pagination links as a jQuery object
     */
     var init = 0;
 	function pageselectCallback(page_index, jq){
         // Get number of elements per pagionation page from form
         $('#current_page').val(page_index);
         resetParams();
         if(init != 0)
        		 $("#searchAndPageForm").submit();
 		init = 1;
         return false;
     }
    
    function resetParams(){
    	$("#p_user").val($("#userId").val());
    }
    
    function delDragon(userId){
    	if (confirm("是否要删除游戏数据？")) {
    		var url = '<c:url value="/web/user/forbid/"/>' + userId + "/del";
    	    $("#searchAndPageForm").attr("action", url);
    	    $("#searchAndPageForm").submit();
    	}
    }
	//-->
</script>

</head>
<body>
<h2>User 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchUser" modelAttribute="searchUser"
	action="${pageContext.request.contextPath}/web/user/forbid" method="post">
<table class="datagrid1" align="center" style="width:80%;">
<thead>
<tr>
<th>userId</th>
<td>
	<input type="text" name="id" value="${userId}" id="id" style="width: 180px;" />
</td>
</tr>
</thead>
</table>
<div style="width:100%;text-align: center;margin-top:5px;">
<input id="searchBtn" type="submit" value="查询"/>
</div>	
</form:form>
<c:set var="btnStatus" value="-1"/>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty uid}">
	<table class="datagrid1" style="width:80%;">
         <tr>
            <th>ID</th>
            <th>昵称</th>
            <th>等级</th>
            <th>宝石</th>
            <th>金币</th>
            <th>操作</th>
         </tr>
         <tr>
         	<td colspan="6" style="text-align: center;">没有找到相关数据</td>
         </tr>
	</table>
</c:if>
<c:if test="${!empty dragonData}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>ID</th>
            <th>昵称</th>
            <th>等级</th>
            <th>宝石</th>
            <th>金币</th>
            <th>操作</th>
         </tr>
		<tr>
			<td>${dragonData.userId}</td>
			<td>
				${dragonData.nick}
			</td>
			<td>
				${dragonData.level}
			</td>
			<td>
				<c:if test="${empty dragonData.coin}">0</c:if>
				<c:if test="${!empty dragonData.coin}">${dragonData.coin}</c:if>
			</td>
			<td>
				<c:if test="${empty coppernum}">0</c:if>
				<c:if test="${!empty coppernum}">${coppernum}</c:if>
			</td>
			<td>
				<a href="#" onclick="delDragon(${dragonData.userId})">删除&nbsp;&nbsp;</a>
			</td>
		</tr>
      </table>
</c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<form id="deleteForm" action="" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
<form id="searchAndPageForm" action="" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form>
</body>
</html>