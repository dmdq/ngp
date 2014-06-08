package com.ngnsoft.ngp.component.slots.model;

import java.io.Serializable;
import java.util.Date;

import com.ngnsoft.ngp.model.BaseModel;

public class DataCount extends BaseModel {

	public static final String TYPE_TOP_DATA = "topData";
	public static final String TYPE_BUY_DATA = "buyData";
	public static final String TYPE_BASIS_DATA = "basisData";
	public static final String TYPE_LOGIN_DATA = "loginData";
	public static final String TYPE_EXPEND_MONEY_DATA = "expendMoneyData";
	public static final String TYPE_USER_GET_DATA = "userGetData";
	
	private Integer id;
	private Long userId;
	private String type;
	private Long i1;
	private Long i2;
	private Long i3;
	private Long i4;
	private Long i5;
	private String s1;
	private String s2;
	private String s3;
	private String s4;
	private String s5;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getI1() {
		return i1;
	}

	public void setI1(Long i1) {
		this.i1 = i1;
	}

	public Long getI2() {
		return i2;
	}

	public void setI2(Long i2) {
		this.i2 = i2;
	}

	public Long getI3() {
		return i3;
	}

	public void setI3(Long i3) {
		this.i3 = i3;
	}

	public Long getI4() {
		return i4;
	}

	public void setI4(Long i4) {
		this.i4 = i4;
	}

	public Long getI5() {
		return i5;
	}

	public void setI5(Long i5) {
		this.i5 = i5;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	public String getS4() {
		return s4;
	}

	public void setS4(String s4) {
		this.s4 = s4;
	}

	public String getS5() {
		return s5;
	}

	public void setS5(String s5) {
		this.s5 = s5;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (Integer) o;
	}
	
	@Override
	public void prepareForSave() {
		if(createTime == null) {
			createTime = new Date();
		}
	}
	
	@Override
	public String getDbName() {
		return "slots";
	}
	
}
