package com.ngnsoft.ngp.enums;

public enum DataSourceType {

	UPDATE("update"), SELECT("select");
	
	private String value;
	
	private DataSourceType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
}
