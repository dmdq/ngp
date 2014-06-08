package com.ngnsoft.ngp.web.servlet;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.service.GenericManager;
import java.io.IOException;
import java.io.OutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fcy
 */
public class FileServlet extends HttpServlet {
    
    private GenericManager gm;
    
    private static final MimetypesFileTypeMap mftm = (MimetypesFileTypeMap)MimetypesFileTypeMap.getDefaultFileTypeMap();
    static {
        mftm.addMimeTypes("image/png png");
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
        String shortName = null;
        String uuid;
        if (params.length == 1) {
            uuid = params[0];
        } else if (params.length == 2) {
            shortName = params[0];
            uuid = params[1];
        } else {
            return;
        }
        FileStorage fs = new FileStorage(shortName, uuid);
        if (gm == null) {
            gm = Engine.getInstance().getCtx().getBean("genericManager", GenericManager.class);
        }
        fs = gm.findObject(fs);
        if (fs != null) {
            byte[] data = fs.getData();
            String name = fs.getName();
            if (name == null || name.isEmpty() || data == null || data.length == 0)
                return;
            int dotIndex = name.lastIndexOf(".");
            if (dotIndex != -1) {
                name = name.substring(dotIndex + 1);
            }
            String mimeType = mftm.getContentType(name);
            response.setContentType(mimeType);
            OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close(); 
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
        return "Short description";
    }
    
    public static void main(String[] args) {
		String a1 = "unlock_item_tire50_amazingcandymatchinggame0708";
		
		System.out.println(a1.hashCode());
	}
}
