package com.ngnsoft.ngp.web.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.component.candy.model.CandyDevice;
import com.ngnsoft.ngp.component.candy.model.PopularizeCount;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.UserManager;

/**
 *
 * @author fcy
 */
public class PopularizeNotify extends HttpServlet {

	private UserManager um;

	public void init() throws ServletException {
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
		try {
			JSONObject jsonObj = new JSONObject();
			Enumeration en = request.getParameterNames();
			while (en.hasMoreElements()) {
				String paramName = (String) en.nextElement();
				String paramValue = request.getParameter(paramName);
				jsonObj.put(paramName, paramValue);
			}
			PopularizeCount pCount = new PopularizeCount();
			if(jsonObj.getString("type").equals(PopularizeCount.TYPE_LOGIN)){
				pCount.setType(PopularizeCount.TYPE_LOGIN);
				pCount.setI1(Integer.parseInt(jsonObj.getString("i1")));
				pCount.setI2(Integer.parseInt(jsonObj.getString("i2")));
				CandyDevice cd = new CandyDevice();
				cd.setId(jsonObj.getString("device_id"));
				cd.setAppId(jsonObj.getString("app_id"));
				cd.setAppVersion(jsonObj.getString("app_version"));
				cd.setOsVersion(jsonObj.getString("os_version"));
				cd.setModel(jsonObj.getString("model"));
				
				//get DbName
				App app = um.get(jsonObj.getString("app_id"), App.class);
				cd.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName());
				
				um.saveIgnoreDke(cd);
			}
			if(jsonObj.getString("type").equals(PopularizeCount.TYPE_BUY)){
				pCount.setType(PopularizeCount.TYPE_BUY);
				pCount.setS1(jsonObj.getString("s1"));				
				pCount.setI1(Integer.parseInt(jsonObj.getString("i1")));
				pCount.setI2(Integer.parseInt(jsonObj.getString("i2")));
				pCount.setI3(Integer.parseInt(jsonObj.getString("i3")));
			}
			if(jsonObj.getString("type").equals(PopularizeCount.TYPE_ITEM)){
				pCount.setType(PopularizeCount.TYPE_ITEM);
				pCount.setI1(Integer.parseInt(jsonObj.getString("i1")));
				pCount.setI2(Integer.parseInt(jsonObj.getString("i2")));
				pCount.setI3(Integer.parseInt(jsonObj.getString("i3")));
			}
			if(jsonObj.getString("type").equals(PopularizeCount.TYPE_RESULT)){
				pCount.setType(PopularizeCount.TYPE_RESULT);
				pCount.setI1(Integer.parseInt(jsonObj.getString("i1")));
				pCount.setI2(Integer.parseInt(jsonObj.getString("i2")));
				pCount.setI3(Integer.parseInt(jsonObj.getString("i3")));
				pCount.setI4(Integer.parseInt(jsonObj.getString("i4")));
			}
			pCount.setDeviceId(jsonObj.getString("device_id"));
			um.save(pCount);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
}
