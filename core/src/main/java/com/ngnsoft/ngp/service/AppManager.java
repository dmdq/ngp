package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.App;

/**
 *
 * @author fcy
 */
public interface AppManager extends GenericManager {
    
    void saveAll(App app);
    
}
