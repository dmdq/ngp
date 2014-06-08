package com.ngnsoft.ngp.web.servlet.paypal;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.PaypalOrder;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.service.UserManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fcy
 */
public class PaypalNotify extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(PaypalNotify.class);

	private UserManager um;

	@Override
	public void init() throws ServletException {
		super.init();
		um = Engine.getInstance().getCtx().getBean(UserManager.class);
	}

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String paymentStatus = request.getParameter("payment_status");
		// check that paymentStatus=Completed
		if (!"Completed".equals(paymentStatus)) {
			return;
		}
		JSONObject jo = new JSONObject();
		// read post from PayPal system and add 'cmd'
		Enumeration en = request.getParameterNames();
		StringBuilder sb = new StringBuilder("cmd=_notify-validate");
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			sb.append("&").append(paramName).append("=").append(URLEncoder.encode(paramValue, "UTF-8"));
			jo.put(paramName, paramValue);
		}
		String ipnData = sb.toString();
		if (paypalVerify(ipnData)) {
			String custom = request.getParameter("custom");
			String[] cs = custom.split("_");
			String baseId = cs[0];
			String dbName = AppComponentFactory.getComponent(baseId).getDbName();

			// check that txn_id has not been previously processed
			String txnId = request.getParameter("txn_id");
			PaypalOrder existOrder = new PaypalOrder();
			existOrder.setDbName(dbName);
			existOrder.setTxnId(txnId);
			existOrder = um.findObject(existOrder);
			if (existOrder != null) {
				logger.warn("DUPL:" + txnId + "," + ipnData);
				return;
			}

			PaypalOrder payOrder = new PaypalOrder(jo);
			payOrder.setDbName(dbName);
			// check that receiverEmail is your Primary PayPal email
			String receiverEmail = "lzhang@ngnentertainment.com";
			if (!receiverEmail.equals(payOrder.getReceiverEmail())) {
				logger.warn("RECEIVER:"+ipnData);
				return; 
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
			um.save(sale);
			um.save(payOrder);
		} else {
			logger.warn("INVALID:"+ipnData);
		}

	}

	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "PaypalNotify";
	}

	private boolean paypalVerify(String ipnData) throws MalformedURLException, IOException {

		//This is the URL of the REST webservice in paypal
		URL url = new URL("https://www.paypal.com/cgi-bin/webscr");

		//make connection, use post mode
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		//Write the JSON query object to the connection output stream
		PrintStream ps = new PrintStream(connection.getOutputStream());
		ps.print(ipnData);
		ps.close();

		//Call the service
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		//Extract response
		String ret = br.readLine();
		br.close();
		if (("VERIFIED").equals(ret.trim())) {
			return true;
		} else {
			return false;
		}
	}
}
