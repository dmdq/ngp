package com.ngnsoft.ngp.component.candy.model;

import java.io.Serializable;

import com.ngnsoft.ngp.model.BaseModel;

public class PopularizeCount extends BaseModel {

	public static final String TYPE_LOGIN = "login";
	public static final String TYPE_BUY = "buy";
	public static final String TYPE_ITEM = "item";
	public static final String TYPE_RESULT = "result";
	
	private Integer id;
	private String type;
	private String deviceId;
	private Integer i1;
	private Integer i2;
	private Integer i3;
	private Integer i4;
	private Integer i5;
	private Integer i6;
	private Integer i7;
	private Integer i8;
	private Integer i9;
	private Integer i10;
	private String s1;
	private String s2;
	private String s3;
	private String s4;
	private String s5;
	private String s6;
	private String s7;
	private String s8;
	private String s9;
	private String s10;
	 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getI1() {
		return i1;
	}

	public void setI1(Integer i1) {
		this.i1 = i1;
	}

	public Integer getI2() {
		return i2;
	}

	public void setI2(Integer i2) {
		this.i2 = i2;
	}

	public Integer getI3() {
		return i3;
	}

	public void setI3(Integer i3) {
		this.i3 = i3;
	}

	public Integer getI4() {
		return i4;
	}

	public void setI4(Integer i4) {
		this.i4 = i4;
	}

	public Integer getI5() {
		return i5;
	}

	public void setI5(Integer i5) {
		this.i5 = i5;
	}

	public Integer getI6() {
		return i6;
	}

	public void setI6(Integer i6) {
		this.i6 = i6;
	}

	public Integer getI7() {
		return i7;
	}

	public void setI7(Integer i7) {
		this.i7 = i7;
	}

	public Integer getI8() {
		return i8;
	}

	public void setI8(Integer i8) {
		this.i8 = i8;
	}

	public Integer getI9() {
		return i9;
	}

	public void setI9(Integer i9) {
		this.i9 = i9;
	}

	public Integer getI10() {
		return i10;
	}

	public void setI10(Integer i10) {
		this.i10 = i10;
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

	public String getS6() {
		return s6;
	}

	public void setS6(String s6) {
		this.s6 = s6;
	}

	public String getS7() {
		return s7;
	}

	public void setS7(String s7) {
		this.s7 = s7;
	}

	public String getS8() {
		return s8;
	}

	public void setS8(String s8) {
		this.s8 = s8;
	}

	public String getS9() {
		return s9;
	}

	public void setS9(String s9) {
		this.s9 = s9;
	}

	public String getS10() {
		return s10;
	}

	public void setS10(String s10) {
		this.s10 = s10;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (Integer) o;;
	}
	
	@Override
	public String getDbName() {
		return "candy";
	}
	
}
