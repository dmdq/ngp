package com.ngnsoft.ngp.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.AdTrack;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.service.UserManager;

/**
 *
 * @author fcy
 */
public class AdTrackNotify extends HttpServlet {

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
		String appId = request.getParameter("app_id");
		String adAppid = request.getParameter("ad_appid");
		String adAction = request.getParameter("ad_action");
		String deviceId = request.getParameter("device_id");

		Request rq = new Request();
		rq.setIdfa(deviceId);
		rq.setMacId(deviceId);
		Device device = um.getDevice(rq);

		AdTrack adTrack = new AdTrack();
		adTrack.setAdAction(adAction);
		adTrack.setAppId(appId);
		adTrack.setDeviceId(device.getId());
		adTrack.setAdAppid(adAppid);
		
		//get DbName
		App app = um.get(appId, App.class);
		adTrack.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName());
		
		um.save(adTrack);
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
