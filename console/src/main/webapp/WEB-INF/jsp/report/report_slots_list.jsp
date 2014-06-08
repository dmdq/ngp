<%-- 
	模块名称 :
	功能名称 :
	创建人 : yangjinglei                  创建日期 : 2013-12-13
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

<script type="text/javascript">
	/**
	 * description : 页面初始回调函数
	 *
	 * author : system
	 * create on : 2010-6-17
	 * last modifier : 
	 */
	$(function() {
		
		systeminit();
		$("#searchBtn").button();
		
		$("#beginTime").datepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:''});
		$("#endTimes").datepicker({
			dateFormat: 'yy-mm-dd',
			timeFormat:''});
		
		// 点击“保存”按钮时先验证，验证通过后方能保存
		$("#searchBtn").click(function() {
				$("#searchForm").submit();
		});
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
 		if (init != 0)
 			$("#searchAndPageForm").submit();
 		init = 1;
 		return false;
 	}
	//-->
</script>

</head>
<body>
<h2>留存 列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchFileStorage" modelAttribute="searchFileStorage"
	action="${pageContext.request.contextPath}/web/report/slots/slotsRemain" method="post">
	<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>开始时间</th>
						<td>
							<input name="beginTime" id="beginTime" value="${beginTime}" class="required" readonly="readonly" size="50" maxlength="50"/>&nbsp;<font color="red">*</font>
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
<div style="width:1000px;height:20px;">&nbsp;</div>
<c:if test="${empty keeps}">
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>日期</th>
            <th>新增数量</th>
            <th>登录数量</th>
            <th>次日留存</th>
            <th>七日留存</th>
            <th>十五日留存</th>
         </tr>
         <tr>
   			<td colspan="6" style="text-align: center;">没有找到相关数据</td>
		 </tr>
      </table>
      </c:if>
<c:if test="${!empty keeps}">
<display:table name="keeps" defaultsort="1" class="datagrid1"
	style="width:80%;" defaultorder="descending" id="keep" >
	<display:column title="日期">
		<fmt:formatDate value="${keep.createTime}" pattern="yyyy-MM-dd" />
	</display:column>
	<display:column property="insNumber" title="新增数量" />
	<display:column property="logNumber" title="登录数量"/>
	<display:column title="次日留存">
		<c:if test="${!empty keep.tomKeep && keep.tomKeep != 0}">
			<fmt:formatNumber value="${((keep.tomKeep*1.0)/(keep.insNumber*1.0))}" type="currency" pattern="##0.00%"/>
		</c:if>
	</display:column>
	<display:column title="七日留存">
		<c:if test="${!empty keep.sevKeep && keep.sevKeep != 0}">
			<fmt:formatNumber value="${((keep.sevKeep*1.0)/(keep.insNumber*1.0))}" type="currency" pattern="##0.00%"/>
		</c:if>
	</display:column>
	
	<display:column title="十五日留存">
		<c:if test="${!empty keep.fiftKeep && keep.fiftKeep != 0}">
			<fmt:formatNumber value="${((keep.fiftKeep*1.0)/(keep.insNumber*1.0))}" type="currency" pattern="##0.00%"/>
		</c:if>
	</display:column>
</display:table>
</c:if>
<div style="width: 1000px; height: 20px;">&nbsp;</div>
		<div id="Pagination" style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<div class="tinbox">
</div>
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/report/slots/slotsRemain" method="post">
		<input type="hidden" value="${startTime }" name="beginTime" id="beginTime" />
		<input type="hidden" value="${endTimes }" name="endTimes" id="endTimes" />
	</form>
</body>
</html>