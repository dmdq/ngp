package com.ngnsoft.ngp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.service.AvatarFileService;

/**
 * 
 * @author yjl
 *
 */
@Service
public class AvatarFileServiceImpl extends GenericManagerImpl implements AvatarFileService {
	
	@Override
	public void addAvatarFile(MultipartFile uri) throws Exception {
		FileStorage fs = new FileStorage(PhotoType.AVATAR_TYPE.value(), null);
		fs.setName(uri.getOriginalFilename());
		byte[] datas = FileCopyUtils.copyToByteArray(uri.getInputStream());
		fs.setData(datas);
		
		this.save(fs);
	}

}
