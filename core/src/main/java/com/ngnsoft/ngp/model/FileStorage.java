package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class FileStorage extends BaseModel {
    
    public static final String TABLE_NAME_FILE = "file";
    public static final String TABLE_NAME_APP_FILE = "app_file";
    public static final String TABLE_NAME_AVATAR_FILE = "avatar_file";
    public static final String TABLE_NAME_PROMO_FILE = "promo_file";
    public static final String TABLE_NAME_RES_FILE = "res_file";
    
    public static final String FILE_PATH = "/file/";
    
    public static final String LOCAL_PATH = "local://";
    
    public static final String SHORT_NAME_APP = "app";
    public static final String SHORT_NAME_AVATAR = "avatar";
    public static final String SHORT_NAME_PROMO = "promo";
    public static final String SHORT_NAME_RES = "res";
    
    private String id;
    
    private String name;
    
    private String urn;
    
    private byte[] data;
    
    public FileStorage() {
        this(null, null);
    }
    
    public FileStorage(String shortName, String id) {
        this.id = id;
        urn = genUrn(shortName);
        tableName = genTableName(shortName);
    }
    
    public FileStorage(String shortName, String id, String name, byte[] data) {
        this.id = id;
        this.name = name;
        this.data = data;
        urn = genUrn(shortName);
        tableName = genTableName(shortName);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrn() {
        return urn;
    }
    
    private String genTableName(String shortName) {
        String ts = TABLE_NAME_FILE;
        if (SHORT_NAME_APP.equalsIgnoreCase(shortName)) {
            ts = TABLE_NAME_APP_FILE;
        } else if (SHORT_NAME_AVATAR.equalsIgnoreCase(shortName)) {
            ts = TABLE_NAME_AVATAR_FILE;
        } else if (SHORT_NAME_PROMO.equalsIgnoreCase(shortName)) {
            ts = TABLE_NAME_PROMO_FILE;
        } else if (SHORT_NAME_RES.equalsIgnoreCase(shortName)) {
            ts = TABLE_NAME_RES_FILE;
        }
        return ts;
    }
    
    public static String genUrn(String shortName) {
        if (shortName == null) {
            return FILE_PATH;
        } else {
            return FILE_PATH + shortName.toLowerCase() + "/";
        }
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }
    
    @Override
    public void setPrimaryKey(Object o) {
        id = (String)o;
    }
    
    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if(id == null)
        	id = Integer.toHexString(name.hashCode());
        urn = urn + id;
    }
    
    @Override
    public void prepareForUpdate() {
    	super.prepareForUpdate();
    	 urn = urn + id;
    }

}
