package com.ngnsoft.ngp.model;

import org.springframework.util.ClassUtils;

/**
 *
 * @author fcy
 */
public class AppConfigBase extends AppConfig {
    
    public static final String TABLE_NAME = "app_config_base";
    
    public AppConfigBase() {
        tableName = TABLE_NAME;
    }
    
    public AppConfigBase(String appId) {
        super(appId);
        tableName = TABLE_NAME;
    }
    
    public AppConfigBase(String appId, String jsonAll) {
        super(appId, jsonAll);
        tableName = TABLE_NAME;
    }

    @Override
    public String getModelName() {
        return ClassUtils.getShortName(this.getClass().getSuperclass());
    }
    
}
