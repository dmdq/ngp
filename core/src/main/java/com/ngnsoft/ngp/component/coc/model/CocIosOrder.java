package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.IosOrder;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class CocIosOrder extends IosOrder {
    
    public CocIosOrder() {
        
    }

    public CocIosOrder(Long id) {
        super(id);
    }
    
    public CocIosOrder(String orderId) {
        super(orderId);
    }
    
    public CocIosOrder(Long id, JSONObject jsonData, String appId, Long userId) {
        super(id, jsonData, appId, userId);
    }

    @Override
    public String getDbName() {
        return "coc";
    }
    
    @Override
    public String getTableName() {
        return "coc_ios_order";
    }

    @Override
    public String getModelName() {
        return "CocIosOrder";
    }
    
}
