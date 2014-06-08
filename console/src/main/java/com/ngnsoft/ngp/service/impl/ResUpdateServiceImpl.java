package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.ResUpdate;
import com.ngnsoft.ngp.model.ResUpdateFile;
import com.ngnsoft.ngp.service.ResUpdateService;

/**
 * 
 * @author yjl
 *
 */
@Service
public class ResUpdateServiceImpl extends GenericManagerImpl implements ResUpdateService {
	
	@Override
	public void addResUpdate(String[] names, ResUpdate ru, List<FileStorage> fss) throws Exception {
		ru.setCreateTime(new Date());
		this.save(ru);
		
		ResUpdateFile ruf = new ResUpdateFile();
		
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			for (int j = 0; j < fss.size(); j++) {
				if(fss.get(j).getUrn().equals(name)){
					ruf.setRuId(ru.getId());
					ruf.setFileName(fss.get(j).getName());
					ruf.setFileUrn(fss.get(j).getUrn());
					ruf.setCreateTime(new Date());
					save(ruf);
				}
			}
		}
	}
}
