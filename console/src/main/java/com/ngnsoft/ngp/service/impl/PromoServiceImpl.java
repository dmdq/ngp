package com.ngnsoft.ngp.service.impl;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Promo;
import com.ngnsoft.ngp.service.PromoService;

@Service
public class PromoServiceImpl extends GenericManagerImpl implements
		PromoService {

	@Override
	public void savePromo(Promo promo, MultipartFile iconDoc) throws Exception {
		String simpleFileName = iconDoc.getOriginalFilename().substring(iconDoc.getOriginalFilename().lastIndexOf(File.separator) + 1);
		
		byte[] datas = FileCopyUtils.copyToByteArray(iconDoc.getInputStream());
		
		FileStorage fs = new FileStorage(PhotoType.PROMO_TYPE.value(), null);
		fs.setName(simpleFileName);
		fs.setData(datas);
		save(fs);

		promo.setIcon(fs.getUrn());
		save(promo);
		
		
	}

	@Override
	public void updatePromo(Promo promo, MultipartFile iconDoc)
			throws Exception {
		Promo oldPromo = (Promo)get(promo.getId(), Promo.class);
		String simpleFileName = iconDoc.getOriginalFilename().substring(iconDoc.getOriginalFilename().lastIndexOf(File.separator) + 1);
		if(simpleFileName != null && !simpleFileName.equals("")) {
			byte[] datas = FileCopyUtils.copyToByteArray(iconDoc.getInputStream());
			String fileStorageId = oldPromo.getIcon().substring(oldPromo.getIcon().lastIndexOf("/") + 1);
			FileStorage fileStorage = new FileStorage(PhotoType.PROMO_TYPE.value(), fileStorageId, simpleFileName, datas);
			update(fileStorage);
		} else {
			promo.setIcon(oldPromo.getIcon());
			promo.setCreateTime(oldPromo.getCreateTime());
		}
		update(promo);
	}
	
}
