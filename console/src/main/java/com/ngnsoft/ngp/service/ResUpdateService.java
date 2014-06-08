package com.ngnsoft.ngp.service;

import java.util.List;

import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.ResUpdate;



/**
 * 
 * @author yjl
 *
 */
public interface ResUpdateService extends GenericManager {
	void addResUpdate(String[] names, ResUpdate ru, List<FileStorage> fss) throws Exception;
}
