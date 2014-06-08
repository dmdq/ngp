package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.Response;

public interface ResUpdateManager extends GenericManager {
	
	Response checkResVersion(String appId, Integer resVersion) throws Exception;

}
