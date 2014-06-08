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
	 * create on : 2010-6-17
	 * last modifier : 
	 */
	$(function() {
		//system
		systeminit();
		$("#searchBtn").button();

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
        if(init != 0)
       		 $("#searchForm").submit();
		init = 1;
        return false;
    }
    
	$(document).ready(function(){
        $("#Pagination").pagination(<c:out value="${totalNum }"/>, {
            callback: pageselectCallback,
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
    	$("#searchForm").submit();
    }
    
    function edit(userId){
    	var url = '<c:url value="/web/user/"/>' + userId + "/toUpdateGameData";
		$("#searchAndPageForm").attr("action", url);
		$("#searchAndPageForm").submit();
    }
    
    function del(userId){
    	if (confirm("确定要删除该记录吗？")) {
	    	var url = '<c:url value="/web/user/"/>' + userId + "/delGameData";
			$("#searchAndPageForm").attr("action", url);
			$("#searchAndPageForm").submit();
    	}
    }
    
    function banAccount(userId){
    	var url = '<c:url value="/web/user/"/>' + userId + "/coc/ban";
    	$("#searchAndPageForm").attr("action", url);
    	$("#searchAndPageForm").submit();
    }
	//-->
</script>

</head>
<body>
<h2>游戏 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchUserDataShow" modelAttribute="searchUserDataShow"
	action="${pageContext.request.contextPath}/web/user/gameData" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
<table class="datagrid1" align="center" style="width:80%;">
<thead>
<tr>
<th>userId</th>
<td>
	<input type="text" name="userId" value="${userId}" id="userId" style="width: 280px;" />
</td>
<th>nick</th>
<td>
	<input type="text" name="nick" value="${nick}" id="nick" style="width: 280px;" />
</td>
</tr>
<tr>
	<th>level</th>
	<td>
		<input type="text" name="level" value="${level}" id="level" style="width: 280px;" />
	</td>
	<th>trophy</th>
	<td>
		<input type="text" name="trophy" value="${trophy}" id="trophy" style="width: 280px;" />
	</td>
</tr>
</thead>
</table>
<div style="width:100%;text-align: center;margin-top:5px;">
<input id="searchBtn" onclick="searchSubmit();" type="button" value="查询"/>
</div>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty userDataShows}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>ID</th>
            <th>昵称</th>
            <th>宝石</th>
            <th>奖杯</th>
            <th>等级</th>
            <th>状态</th>
            <th>操作</th>
         </tr>
         <tr>
   <td colspan="7" style="text-align: center;">没有找到相关数据</td>
</tr>
      </table>
      </c:if>
 <c:if test="${!empty userDataShows}">
<display:table name="userDataShows" defaultsort="1"
	requestURI="/web/user/gameData" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="userData">
	<display:column title="ID">
		${userData.userId}
	</display:column>
	<display:column title="昵称">
		${userData.nick}
	</display:column>
	<display:column title="宝石">
		${userData.jsonMap['Gem']}
	</display:column>
	<display:column title="奖杯">
		${userData.trophy}
	</display:column>
	<display:column title="等级">
		${userData.level}
	</display:column>
	<display:column title="状态">
		<c:if test="${userData.status == 1}">已封禁</c:if>
		<c:if test="${userData.status == 0}">正常</c:if>
	</display:column>
	<display:column title="操作">
		<a href="#" onclick="javascript:edit(${userData.userId});">修改</a>&nbsp;
		<a href="#" onclick="javascript:del(${userData.userId});">删除</a>&nbsp;
		<a href="#" onclick="javascript:banAccount(${userData.userId});">是否封禁</a>
	</display:column>
</display:table></c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/user/gameData" method="post">
</form>
</body>
</html>