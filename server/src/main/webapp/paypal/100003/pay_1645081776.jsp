<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rainbow Candy X3</title>
</head>
<body>
    <!-- item04_tire2_ActionCandyShiftHD0202 -->
<form action="${pageContext.request.contextPath}/misc/paypal/sec.st" method="post" target="_top">
	<table>
                <tr>
                        <td width="300" align="right">
                                <img alt="Rainbow Candy" src="${pageContext.request.contextPath}/images/item_4.png">
                        </td>
                        <td>
                                <span style="font-size: 40px; color:#9900FF;">&nbsp;Rainbow Candy X3</span><br/><br/>
                                <span style="font-size: 80px; color:#FF00FF;">&nbsp;$1.99</span><br/><br/>
<input type="hidden" name="NAME" value="Rainbow Candy X3" />
								<input type="hidden" name="paymentAmount" value="1.99" />
                        </td>
                </tr>
                <tr>
                        <td colspan="2" align="left"><span style="font-size: 60px; color:#333333; float:left;">Switching with other candy, all the candies with the same color of the switched candy will be crushed.</span></td>
                </tr>
                <tr>
                        <td colspan="2" align="center">
                                <input type="hidden" name="custom" value="${custom }">
                                <input type="image" style="width:342px; height:94;" src="https://www.paypal.com/en_US/i/btn/btn_buynowCC_LG.gif" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">
                                <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
                        </td>
                </tr>
        </table>
</form>

</body>
</html>