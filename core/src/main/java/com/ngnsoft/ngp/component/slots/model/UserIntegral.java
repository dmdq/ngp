package com.ngnsoft.ngp.component.slots.model;

import java.io.Serializable;

public class UserIntegral implements Serializable, Comparable {

	private Integer integral;
	
	private Long userId;

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Override
	public int compareTo(Object o) {
		if(o instanceof UserIntegral) {
			UserIntegral integral = (UserIntegral)o;
			if(integral.getIntegral() >= this.getIntegral()) 
				return -1;
			else return 1;
		}
		return 0;
	}

}
