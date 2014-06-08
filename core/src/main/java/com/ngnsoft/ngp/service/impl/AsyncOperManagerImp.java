package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Notification;
import com.ngnsoft.ngp.service.AsyncOperManager;
import com.ngnsoft.ngp.service.DataManager;


/**
 * 所有异步插入操作
 * @author lmj
 *
 */
@Service
public class AsyncOperManagerImp extends GenericManagerImpl implements AsyncOperManager {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncOperManagerImp.class);
    
	private static final BlockingQueue<Notification> notificationQueue = new LinkedBlockingQueue<Notification>();
	
	private static final BlockingQueue<BaseModel> updateObjQueue = new LinkedBlockingQueue<BaseModel>();
	
	private static final BlockingQueue<BaseModel> saveObjQueue = new LinkedBlockingQueue<BaseModel>();
	
	private static final int MAX_POLL_OBJECT_NUM = 500;
	
	@Value("${async.operate.thread.count}")
	private int threadCount; 
	
	@Autowired
	private DataManager dataManager;
	
	@PostConstruct
	public void init() throws Exception{
		for(int i = 0; i < threadCount; i++){
			//启动线程
			AsyncNotiThread asyncNotiThread = new AsyncNotiThread(this, i);
			AsyncUpdateThread asyncUpdThread = new AsyncUpdateThread(this, i);
			AsyncSaveThread asyncSaveThread = new AsyncSaveThread(this, i);
			Thread thread1 = new Thread(asyncNotiThread);
			Thread thread2 = new Thread(asyncUpdThread);
			Thread thread3 = new Thread(asyncSaveThread);
			thread1.start();
			thread2.start();
			thread3.start();
		}
		
	}
	
	/**
	 * add baseModel to queue
	 */
	@Override
	public <T extends BaseModel> void saveData(T object) throws Exception {
		saveObjQueue.put(object);
	}
	
	/**
	 * add baseModel to queue
	 */
	@Override
	public <T extends BaseModel> void updateData(T object) throws Exception {
		updateObjQueue.put(object);
	}
	
	/**
	 * Asynchronous notification
	 */
	@Override
	public void addNotification(Notification notification) throws Exception {
		notificationQueue.put(notification);
	}

	/**
	 * Send a notification by thread
	 */
	@Override
	public void sendNotification() throws Exception {
		Notification notif = notificationQueue.take();
		dataManager.push(notif.getUserId(), notif.getApp(), notif.getPn());
	}
	
	/**
	 * update data from queue
	 * @author Administrator
	 * @throws InterruptedException 
	 *
	 */
	public void updateTable() throws InterruptedException {
		List<BaseModel> modelList = new ArrayList<BaseModel>();
		if(updateObjQueue.size() == 0) modelList.add(updateObjQueue.take());
		updateObjQueue.drainTo(modelList, MAX_POLL_OBJECT_NUM);
		long startTime = System.currentTimeMillis();
		dataManager.batchUpdate(modelList);
		long endTime = System.currentTimeMillis();
		logger.debug("Updata batch waste time :" + (endTime - startTime));
	}
	
	/**
	 * save data from queue
	 * @author Administrator
	 * @throws InterruptedException 
	 *
	 */
	@Override
	public void saveTable() throws Exception {
		List<BaseModel> modelList = new ArrayList<BaseModel>();
		if(saveObjQueue.size() == 0) modelList.add(saveObjQueue.take());
		saveObjQueue.drainTo(modelList, MAX_POLL_OBJECT_NUM);
		long startTime = System.currentTimeMillis();
		dataManager.batchSave(modelList);
		long endTime = System.currentTimeMillis();
		logger.debug("Save batch waste time :" + (endTime - startTime));
	}
	
	class AsyncNotiThread implements Runnable{
		public final Logger LOGGER = LoggerFactory.getLogger(AsyncNotiThread.class);
		AsyncOperManager asyncOperManger = null;
		int threadNum;
        public AsyncNotiThread(AsyncOperManager asyncOperManger, int threadNum){
        	this.asyncOperManger = asyncOperManger;
        	this.threadNum = threadNum;
        }
		public void run() {
			while(true){
				try {
					asyncOperManger.sendNotification();
				} catch (Exception e) {
					LOGGER.error("ERROR asyncNoti Thread " + threadNum, e);
					e.printStackTrace();
				}
			}
			
		}
	}
	
	class AsyncUpdateThread implements Runnable{
		public final Logger LOGGER = LoggerFactory.getLogger(AsyncUpdateThread.class);
		AsyncOperManager asyncOperManger = null;
		int threadNum;
        public AsyncUpdateThread(AsyncOperManager asyncOperManger, int threadNum){
        	this.asyncOperManger = asyncOperManger;
        	this.threadNum = threadNum;
        }
		public void run() {
			while(true){
				try {
					asyncOperManger.updateTable();
				} catch (Exception e) {
					LOGGER.error("ERROR asyncUpdate Thread " + threadNum, e);
					e.printStackTrace();
				}
			}
			
		}
	}
	
	class AsyncSaveThread implements Runnable{
		public final Logger LOGGER = LoggerFactory.getLogger(AsyncSaveThread.class);
		AsyncOperManager asyncOperManger = null;
		int threadNum;
        public AsyncSaveThread(AsyncOperManager asyncOperManger, int threadNum){
        	this.asyncOperManger = asyncOperManger;
        	this.threadNum = threadNum;
        }
		public void run() {
			while(true){
				try {
					asyncOperManger.saveTable();
				} catch (Exception e) {
					LOGGER.error("ERROR asyncUpdate Thread " + threadNum, e);
					e.printStackTrace();
				}
			}
			
		}
	}

}
