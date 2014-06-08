<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-5-6
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
	----------------------------------------------------------------------
	修改人 :                          修改日期 :
	修改内容 :
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/head.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
<script type="text/javascript" src="<c:url value='/js/pms/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/pms/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.8.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/pagination/jquery.pagination.js'/>"></script>

<script type="text/javascript">
	$(function() {
		systeminit();
		$("#addBtn").button();
		$("#searchBtn").button();
	});

	function systeminit() {
	}

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

    function selectEngines(field, engineName) {
    	var src = window.parent.document.getElementById("resUpdateNames");
		var option;
		var flag;
    	if(field.checked) {
       	    if(src.length == 0){
       	    	option = new Option(engineName, engineName);
       			src.options[src.length] = option;
       	    }else{
        		loop: for(var j = 0; j < src.length; j++){
        			if (src.options[j].text == engineName) {
        				flag = 0;
        				break loop;
        			} else {
        				flag = 1;
        			}
        		}
        		if(flag == 1){
        			option = new Option(engineName, engineName);
       				src.options[src.length] = option;
        		}
       		}
       	 	field.checked = true;
    	} else {
    		if(src.length > 0){
        		loop: for(var j = 0; j < src.length; j++){
        			if (src.options[j].text == engineName) {
        				src.options.remove(j);
        				break loop;
        			} 
        		}
       		}
       	 	field.checked = false;
    	}
	    return true;
	}

	function setPage() {
		document.getElementById("current_page").value = 0;
    }
	
	function checkAll(field) {
		if(field.checked) {
			$("#showTable").find("input[type='checkbox']").each(function() {
	            $(this).attr("checked", true);
	            if($(this).attr("id") != "") {
	                selectEngines($(this)[0], $(this).attr("id"));
	            }
	        });
		} else {
			$("input[name='checkbox']").attr("checked", false);
			var src = window.parent.document.getElementById("resUpdateNames");
			src.options.length = 0;
		}
	}
</script>

</head>
<body>
<div style="width: 500px; height: 500px;margin: 0px auto;">
<h2>ResFile 列表</h2>
<form:form id="searchForm" commandName="searchFileStorage" modelAttribute="searchFileStorage"
	action="${pageContext.request.contextPath}/web/resFile/selectResFiles" method="post">
	<input type="hidden" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
<table class="datagrid1" style="width:80%;">
<thead>
<tr>
<th width="30%">名称</th>
<td>
	<form:input path="name" size="40" maxlength="50"/>
</td>
</tr>
</thead>
</table>
<div style="width:100%;text-align: center;margin-top:5px;">
<input id="searchBtn" onclick="setPage();" type="submit" value="查询"/>
</div>
</form:form>
<div style="width:1000px;height:20px;">&nbsp;</div>
<!-- 内容区域 -->
<table  class="datagrid1" style="width:80%;" id="showTable">
  <tr>
     <th>选择<input type="checkbox" name="checkbox" onclick="checkAll(this)"/></th>
     <th>名称</th>
     <th>URL</th>
  </tr>
  <c:forEach items="${resFileShows }" var="resFileShow" varStatus="">
   <tr>
      <td>
         <c:choose>
         	<c:when test="${resFileShow.select}"><input type="checkbox" name="checkbox" id="${resFileShow.urn }"  checked="checked" onclick="selectEngines(this,'${resFileShow.urn }');"/></c:when>
         	<c:otherwise><input type="checkbox" name="checkbox" id="${resFileShow.urn }" onclick="selectEngines(this,'${resFileShow.urn }');"/></c:otherwise>
         </c:choose>
      </td>
      <td>${resFileShow.name }</td>
      <td>${resFileShow.urn}</td>
   </tr>
   </c:forEach>
</table>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div id="Pagination" style="width:80%"></div>
</div>
</body>
</html>