package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.Order;

/**
 * 
 * @author yjl
 *
 */
public interface OrderService extends GenericManager {
	
	boolean saveOrder(Order order);
	
}
