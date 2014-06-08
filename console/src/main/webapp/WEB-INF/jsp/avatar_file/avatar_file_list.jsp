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
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.bgiframe-2.1.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/qunit.js'/>"></script>
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
		
		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "failed") {
				alert("操作失败！");
			}
		}
		
		//system
		systeminit();
		$("#addBtn").button();
		$("#searchBtn").button();
	});

	function systeminit() {
	}

	function del(id) {
		if (confirm("确定要删除该记录吗？")) {
	    	var url = '<c:url value="/web/avatarFile/del/"/>' + id;
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

    function newAvatarFile(url) {
    	$("#upload-iframe").attr("src", url);
		$("#uploadDIV").dialog({
			resizable : true,
			height : 450,
			width : 600,
			autoOpen : true,
			modal : true,
			open: function() {
			},
			close: function() {
				location.href='<c:url value="/web/avatarFile/"/>';
			},
			buttons: {
				Ok: function() {
					$(this).dialog('close');
					location.href='<c:url value="/web/avatarFile/"/>';
				}
			}
				
		}); 
	}
	//-->
</script>

</head>
<body>
<h2>Avatar 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchFileStorage" modelAttribute="searchFileStorage"
	action="${pageContext.request.contextPath}/web/avatarFile" method="post">
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty avatarFiles}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>Name</th>
            <th>URL</th>
            <th>Image</th>
            <th>操作</th>
         </tr>
         <tr>
   <td colspan="4" style="text-align: center;">没有找到相关数据</td>
</tr>
      </table>
      </c:if>
 <c:if test="${!empty avatarFiles}">
<display:table name="avatarFiles" defaultsort="1"
	requestURI="/web/avatarFile" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="avatarFile">
	<display:column property="name" title="Name" />
	<display:column title="URL">
		<c:out value="${dict:getAbsolutePath(avatarFile.urn)}"></c:out>		
	</display:column>
	<display:column title="Image">
		<img style="widows: 100px;height: 100px;" src="<c:url value='/web/download${avatarFile.urn}'/>"/>
	</display:column>
	<display:column title="操作">
		<a href="#" onclick="javascript:del('${avatarFile.id}');">删除</a>
	</display:column>
</display:table></c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<div class="tinbox">
<button id="addBtn" onclick="newAvatarFile('<c:url value='/web/avatarFile/selectPhotos'/>');">上传图片</button>
</div>
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
	
<form id="searchAndPageForm" action="" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
</form>
<div id="uploadDIV" title="上传图片" style="display: none;">
	<iframe id="upload-iframe" scrolling="no" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
</body>
</html>