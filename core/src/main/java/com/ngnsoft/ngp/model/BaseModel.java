package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.util.MiscUtil;
import java.io.Serializable;
import java.util.Date;
import org.json.JSONObject;
import org.springframework.util.ClassUtils;

/**
 *
 * @author fcy
 */
public abstract class BaseModel implements Serializable {
    
    protected String dbName;
    
    protected String tableName;
    
    protected String modelName;
    
    protected Date createTime;
    
    protected Date updateTime;
    
    public static final String NGP_DB_NAME = "ngp";
    
    public BaseModel() {
        dbName = NGP_DB_NAME;
        tableName = MiscUtil.classToDbName(this.getClass());
        modelName = genModelName();
    }
    
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    private String genModelName() {
        return ClassUtils.getShortName(this.getClass());
    }
    
    public abstract Serializable getPrimaryKey();
    
    public abstract void setPrimaryKey(Object o);
    
    public void prepareForSave() {
        createTime = new Date();
        updateTime = createTime;
    }
    
    public void prepareForUpdate() {
        if (updateTime != null) {
            updateTime = new Date();
        }
    }
    
    /**
     * 
     * @return the JSONObject represent of this object with valid properties
     */
    protected JSONObject doJSONObject() {
        JSONObject jo = new JSONObject(this);
        jo.remove("dbName");
        jo.remove("tableName");
        jo.remove("modelName");
        jo.remove("primaryKey");
        jo.remove("createTime");
        jo.remove("updateTime");
        return jo;
    }
    
    /**
     * 
     * @return the JSONObject represent of this object with specified properties
     */
    public JSONObject toJSONObject() {
        return doJSONObject();
    }
    
}
