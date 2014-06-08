package com.ngnsoft.ngp.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author yjl
 *
 */
public interface ResFileService extends GenericManager {
	void addResFile(MultipartFile uri) throws Exception;
	
	void removeResFile(String resId) throws Exception;
}
