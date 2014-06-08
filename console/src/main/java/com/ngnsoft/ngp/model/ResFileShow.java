package com.ngnsoft.ngp.model;

public class ResFileShow extends FileStorage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean select;
	
	private String urn;
	
	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

}
