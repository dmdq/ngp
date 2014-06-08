package com.ngnsoft.ngp.component.slots.model;

import com.ngnsoft.ngp.model.SaleHistory;

/**
 *
 * @author fcy
 */
public class SlotsSaleHistory extends SaleHistory {
    
	@Override
	public String getDbName() {
		return "slots";
	}
	
	@Override
	public String getTableName() {
		return "sale_history";
	}
	
	@Override
	public String getModelName() {
		return "SaleHistory";
	}
	
}
