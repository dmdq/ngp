package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.JSONData;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yjl
 */
public class PaypalIndent extends BaseModel {
    
    protected Long id;
    
    protected String custom;
    
    protected String totalAmount;
    
    protected String token;
    
    protected String itemName;
    
    protected String quantity;
    
    protected String payerId;
    
    protected String payerEmail;
    
    protected String receiverEmail;
    
    protected String paymentData;
    
    protected String jsonData;
    
    protected Long paymentDateMs;
    
    
    public PaypalIndent(JSONObject jsonData) {
        JSONData jd = new JSONData(jsonData);
        this.totalAmount = jd.getStr("AMT");
        this.quantity = jd.getStr("quantity");
        this.custom = jd.getStr("custom");
        this.itemName = jd.getStr("name");
        this.payerEmail = jd.getStr("eamil");
        this.payerId = jd.getStr("payerId");
        this.paymentData = jd.getStr("TIMESTAMP");
        this.token = jd.getStr("transactionId");
        this.receiverEmail = jd.getStr("receiver_email");
        this.jsonData = jsonData.toString();
    }
    
    public PaypalIndent() {
        
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(String paymentData) {
		this.paymentData = paymentData;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public Long getPaymentDateMs() {
		return paymentDateMs;
	}

	public void setPaymentDateMs(Long paymentDateMs) {
		this.paymentDateMs = paymentDateMs;
	}

	@Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        this.id = (Long)o;
    }
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
    public JSONObject toJSONObject() {
        try {
            JSONObject jo = super.doJSONObject();
            jo.remove("jsonData");
            return jo;
        } catch (JSONException ex) {
        }
        return null;
    }
    
}
