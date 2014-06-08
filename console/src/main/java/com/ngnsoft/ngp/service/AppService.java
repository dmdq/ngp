package com.ngnsoft.ngp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Pagination;

/**
 *
 * @author fcy
 */
public interface AppService extends GenericManager {
    
    List<App> findByApp(App app, Pagination page);
    
    Long findTotalNum(App app);
    
    void saveApp(App app, MultipartFile iconDoc) throws Exception;
    
    void updateApp(App app, MultipartFile iconDoc) throws Exception;
}
