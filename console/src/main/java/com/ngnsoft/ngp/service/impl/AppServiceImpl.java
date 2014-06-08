package com.ngnsoft.ngp.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppZone;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.AppService;

/**
 *
 * @author fcy
 */
@Service
public class AppServiceImpl extends GenericManagerImpl implements AppService {

	@Override
	public List<App> findByApp(App app, Pagination page) {
		return this.findMulti(app, page);
	}

	@Override
	public Long findTotalNum(App app) {
		return countTotalNum(app);
	}

	@Override
	public void saveApp(App app, MultipartFile iconDoc) throws Exception {
		String simpleFileName = iconDoc.getOriginalFilename().substring(iconDoc.getOriginalFilename().lastIndexOf(File.separator) + 1);

		if(!simpleFileName.equals("")) {
			byte[] datas = FileCopyUtils.copyToByteArray(iconDoc.getInputStream());
			FileStorage fs = new FileStorage(PhotoType.APP_TYPE.value(), null);
			fs.setName(simpleFileName);
			fs.setData(datas);
			save(fs);
			app.setIconUrn(fs.getUrn());
		}
		save(app);
	}

	@Override
	public void updateApp(App app, MultipartFile iconDoc) throws Exception {
		String simpleFileName = iconDoc.getOriginalFilename().substring(iconDoc.getOriginalFilename().lastIndexOf(File.separator) + 1);
		byte[] datas = FileCopyUtils.copyToByteArray(iconDoc.getInputStream());
		App oldApp = (App)get(app.getId(), App.class);

		if(!simpleFileName.equals("")) {
			FileStorage fs = new FileStorage(PhotoType.APP_TYPE.value(), null);
			fs.setName(simpleFileName);
			if(oldApp.getIconUrn() != null && !oldApp.getIconUrn().equals("")) {
//				String fileStorageId = oldApp.getIconUrn().substring(oldApp.getIconUrn().lastIndexOf("/") + 1);
				List<FileStorage> fss = findMulti(fs);
				if(fss.size() == 0) {
					fs.setData(datas);
					save(fs);
				} else {
					fs = fss.get(0);
				}
			} else {
				fs = new FileStorage(PhotoType.APP_TYPE.value(), null);
				List<FileStorage> fss = findMulti(fs);
				if(fss.size() == 0) {
					fs.setData(datas);
					save(fs);
				} else {
					fs = fss.get(0);
				}
			}
			app.setIconUrn(fs.getUrn());
			app.setCreateTime(oldApp.getCreateTime());
			app.setUpdateTime(new Date());
		}

		update(app);

		//remove all record by appId
		/*List<AppZone> appZones = this.findMulti(new AppZone());
		for (AppZone appZone : appZones) {
			if(appZone.getAppId().equals(app.getId())){
				remove(appZone);
			}
		}*/
	}
}
