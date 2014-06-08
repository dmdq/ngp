<%-- 
	模块名称 :
	功能名称 :
	创建人 : limingjun                  创建日期 : 2013-4-25
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
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/jquery-ui-1.10.3.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/pagination/pagination.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/theme/img_list.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-timepicker-addon.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/AWCS.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/loading.css'/>" />

<style type="text/css">
<!--
a {
	color: blue;
	cursor: pointer;
	text-decoration: underline;
}

div.instructions_container {
	float: left;
	width: 350px;
	margin-right: 50px;
}

div#chartContainer {
	width: 600px;
	height: 300px;
}

div.example_links 
         .link_category {
	margin-bottom: 15px;
}

.loading-indicator-bars {
	background-image: url('images/loading-bars.gif');
	width: 150px;
}

label.error {
    color: red;
    font-weight: bold;
    padding-left: 20px;
  }
-->
</style>

<!-- JAVASCRIPT AREA -->
<c:url value='' />
<script type="text/javascript" src="<c:url value='/js/head.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/lib.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.10.2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/jquery-ui-1.10.3.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.validate.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-ui-sliderAccess.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-ui-timepicker-addon.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/i18n/jquery-ui-timepicker-zh-CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/jquery.metadata.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/external/messages_cn.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/jquery.cookie.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/ui/external/qunit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery/jquery.showLoading.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/chart/FusionCharts.js'/>"></script>

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
		$("#searchBtn").button();
		
		$("#beginTime").datepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:''});
		
		$("#endTimes").datepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:''});
		
		//customer
		showConditionalMessage( "Your browser does not seem to have Flash Player support. JavaScript chart is rendered instead.", isJSRenderer(myChart) );
		
		// 点击“保存”按钮时先验证，验证通过后方能保存
		$("#searchBtn").click(function() {
				searchSubmit();
		});
	});

	function systeminit() {
	}
	
	var myChart = new FusionCharts("${pageContext.request.contextPath}/charts/Column3D.swf", "myChartId", "600", "500", "0", "1");
	
	function searchSubmit() {
		$('#chartContainer').showLoading();
		var url = '<c:url value="/web/report/coc/queryNewUser"/>';
    	$.ajax({ 
			type: "POST", 
			dataType: "json",
			data: { beginTime:$("#beginTime").val(),endTimes:$("#endTimes").val()},
			url: url, 
			cache: false,
			error: function(){
	       	 	alert('Error');
	    	},
	    	success: function(data){ 
	    		setTimeout( "$('#chartContainer').hideLoading()", 0);
	            myChart.setJSONData(data.data);
	            myChart.render("chartContainer");
	    	}
		});
	}
	
	  

//-->
</script>

</head>
<body>
	<h2>每日新增用户</h2>
	<!-- 内容区域 -->
	<div id="content" align="center">
		<form:form id="searchForm" commandName="searchPlacard"
			modelAttribute="searchPlacard"
			action="${pageContext.request.contextPath}/web/placard" method="post">
			<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>开始时间</th>
						<td>
							<input name="beginTime" id="beginTime" value="${beginTime}" readonly="readonly" size="50" maxlength="50"/>&nbsp;<font color="red">*</font>
							<img src="${pageContext.request.contextPath}/images/reload_16.png" alt="清除" class="refreshBtn" onclick="$('#beginTime').val('');">
						</td>
						<th>结束时间</th>
						<td>
							<input name="endTimes" id="endTimes" value="${endTime}" readonly="readonly" size="50" maxlength="50"/>&nbsp;<font color="red">*</font>
							<img src="${pageContext.request.contextPath}/images/reload_16.png" alt="清除" class="refreshBtn" onclick="$('#endTimes').val('');">
						</td>
					</tr>
				</thead>
			</table>
			<div style="width: 100%; text-align: center; margin-top: 5px;">
				<input id="searchBtn" type="button" value="查询" />
			</div>
		</form:form>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<div id="chartContainer">Charts will load here</div><br/>
		<div style="width: 1000px; height: 20px;">&nbsp;</div>
	</div>
	<br>
	<br>
	<!-- 按钮区域 -->

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