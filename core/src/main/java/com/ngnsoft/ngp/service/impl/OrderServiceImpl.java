package com.ngnsoft.ngp.service.impl;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.Order;
import com.ngnsoft.ngp.service.OrderService;

@Service
public class OrderServiceImpl extends GenericManagerImpl implements OrderService {

	@Override
	public boolean saveOrder(Order order) {
		if(exist(order.getOrderId(), Order.class)) {
			return false;
		} else {
			return this.save(order);
		}
	}
}
