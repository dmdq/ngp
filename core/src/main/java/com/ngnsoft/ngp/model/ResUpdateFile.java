package com.ngnsoft.ngp.model;

import java.io.Serializable;

public class ResUpdateFile extends BaseModel {
	
	private Long id;
	
	private String fileName;
	
	private String fileUrn;
	
	private Long ruId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrn() {
		return fileUrn;
	}

	public void setFileUrn(String fileUrn) {
		this.fileUrn = fileUrn;
	}

	public Long getRuId() {
		return ruId;
	}

	public void setRuId(Long ruId) {
		this.ruId = ruId;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}

}
