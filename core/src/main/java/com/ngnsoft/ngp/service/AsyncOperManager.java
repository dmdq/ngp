package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Notification;


public interface AsyncOperManager {
	
	public void addNotification(Notification notification) throws Exception;
	
	public <T extends BaseModel> void updateData(T object) throws Exception;
	
	public <T extends BaseModel> void saveData(T object) throws Exception;
	
	public void sendNotification() throws Exception;
	
	public void updateTable() throws Exception;
	
	public void saveTable() throws Exception;
	
}
