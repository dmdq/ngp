package com.ngnsoft.ngp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "cmge_message")
public class OrderWrapper {

	@XmlElement(name = "message")
	private Order order;

	@XmlTransient
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}
