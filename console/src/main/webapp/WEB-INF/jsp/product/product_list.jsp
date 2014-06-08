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
		//system
		systeminit();
		$("#addBtn").button();
		$("#searchBtn").button();

		//customer

		var msg = '<c:out value="${msg}"/>';
		if (msg != '') {
			if (msg == "success") {
				alert("操作成功！");
			} else if (msg == "exist") {
				alert("productId已存在，操作失败！");
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
        resetParams();
        $('#current_page').val(page_index);
        if(init != 0)
       		 $("#searchAndPageForm").submit();
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
    
    function newProduct(){
        var url = '<c:url value="/web/product/toAdd"/>';
        resetParams();
        $("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
       }

    function searchSubmit(){
    	$("#current_page").val(0);
    	resetParams();
    	$("#searchAndPageForm").submit();
    }

    function edit(productId){
    	var url = '<c:url value="/web/product/"/>' + productId + "/toUpdate";
    	resetParams();
        $("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
    }
    
    function del(productId){
    	if (confirm("是否删除该条信息？")) {
	    	var url = '<c:url value="/web/product/"/>' + productId + "/del";
	    	resetParams();
    	    $("#searchAndPageForm").attr("action", url);
        	$("#searchAndPageForm").submit();
    	}
    }
    
    function resetParams(){
    	$("#p_id").val($("#productId").val());
    }
	//-->
</script>

</head>
<body>
<h2>SlotsProduct 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchSlotsProduct" modelAttribute="searchSlotsProduct"
	action="${pageContext.request.contextPath}/web/product" method="post">
<table class="datagrid1" align="center" style="width:80%;">
<thead>
<tr>
<th>编号</th>
<td>
	<input style="width:280px;" type="text" id="productId" name="productId" value="${productId}"/>
</td>
</thead>
</table>
<div style="width:100%;text-align: center;margin-top:5px;">
<input id="searchBtn" onclick="searchSubmit();" type="submit" value="查询"/>
</div>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty products}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>编号</th>
            <th>描述</th>
            <th>价格</th>
            <th>操作</th>
         </tr>
         <tr>
   <td colspan="4" style="text-align: center;">没有找到相关数据</td>
</tr>
      </table>
      </c:if>
 <c:if test="${!empty products}">
<display:table name="products" defaultsort="1"
	requestURI="/web/product" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="product">
	<display:column property="productId" title="编号"/>
	<display:column property="description" title="描述" />
	<display:column property="amount" title="价格" />
	<display:column title="操作">
		<a href="#" onclick="edit('${product.productId}');">编辑</a>
		<a href="#" onclick="del('${product.productId}');">删除</a>
	</display:column>
</display:table></c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<div class="tinbox">
<button id="addBtn" onclick="javascript:newProduct();">新增</button>
</div>
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
<div id="uploadDIV" title="图片详细" style="display: none;">
	<iframe id="upload-iframe" scrolling="yes" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
	
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/product" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
	<input type="hidden" name="p_id" id="p_id"/>
</form>
</body>
</html>