<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>${name}</title>
</head>
<body>
<form action='${pageContext.request.contextPath}/misc/paypal/check.st' METHOD='POST' target="_top">
	<table>
                <tr>
                        <td width="300" align="right">
                                <img alt="Wheel Candy X3" src="${pageContext.request.contextPath}/${picture}">
                        </td>
                        <td>
                                <span style="font-size: 40px; color:#9900FF;">&nbsp;${name}</span><br/><br/>
                                <span style="font-size: 80px; color:#FF00FF;">&nbsp;${amount}</span><br/><br/>
                        </td>
                </tr>
                <tr>
                        <td colspan="2" align="left"><span style="font-size: 60px; color:#333333; float:left;">${description}</span></td>
                </tr>
                <tr>
                        <td colspan="2" align="center">
                                <input type='image' style="width:342px; height:94;" name='paypal_submit' id='paypal_submit'  src='https://www.paypal.com/en_US/i/btn/btn_dg_pay_w_paypal.gif' border='0' align='top' alt='Pay with PayPal'/>
                                <input type="hidden" name="custom" value="${custom}">
                                <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
                        </td>
                </tr>
        </table>
</form>
<script src='https://www.paypalobjects.com/js/external/dg.js' type='text/javascript'></script>
<script>

	var dg = new PAYPAL.apps.DGFlow(
	{
		trigger: 'paypal_submit',
		expType: 'instant'
		 //PayPal will decide the experience type for the buyer based on his/her 'Remember me on your computer' option.
	});

</script>
</body>
</html>
