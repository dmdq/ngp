package com.ngnsoft.ngp.enums;

public enum PhotoType {
	
	APP_TYPE("app"), AVATAR_TYPE("avatar"), RES_TYPE("res"), PROMO_TYPE("promo");
	
	private String value;
	
	private PhotoType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}

}
