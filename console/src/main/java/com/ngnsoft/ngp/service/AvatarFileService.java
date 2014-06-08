package com.ngnsoft.ngp.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author yjl
 *
 */
public interface AvatarFileService extends GenericManager {
    void addAvatarFile(MultipartFile uri) throws Exception;
}
