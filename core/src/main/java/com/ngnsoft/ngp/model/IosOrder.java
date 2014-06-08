package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.JSONData;
import com.ngnsoft.ngp.util.JSONUtil;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class IosOrder extends BaseModel {
    
    protected Long id;
    
    protected String productId;
    
    protected String itemId;
    
    protected String quantity;
    
    protected String bid;
    
    protected String bvrs;
    
    protected String purchaseDate;
    
    protected String appId;
    
    protected Long userId;
    
    protected String jsonData;
    
    protected String uniqueVendorIdentifier;
    
    protected String purchaseDateMs;
    
    protected String originalTransactionId;
    
    protected String originalPurchasesDateMs;
    
    protected String uniquerIdetifier;
    
    protected String originalPurchasesDatePst;
    
    protected String transactionId;
    
    protected String purchaseDatePst;
    
    
    public IosOrder(Long id, JSONObject jsonData, String appId, Long userId) {
		this.id = id;
        JSONData jd = new JSONData(jsonData);
        this.productId = jd.getStr("product_id");
        this.itemId = jd.getStr("item_id");
        this.quantity = jd.getStr("quantity");
        this.bid = jd.getStr("bid");
        this.bvrs = jd.getStr("bvrs");
        this.purchaseDate = jd.getStr("purchase_date");
        this.originalPurchasesDateMs = jd.getStr("original_purchases_date_ms");
        this.originalPurchasesDatePst = jd.getStr("original_purchases_date_pst");
        this.originalTransactionId = jd.getStr("original_transaction_id");
        this.transactionId = jd.getStr("transaction_id");
        this.uniquerIdetifier = jd.getStr("unique_idetifier");
        this.uniqueVendorIdentifier = jd.getStr("unique_vendor_identifier");
        this.purchaseDateMs = jd.getStr("purchase_date_ms");
        this.purchaseDatePst = jd.getStr("purchase_date_pst");
        this.jsonData = jsonData.toString();
        this.appId = appId;
        this.userId = userId;
    }
    
    public IosOrder() {
        
    }

    public IosOrder(Long id) {
        this.id = id;
    }
    
    public IosOrder(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBvrs() {
		return bvrs;
	}

	public void setBvrs(String bvrs) {
		this.bvrs = bvrs;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getUniqueVendorIdentifier() {
		return uniqueVendorIdentifier;
	}

	public void setUniqueVendorIdentifier(String uniqueVendorIdentifier) {
		this.uniqueVendorIdentifier = uniqueVendorIdentifier;
	}

	public String getPurchaseDateMs() {
		return purchaseDateMs;
	}

	public void setPurchaseDateMs(String purchaseDateMs) {
		this.purchaseDateMs = purchaseDateMs;
	}

	public String getOriginalTransactionId() {
		return originalTransactionId;
	}

	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}

	public String getOriginalPurchasesDateMs() {
		return originalPurchasesDateMs;
	}

	public void setOriginalPurchasesDateMs(String originalPurchasesDateMs) {
		this.originalPurchasesDateMs = originalPurchasesDateMs;
	}

	public String getUniquerIdetifier() {
		return uniquerIdetifier;
	}

	public void setUniquerIdetifier(String uniquerIdetifier) {
		this.uniquerIdetifier = uniquerIdetifier;
	}

	public String getOriginalPurchasesDatePst() {
		return originalPurchasesDatePst;
	}

	public void setOriginalPurchasesDatePst(String originalPurchasesDatePst) {
		this.originalPurchasesDatePst = originalPurchasesDatePst;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPurchaseDatePst() {
		return purchaseDatePst;
	}

	public void setPurchaseDatePst(String purchaseDatePst) {
		this.purchaseDatePst = purchaseDatePst;
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
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("jsonData");
        return jo;
    }
    
    @Override
    public JSONObject toJSONObject() {
        try {
            JSONObject jo1 = new JSONObject(jsonData);
            JSONObject jo2 = doJSONObject();
            return JSONUtil.mergeAndUpdate(jo1, jo2);
        } catch (JSONException ex) {
        }
        return null;
    }
    
}
