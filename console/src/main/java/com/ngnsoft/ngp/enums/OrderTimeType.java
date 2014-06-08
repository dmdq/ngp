package com.ngnsoft.ngp.enums;

public enum OrderTimeType {
	
	YESTERDAY_TYPE("yesterday"), TODAY_TYPE("today"), SEVENDAYS_TYPE("sevendays"), 
	FIFTEENTHDAYS_TYPE("fifteenthdays"), FEBRUARY_TYPE("february"), 
	MARCH_TYPE("march"), JUNE_TYPE("june"), ALL_TYPE("all");
	
	private String value;
	
	private OrderTimeType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}

}
