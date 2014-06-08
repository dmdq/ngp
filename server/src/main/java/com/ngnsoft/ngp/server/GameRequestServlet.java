package com.ngnsoft.ngp.server;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fcy
 */
public class GameRequestServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(GameRequestServlet.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        OutputStream gout = response.getOutputStream();
        if (request.getParameter("nc") != null) {
            response.setContentType("application/json");
        } else {
        	if(request.getParameter("rq") != null && request.getParameter("rq").equals("ad")) {
        		response.setContentType("text/plain");
        	} else {
        		response.setContentType("application/x-gzip");
                response.addHeader("Content-Encoding", "gzip");
                gout = new ZOutputStream(response.getOutputStream(), JZlib.Z_BEST_COMPRESSION);
        	}
        }
        try {
            String rq = request.getParameter("rq");
            if (rq == null) {
                return;
            }
            JSONObject gpo;
            boolean isAd = false;
            if (rq.equals("ad")) {
                isAd = true;
                gpo = new JSONObject();
                Map<String, String[]> paramNames = request.getParameterMap();
                Iterator<Map.Entry<String, String[]>> it = paramNames.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String[]> me = it.next();
                    String paramName = me.getKey();
                    String[] paramValues = me.getValue();
                    if ("rq".equals(paramName)) {
                        continue;
                    }
                    gpo.put(paramName, paramValues[0]);
                }
                gpo.put("ip", request.getRemoteAddr());
            } else {
                gpo = new JSONObject(rq);
                gpo.put("ip", request.getRemoteAddr());
            }
            Engine ge = Engine.getInstance();
            if (isAd) {
                Object result = ge.ad(gpo);
                if(result != null) {
                	gout.write(result.toString().getBytes("UTF-8"));
                }
            } else {
                Request gr = new Request(gpo);
                Response result = ge.gr(gr);
                if (result != null) {
                    result.setTM(System.currentTimeMillis());
                    gout.write(result.toString().getBytes("UTF-8"));
                }
            }
        } catch (JSONException ex) {
            logger.warn(ex.getMessage());
        } finally {
            gout.flush();
            gout.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
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
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    

}
