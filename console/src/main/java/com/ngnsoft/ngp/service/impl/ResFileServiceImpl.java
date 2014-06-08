package com.ngnsoft.ngp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.ResUpdateFile;
import com.ngnsoft.ngp.service.ResFileService;

/**
 * 
 * @author yjl
 *
 */
@Service
public class ResFileServiceImpl extends GenericManagerImpl implements ResFileService {

	@Override
	public void addResFile(MultipartFile uri) throws Exception {
		FileStorage fs = new FileStorage(PhotoType.RES_TYPE.value(), null);
		fs.setName(uri.getOriginalFilename());
		byte[] datas = FileCopyUtils.copyToByteArray(uri.getInputStream());
		fs.setData(datas);

		this.save(fs);
	}

	@Override
	public void removeResFile(String resId) throws Exception {
		List<ResUpdateFile> rufs = this.findMulti(new ResUpdateFile());
		
		/**
		 * 级联删除
		 */
		for (int i = 0; i < rufs.size(); i++) {
			String id = rufs.get(i).getFileUrn().substring(rufs.get(i).getFileUrn().lastIndexOf("/")+1, rufs.get(i).getFileUrn().length());
			if(id.equals(resId)){
				this.remove(rufs.get(i));
			}
		}

		remove(new FileStorage(PhotoType.RES_TYPE.value(), resId));
		
	}

}
