package com.ngnsoft.ngp.web.servlet.paypal;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.component.candy.CandyComponent;
import com.ngnsoft.ngp.model.Product;
import com.ngnsoft.ngp.service.UserManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fcy
 */
public class ProductPage extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(ProductPage.class);

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
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.length() == 0) return;
		if (pathInfo.endsWith("/")) {
			pathInfo = pathInfo.substring(1, pathInfo.length()-1);
		} else {
			pathInfo = pathInfo.substring(1);
		}
		String[] params = pathInfo.split("/");
		String baseId;
		String appId;
		String device;
		String pid;
		if (params.length == 2) {
			baseId = CandyComponent.APP_BASE_ID;
			appId = "com.toktoo.candymatch";
			device = params[0];
			pid = params[1];
		} else if (params.length == 4) {
			baseId = params[0];
			appId = params[1];
			device = params[2];
			pid = params[3];
		} else {
			request.getRequestDispatcher("/error.html").forward(request, response);
			return;
		}
		String dbName = AppComponentFactory.getComponent(baseId).getDbName();
		Product findProduct = new Product();
		findProduct.setProductId(pid);
		findProduct.setDbName(dbName);
		Product p = um.findObject(findProduct);
		if (p == null) {
			request.getRequestDispatcher("/paypal/" + baseId + "/" + "nop.html").forward(request, response);
			return;
		}
		String custom = baseId + "_" + appId + "_" + device + "_" + pid;
		request.setAttribute("custom", custom);
		request.setAttribute("picture", p.getPicture());
		request.setAttribute("name", p.getName());
		request.setAttribute("amount", p.getAmount());
		request.setAttribute("description", p.getDescription());
		request.getRequestDispatcher("/paypal/" + baseId + "/" + "product.jsp").forward(request, response);
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
		return "Product Page";
	}
}
