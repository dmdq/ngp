package com.ngnsoft.ngp.protocol.sale;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author fcy
 */
@Controller
public class APSL extends SyncExecutor {
    
    @Override
    public Response exec(Request req, App app) throws Exception {
    	if (!req.hasKey("receipt")) {
            return new Response(Response.NO, "NO_receipt");
        }
        String base64Recepit = req.getStr("receipt");
        if (base64Recepit == null || base64Recepit.isEmpty()) {
            return new Response(Response.NO, "ERR_receipt");
        }
        JSONObject verifyResponse = iosVerifyReceipt(base64Recepit);
        int status = verifyResponse.getInt("status");
        if(status != 0) {
            return new Response(Response.NO, String.valueOf(status));
        }
        JSONObject receipt = verifyResponse.getJSONObject("receipt");
        AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
        return component.apsl(app, req, receipt);
    }
    
    private JSONObject iosVerifyReceipt(String base64Recepit) throws MalformedURLException, IOException {
        
        //This is the URL of the REST webservice in iTunes App Store
        URL url = new URL("https://sandbox.itunes.apple.com/verifyReceipt");
        
        if (Engine.getInstance().getConfig().isProdStage()) {
            url = new URL("https://buy.itunes.apple.com/verifyReceipt");
        }

        //make connection, use post mode
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("content-type", "text/json");
        connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        
        JSONObject postData = new JSONObject();
        postData.put("receipt-data", base64Recepit);

        //Write the JSON query object to the connection output stream
        PrintStream ps = new PrintStream(connection.getOutputStream());
        ps.print(postData.toString());
        ps.close();

        //Call the service
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //Extract response
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();
        String response = sb.toString();
        
        return new JSONObject(response);
    }

}
