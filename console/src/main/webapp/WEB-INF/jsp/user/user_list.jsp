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

	function deleteApp(appId) {
		if (confirm("确定要删除该记录吗？")) {
	    	var url = '<c:url value="/web/app/del/"/>' + appId;
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
    
    function detailActivity(userId){
    	var url = '<c:url value="/web/user/"/>' + userId + "/lists";
        $("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
    }

    function banAccount(userId){
    	var url = '<c:url value="/web/user/"/>' + userId + "/platform/ban";
    	$("#searchAndPageForm").attr("action", url);
    	$("#searchAndPageForm").submit();
    }
	//-->
</script>

</head>
<body>
<h2>User 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchUser" modelAttribute="searchUser"
	action="${pageContext.request.contextPath}/web/user" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty users}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>ID</th>
            <th>名称</th>
            <th>昵称</th>
            <th>类型</th>
            <th>金币</th>
            <th>重置金币</th>
            <th>操作</th>
         </tr>
         <tr>
   <td colspan="7" style="text-align: center;">没有找到相关数据</td>
</tr>
      </table>
      </c:if>
 <c:if test="${!empty users}">
<display:table name="users" defaultsort="1"
	requestURI="/web/user" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="user">
	<display:column property="id" title="ID" />
	<display:column property="name" title="名称" />
	<display:column property="name" title="昵称" />
	<display:column title="状态">
		<c:choose>
			<c:when test="${user.type == 0 }">用户</c:when>
			<c:otherwise>机器人</c:otherwise>
		</c:choose>
	</display:column>
	<display:column property="userAccount.coin" title="金币" />
	<display:column property="userAccount.coinReset" title="重置金币" />
	<display:column title="操作">
		<a href="#" onclick="detailActivity('${user.id}');">查看动态</a>
		<a href="#" onclick="javascript:banAccount(${user.id});">是否封禁</a>
	</display:column>
</display:table></c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<form id="deleteForm" method="post"><input type="hidden"
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