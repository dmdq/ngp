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
 * @author fcy
 */
public class PaypalOrder extends BaseModel {
    
    protected Long id;
    
    protected String txnId;
    
    protected String txnType;
    
    protected String custom;
    
    protected String business;
    
    protected String btnId;
    
    protected String itemNumber;
    
    protected String itemName;
    
    protected String quantity;
    
    protected String payerId;
    
    protected String payerEmail;
    
    protected String receiverId;
    
    protected String receiverEmail;
    
    protected String paymentData;
    
    protected String ipnTrackId;
    
    protected String jsonData;
    
    protected Long paymentDateMs;
    
    
    public PaypalOrder(JSONObject jsonData) {
        JSONData jd = new JSONData(jsonData);
        this.btnId = jd.getStr("btn_id");
        this.business = jd.getStr("business");
        this.quantity = jd.getStr("L_QTY0");
        this.custom = jd.getStr("custom");
        this.ipnTrackId = jd.getStr("ipn_track_id");
        this.itemName = jd.getStr("L_NAME0");
        this.itemNumber = jd.getStr("item_number");
        this.payerEmail = jd.getStr("EMAIL");
        this.payerId = jd.getStr("PAYERID");
        this.paymentData = jd.getStr("TIMESTAMP");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.US);
        try {
			Date t = sdf.parse(paymentData);
			sdf.setTimeZone(TimeZone.getTimeZone("PDT"));
			this.paymentDateMs = t.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        this.txnId = jd.getStr("TOKEN");
        this.txnType = jd.getStr("txn_type");
        this.receiverId = jd.getStr("receiver_id");
        this.receiverEmail = jd.getStr("receiver_email");
        this.jsonData = jsonData.toString();
    }
    
    public PaypalOrder() {
        
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getBtnId() {
		return btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
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

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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

	public String getIpnTrackId() {
		return ipnTrackId;
	}

	public void setIpnTrackId(String ipnTrackId) {
		this.ipnTrackId = ipnTrackId;
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
