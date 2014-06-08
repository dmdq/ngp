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
			} else if (msg == "same") {
				alert("promoId已存在，操作失败！");
			} else if (msg == "failed") {
				alert("操作失败！");
			} else if(msg == 'duplication') {
				alert("idx重复，操作失败！");
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
    
    function newPromo(){
    	resetParams();
        var url = '<c:url value="/web/promo/toAdd"/>';
        $("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
       }

    function searchSubmit(){
    	$("#current_page").val(0);
    	resetParams();
    	$("#searchAndPageForm").submit();
    }

    function edit(promoId){
    	var url = '<c:url value="/web/promo/"/>' + promoId + "/toUpdate";
    	resetParams();
        $("#searchAndPageForm").attr("action", url);
        $("#searchAndPageForm").submit();
    }

    var o = 0;
    
    function OnclickValue(order){
    	o = order.value;
    }
    
    function update(field, promoId) {
    	var order = field.value;
		var regex = /^[0-9]*[1-9][0-9]*$/;
		if(order == "" || !order.match(regex)) {
			alert("必须输入正整数");
			$("#order"+promoId).val(o);
		} else {
			var url = "<c:url value='/web/promo/update/' />" + promoId +"/" +order;
			$.ajax({ 
				type: "GET", 
				dataType: "text",
				url: url,
				cache: false,
				error: function(){
					$("#order"+promoId).val(order);
		       	 	alert('Error');
		    	},
		    	success: function(msg){
		    		if(msg == 'success') {
		    			alert('修改成功！');
		    		} else if(msg == 'duplication') {
		    			$("#order"+promoId).val(o);
		    			alert('该序列号已存在！');
		    		}
		    	}
			});
		}
    }
    
    
    function resetParams(){
    	$("#p_id").val($("#id").val());
    }
    
    function refresh(){
    }
    
	//-->
</script>

</head>
<body>
<h2>Promo 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchPromo" modelAttribute="searchPromo"
	action="${pageContext.request.contextPath}/web/promo" method="post">
<table class="datagrid1" align="center" style="width:80%;">
<thead>
<tr>
<th>Name</th>
<td>
	<select name="id" id="id" style="width: 280px;">
		<option value="">----------请选择----------</option>
		<c:forEach items="${ps}" var="promo">
			<option <c:if test='${promo.id == searchPromo.id}'>selected</c:if> value="${promo.id}">${promo.name}</option>
		</c:forEach>
	</select>
</td>
</thead>
</table>
<div style="width:100%;text-align: center;margin-top:5px;">
<input id="searchBtn" onclick="searchSubmit();" type="submit" value="查询"/>
</div>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty promos}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>Icon</th>
            <th>name</th>
            <th>Link</th>
            <th>CreateTime</th>
            <th>操作</th>
         </tr>
         <tr>
   <td colspan="5" style="text-align: center;">没有找到相关数据</td>
</tr>
      </table>
      </c:if>
 <c:if test="${!empty promos}">
<display:table name="promos" defaultsort="1"
	requestURI="/web/promo" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="promo">
	<display:column title="Icon">
		<img src="<c:url value='${dict:getAbsolutePath(promo.icon)}' />" />
	</display:column>
	<display:column property="name" title="Name" />
	<display:column property="link" title="Link" />
	<display:column title="Index">
		<input type="text" name="order" id="order${promo.id}" onclick="OnclickValue(this);" value="${promo.idx }" onfocus="this.select();" onblur="update(this, ${promo.id});" style="width: 30px;"/>
	</display:column>
	<display:column title="CreateTime" >
		<fmt:formatDate value="${promo.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
	</display:column>
	<display:column title="操作">
		<a href="#" onclick="edit('${promo.id}');">编辑</a>
	</display:column>
</display:table></c:if>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<div class="tinbox">
<button id="addBtn" onclick="javascript:newPromo();">新增</button>
</div>
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
<div id="uploadDIV" title="图片详细" style="display: none;">
	<iframe id="upload-iframe" scrolling="yes" name="upload-iframe" src=""  width="100%" height="100%" frameborder="0"></iframe>
</div>
	
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/promo" method="post">
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