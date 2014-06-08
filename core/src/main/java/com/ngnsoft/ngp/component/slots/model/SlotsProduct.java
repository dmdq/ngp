package com.ngnsoft.ngp.component.slots.model;

import com.ngnsoft.ngp.model.Product;

public class SlotsProduct extends Product {

	@Override
	public String getDbName() {
		return "slots";
	}
	
	@Override
    public String getTableName() {
        return "slots_product";
    }

    @Override
    public String getModelName() {
        return "SlotsProduct";
    }
	
}
