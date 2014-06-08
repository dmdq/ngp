package com.ngnsoft.ngp.model;

import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public abstract class BaseModelExt extends BaseModel {
    
    protected Long createTimeMs;
    
    protected Long updateTimeMs;
    
    @Override
    public void prepareForSave() {
        createTime = new Date();
        createTimeMs = createTime.getTime();
        updateTime = createTime;
        updateTimeMs = createTimeMs;
    }
    
    @Override
    public void prepareForUpdate() {
        if (updateTime != null) {
            updateTime = new Date();
            updateTimeMs = updateTime.getTime();
        }
    }
    
    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("createTimeMs");
        jo.remove("updateTimeMs");
        return jo;
    }
    
}
