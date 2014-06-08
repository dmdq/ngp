package com.ngnsoft.ngp.enums;

public enum Game {

	COC("coc"), DRAGON("dragon");
	
	private String name;
	
	private Game(String name) {
		this.name = name;
	}
	
	public String value() {
		return name;
	}
	
}
