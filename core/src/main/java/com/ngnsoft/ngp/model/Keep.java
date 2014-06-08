package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;

import com.ngnsoft.ngp.model.BaseModel;

/**
 *
 * @author fcy
 */
public class Keep extends BaseModel {
    
    private Long insNumber;
    
	private Long logNumber;
	
	private Long tomKeep;
	
	private Long sevKeep;
	
	private Long fiftKeep;
	
	private Long firtKeep;

	public Long getInsNumber() {
		return insNumber;
	}

	public void setInsNumber(Long insNumber) {
		this.insNumber = insNumber;
	}

	public Long getLogNumber() {
		return logNumber;
	}

	public void setLogNumber(Long logNumber) {
		this.logNumber = logNumber;
	}

	public Long getTomKeep() {
		return tomKeep;
	}

	public void setTomKeep(Long tomKeep) {
		this.tomKeep = tomKeep;
	}

	public Long getSevKeep() {
		return sevKeep;
	}

	public void setSevKeep(Long sevKeep) {
		this.sevKeep = sevKeep;
	}

	public Long getFiftKeep() {
		return fiftKeep;
	}

	public void setFiftKeep(Long fiftKeep) {
		this.fiftKeep = fiftKeep;
	}

	public Long getFirtKeep() {
		return firtKeep;
	}

	public void setFirtKeep(Long firtKeep) {
		this.firtKeep = firtKeep;
	}

	@Override
	public Serializable getPrimaryKey() {
		return createTime;
	}

	@Override
	public void setPrimaryKey(Object o) {
		createTime = (Date) o;
	}
}
