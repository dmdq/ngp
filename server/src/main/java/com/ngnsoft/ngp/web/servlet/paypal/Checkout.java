package com.ngnsoft.ngp.web.servlet.paypal;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.Product;
import com.ngnsoft.ngp.service.GenericManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checkout extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Checkout.class);
    private GenericManager gm;

    @Override
    public void init() throws ServletException {
        super.init();
        gm = Engine.getInstance().getCtx().getBean("genericManager", GenericManager.class);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String custom = request.getParameter("custom");
        String[] cs = custom.split("_");
        String baseId = cs[0];
        String dbName = AppComponentFactory.getComponent(baseId).getDbName();
        int pidIndex = cs[0].length() + cs[1].length() + cs[2].length() + 3;
        String pid = custom.substring(pidIndex);
        Product findProduct = new Product();
        findProduct.setProductId(pid);
        findProduct.setDbName(dbName);
        Product p = gm.findObject(findProduct);
        if (p == null) {
            request.getRequestDispatcher("/paypal/" + baseId + "/" + "nop.html").forward(request, response);
            return;
        }
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getContextPath());
        String returnURL = url.toString() + "/misc/paypal/confirm.st?custom=" + custom;
        String cancelURL = url.toString() + "/misc/paypal/cancel.st";
        String paymentAmount = p.getAmount().toString();
        Map item = new HashMap();
        item.put("name", p.getName());
        item.put("amt", paymentAmount);
        item.put("qty", "1");
        PaypalFunctions ppf = new PaypalFunctions();
        HashMap nvp = ppf.setExpressCheckout(paymentAmount, returnURL, cancelURL, item);
        String strAck = nvp.get("ACK").toString();
        if (strAck != null && strAck.equalsIgnoreCase("Success")) {
            String redirectURL = ppf.PAYPAL_DG_URL + nvp.get("TOKEN").toString();
            response.sendRedirect(redirectURL);
        } else {
            String ErrorCode = nvp.get("L_ERRORCODE0").toString();
            String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
            String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();
            logger.error("Checkout:" + ErrorCode + ", " + ErrorSeverityCode + ", " + ErrorLongMsg);
            response.getWriter().println("<script>\n alert(\"Payment failed. Please try again later.\");\n window.onload = function(){\nif(window.opener){\nwindow.close();\n}\nelse{\nif(top.dg.isOpen() == true){\ntop.dg.closeFlow();\nreturn true;\n}\n}\n};\n</script>");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}