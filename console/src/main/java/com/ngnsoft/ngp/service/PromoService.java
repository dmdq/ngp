package com.ngnsoft.ngp.service;

import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.model.Promo;



public interface PromoService extends GenericManager {
	
	void savePromo(Promo promo, MultipartFile iconDoc) throws Exception;
	
	void updatePromo(Promo promo, MultipartFile iconDoc) throws Exception;
}
