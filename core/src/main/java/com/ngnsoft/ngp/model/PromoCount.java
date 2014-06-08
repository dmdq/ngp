package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;

public class PromoCount extends BaseModel {
	
	private Long id;
	
	private Long promoId;
	
	private Long clickCount;
	
	private Date countDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPromoId() {
		return promoId;
	}

	public void setPromoId(Long promoId) {
		this.promoId = promoId;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(countDate == null)
			countDate = new Date();
	}

}
