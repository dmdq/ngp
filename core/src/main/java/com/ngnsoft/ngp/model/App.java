package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author fcy
 */
public class App extends BaseModel {

    public static final int STATUS_NORMAL = 0;
    public static final String STATUS_DESC_NORMAL = "normal";
    public static final int STATUS_OFF = -1;
    public static final String STATUS_DESC_OFF = "off";
    public static final int STATUS_TO_MAINTENANCE = -2;
    public static final String STATUS_DESC_TO_MAINTENANCE = "Maintenance is coming soon.";
    public static final int STATUS_MAINTENANCE = -3;
    public static final String STATUS_DESC_MAINTENANCE = "Maintenance break. Please try again later.";
    private String id;
    private String name;
    private String baseId;
    private String baseName;
    private String url;
    private String iconUrn;
    private Integer status;
    private String statusDesc;
    private List<Zone> zones;

    public App() {
    }

    public App(String id) {
        this.id = id;
    }

    public App(String id, String name, String baseId, String baseName, String url,
            String iconUrn, Integer status, String statusDesc) {
        this.id = id;
        this.name = name;
        this.baseId = baseId;
        this.baseName = baseName;
        this.url = url;
        this.statusDesc = statusDesc;
        this.iconUrn = iconUrn;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getIconUrn() {
        return iconUrn;
    }

    public void setIconUrn(String iconUrn) {
        this.iconUrn = iconUrn;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	@Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (String) o;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (baseId == null || baseId.trim().isEmpty()) {
            baseId = id;
        }
        if (status == null) {
            status = STATUS_NORMAL;
            statusDesc = STATUS_DESC_NORMAL;
        }
    }
    
}
