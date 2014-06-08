package com.ngnsoft.ngp.enums;

public enum DataType {

	PLACARD("placard"), PROMOTION("promotion");
	
	private String value;
	
	private DataType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
}
