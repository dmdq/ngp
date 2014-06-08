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

.specBtn:HOVER {
	cursor: pointer;
	color: red;
}

.refreshBtn:HOVER {
	cursor: pointer;
}

.a1{
	color : red;   
	font-style:italic;
	font-size:20px;
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
			
			systeminit();
			
			var a = "<c:out value='${a}'/>";
			
			if (a == 'today') {
				$("#today").css({"color":"red"});
				$("#today").css({"font-size":"20px"});
			} else if (a == 'yesterday') {
				$("#yesterday").css({"color":"red"});
				$("#yesterday").css({"font-size":"20px"});
			} else if (a == 'sevendays') {
				$("#sevendays").css({"color":"red"}); 
				$("#sevendays").css({"font-size":"20px"});
			} else if (a == 'fifteenthdays') {
				$("#fifteenthdays").css({"color":"red"});
				$("#fifteenthdays").css({"font-size":"20px"});
			} else if (a == 'february') {
				$("#february").css({"color":"red"});
				$("#february").css({"font-size":"20px"});
			} else if (a == 'march') {
				$("#march").css({"color":"red"});
				$("#march").css({"font-size":"20px"});
			} else if (a == 'june') {
				$("#june").css({"color":"red"});
				$("#june").css({"font-size":"20px"});
			} else if (a == 'all') {
				$("#all").css({"color":"red"});
				$("#all").css({"font-size":"20px"});
			}
			
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
	     * @param {jQuery} jq the container with t
	     he pagination links as a jQuery object
	     */
	    var init = 0;
	 	function pageselectCallback(page_index, jq) {
	 		// Get number of elements per pagionation page from form
	 		$('#current_page').val(page_index);
	 		if (init != 0)
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
	 	
	 	function searchSubmit(time){
	 		var url = '<c:url value="/web/order/"/>' + time + "/slotsOrder";;
	        $("#searchForm").attr("action", url);
	        $("#searchForm").submit();
	 	}
	 	
	 	function listByUserId(userId) {
	 		var url = '<c:url value="/web/order/"/>' + userId + "/byUserIdSlots";
	        $("#searchForm").attr("action", url);
	        $("#searchForm").submit();
	 	}
	//-->
</script>

</head>
<body>
<h2>充值明细  列表</h2>
<!-- 内容区域 -->
<div id="content" align="center">
<form:form id="searchForm" commandName="searchApp" modelAttribute="searchApp"
	action="${pageContext.request.contextPath}/web/order/null/slotsOrder" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
<table class="datagrid1" align="center" style="width: 80%;">
				<thead>
					<tr>
						<th>appId</th>
						<td>
							<select name="appId" style="width: 240px;">
								<option value="">------请选择-------</option>
								<c:forEach items="${apps}" var="app">
									<option <c:if test="${app.id == appId}">selected</c:if> value="${app.id}">${app.name}(${app.id})</option>
								</c:forEach>
							</select>
						</td>
						<th>开始时间</th>
						<td>
							<input name="beginTime" id="beginTime" value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd" />" class="required" readonly="readonly" size="50" maxlength="50"/>&nbsp;<font color="red">*</font>
							<img src="${pageContext.request.contextPath}/images/reload_16.png" alt="清除" class="refreshBtn" onclick="$('#beginTime').val('');">
						</td>
						<th>结束时间</th>
						<td>
							<input name="endTimes" id="endTimes" value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd" />" readonly="readonly" size="50" maxlength="50"/>&nbsp;<font color="red">*</font>
							<img src="${pageContext.request.contextPath}/images/reload_16.png" alt="清除" class="refreshBtn" onclick="$('#endTimes').val('');">
						</td>
					</tr>
					<tr>
						<td colspan="6" style="text-align:center">
							<a class="specBtn" id="today" onclick="searchSubmit('today')">今天</a>&nbsp;&nbsp;
							<a class="specBtn" id="yesterday" onclick="searchSubmit('yesterday')">昨天</a>&nbsp;&nbsp;
							<a class="specBtn" id="sevendays" onclick="searchSubmit('sevendays')">七日</a>&nbsp;&nbsp;
							<a class="specBtn" id="fifteenthdays" onclick="searchSubmit('fifteenthdays')">十五日</a>&nbsp;&nbsp;
							<a class="specBtn" id="february" onclick="searchSubmit('february')">一个月</a>&nbsp;&nbsp;
							<a class="specBtn" id="march" onclick="searchSubmit('march')">三个月</a>&nbsp;&nbsp;
							<a class="specBtn" id="june" onclick="searchSubmit('june')">六个月</a>&nbsp;&nbsp;
							<a class="specBtn" id="all" onclick="searchSubmit('all')">全部</a>&nbsp;&nbsp;
						</td>
					</tr>
				</thead>
			</table>
			<div style="width: 100%; text-align: center; margin-top: 5px;">
				<input id="searchBtn" type="button" value="查询" />
			</div>
</form:form>
&nbsp;&nbsp;
<table class="datagrid1" style="width:80%;">
		<tr>
			<th style="width:20%;">汇总金额</th>
         	<td style="width:20%;">${sumIosOrder }</td>
         	<th style="width:20%;">当前汇总</th>
         	<td style="width:20%;">${sumIosOrderByApp}</td>
		</tr>
		<tr>
			<th style="width:20%;">当前人次数</th>
         	<td style="width:20%;">${population }</td>
         	<th style="width:20%;">当前总条数</th>
         	<td style="width:20%;">${totalNum}</td>
		</tr>
</table>
<div style="width:1000px;height:20px;">&nbsp;</div>
	
      <table class="datagrid1" style="width:80%;">
         <tr>
            <th>用户编号</th>
            <th>用户名称</th>
            <th>当前金币</th>
            <th>当前等级</th>
            <th>充值金额</th>
            <th>充值累计</th>
            <th>充值时间</th>
            <th>游戏创建时间</th>
         </tr>
         <tr>
			<c:if test="${empty orders}">
			   	<td colspan="8" style="text-align: center;">没有找到相关数据</td>
	      	</c:if>
			<c:if test="${!empty orders}">
					<c:forEach items="${orders}" var="entry">
					<tr>
			 		 	<td><a href="javascript:listByUserId(${entry.userId})">${entry.userId}</a></td>
			 		 	<td><a href="javascript:listByUserId(${entry.userId})">${entry.nick}</td>
			 		 	<td>${entry.coins}</td>
			 		 	<td>${entry.level}</td>
			 		 	<td>${entry.amount}</td>
			 		 	<td>${entry.count}</td>
			 		 	<td>
			 		 		<fmt:formatDate value="${entry.create_time}" pattern="yyyy-MM-dd HH:mm:ss" />
			 		 	</td>
			 		 	<td>
			 		 		<fmt:formatDate value="${entry.create_time1}" pattern="yyyy-MM-dd HH:mm:ss" />
			 		 	</td>
			 		 </tr>
			 	</c:forEach>
			</c:if>
		 </tr>
      </table>
<div style="width:1000px;height:20px;">&nbsp;</div>
<div  id="Pagination"  style="margin-left: 10%;"></div>
</div>
<br><br>
<!-- 按钮区域 -->
<div class="tinbox">
</div>
<form id="deleteForm" method="post"><input type="hidden"
	name="_method" value="delete" /></form>
	
<form id="searchAndPageForm" action="${pageContext.request.contextPath}/web/order/null/slotsOrder" method="post">
	<input type="hidden" id="pageFormmethod" name="_method" value="get" />
	<input type="hidden" value="<c:out value="${page.items_per_page }"/>" name="items_per_page" id="items_per_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_display_entries }"/>" name="num_display_entries" id="num_display_entries" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.current_page }"/>" name="current_page" id="current_page" class="numeric"/>
	<input type="hidden" value="<c:out value="${page.num_edge_entries }"/>" name="num_edge_entries" id="num_edge_entries" class="numeric"/>
	<input type="hidden" value="Prev" name="prev_text" id="prev_text"/>
	<input type="hidden" value="Next" name="next_text" id="next_text"/>
</form>
</body>
</html>