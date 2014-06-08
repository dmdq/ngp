package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.Product;

public class CocProduct extends Product {

	@Override
	public String getDbName() {
		return "coc";
	}
	
	@Override
    public String getTableName() {
        return "coc_product";
    }

    @Override
    public String getModelName() {
        return "CocProduct";
    }
	
}
