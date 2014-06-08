package com.ngnsoft.ngp.web.servlet.paypal;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.PaypalIndent;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.service.UserManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderConfirm extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(OrderConfirm.class);

	private UserManager um;

	@Override
	public void init() throws ServletException {
		super.init();
		um = Engine.getInstance().getCtx().getBean(UserManager.class);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String token = request.getParameter("token");
		String payerId = request.getParameter("PayerID");
		String custom = request.getParameter("custom");
		if (token != null) {
			PaypalFunctions ppf = new PaypalFunctions();
			HashMap nvp = ppf.getPaymentDetails(token);

			String strAck = nvp.get("ACK").toString();
			String finalPaymentAmount = null;
			String perEmail = null;
			if (strAck != null && (strAck.equalsIgnoreCase("Success") || strAck.equalsIgnoreCase("SuccessWithWarning"))) {
				finalPaymentAmount = nvp.get("AMT").toString();
				perEmail = nvp.get("EMAIL").toString();
			}
			String serverName = request.getServerName();
			Map item = new HashMap();
			item.put("name", nvp.get("L_PAYMENTREQUEST_0_NAME0"));
			item.put("amt", nvp.get("L_PAYMENTREQUEST_0_AMT0"));
			item.put("qty", nvp.get("L_PAYMENTREQUEST_0_QTY0"));
			nvp = ppf.confirmPayment(token, payerId, finalPaymentAmount, serverName, item);
			strAck = nvp.get("ACK").toString();
			if (strAck != null
					&& (strAck.equalsIgnoreCase("Success") || strAck
							.equalsIgnoreCase("SuccessWithWarning"))) {
				String transactionId = nvp.get("PAYMENTINFO_0_TRANSACTIONID").toString(); // '
				String transactionType = nvp.get("PAYMENTINFO_0_TRANSACTIONTYPE").toString(); // '
				String paymentType = nvp.get("PAYMENTINFO_0_PAYMENTTYPE").toString(); // '
				String orderTime = nvp.get("PAYMENTINFO_0_ORDERTIME").toString(); // '
				String amt = nvp.get("PAYMENTINFO_0_AMT").toString(); // ' The final amount
				String currencyCode = nvp.get("PAYMENTINFO_0_CURRENCYCODE").toString(); // ' A
				String feeAmt = nvp.get("PAYMENTINFO_0_FEEAMT").toString(); // ' PayPal fee
				String taxAmt = nvp.get("PAYMENTINFO_0_TAXAMT").toString(); // ' Tax charged
				String paymentStatus = nvp.get("PAYMENTINFO_0_PAYMENTSTATUS").toString();
				String pendingReason = nvp.get("PAYMENTINFO_0_PENDINGREASON").toString();
				String reasonCode = nvp.get("PAYMENTINFO_0_REASONCODE").toString();

				JSONObject jo = new JSONObject();
				jo.put("eamil", perEmail);
				jo.put("AMT", amt);
				jo.put("payerId", payerId);
				jo.put("custom", custom);
				jo.put("transactionId", transactionId);
				jo.put("TIMESTAMP", nvp.get("TIMESTAMP").toString());
				jo.put("quantity", item.get("qty"));
				jo.put("name", item.get("name"));
				// read post from PayPal system and add 'cmd'
				String[] cs = custom.split("_");
				String baseId = cs[0];
				String dbName = AppComponentFactory.getComponent(baseId).getDbName();

				// check that txn_id has not been previously processed
				PaypalIndent existIndent = new PaypalIndent();
				existIndent.setDbName(dbName);
				existIndent.setToken(token);
				existIndent = um.findObject(existIndent);
				if (existIndent != null) {
					logger.warn("DUPL:" + token);
					return;
				}
				PaypalIndent payIndent = new PaypalIndent(jo);
				payIndent.setDbName(dbName);
				// check that receiverEmail is your Primary PayPal email
				if (ppf.bSandbox) {
					payIndent.setReceiverEmail(PaypalFunctions.sandReceiverEmail);
				} else {
					payIndent.setReceiverEmail(PaypalFunctions.receiverEmail);
				}

				// process payment
				String appId = cs[1];
				String deviceStr = cs[2];
				Request req = new Request();
				req.setMacId(deviceStr);
				req.setIdfa(deviceStr);
				Device device = um.getDevice(req);
				if (device == null) {
					device = new Device();
					device.setMacId(deviceStr);
					device.setIdfa(deviceStr);
					um.save(device);
				}
				String deviceId = device.getId();
				int pidIndex = baseId.length() + appId.length() + deviceStr.length() + 3;
				String pid = custom.substring(pidIndex);
				Sale sale = new Sale();
				sale.setDbName(dbName);
				sale.setAppId(appId);
				sale.setDeviceId(deviceId);
				sale.setAd("paypal");
				sale.setIp(request.getRemoteAddr());
				sale.setJsonData(jo.toString());
				sale.setPid(pid);
				um.save(payIndent);
				um.save(sale);

				response.setContentType("text/html");
				response.getWriter().println("<script>\n alert(\"Payment was successful. Please return to the game and enjoy it.\");\n window.onload = function(){\nif(window.opener){\nwindow.close();\n}\nelse{\nif(top.dg.isOpen() == true){\ntop.dg.closeFlow();\nreturn true;\n}\n}\n};\n</script>");
			} else {
				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();
				logger.error("OrderConfirm:"+ErrorCode + ", " + ErrorSeverityCode + ", " + ErrorLongMsg);
				response.getWriter().println("<script>\n alert(\"Payment failed. Please try again later.\");\n window.onload = function(){\nif(window.opener){\nwindow.close();\n}\nelse{\nif(top.dg.isOpen() == true){\ntop.dg.closeFlow();\nreturn true;\n}\n}\n};\n</script>");
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
