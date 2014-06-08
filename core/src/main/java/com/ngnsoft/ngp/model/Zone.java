package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.List;
import org.springframework.util.StringUtils;

public class Zone extends BaseModel {

	private Long id;
	private String name;
	private String favEngineId;
	private Integer recommend;

	private List<EngineNode> engineNodes;

	private List<App> apps;

	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	public List<EngineNode> getEngineNodes() {
		return engineNodes;
	}

	public void setEngineNodes(List<EngineNode> engineNodes) {
		this.engineNodes = engineNodes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFavEngineId() {
		return favEngineId;
	}

	public void setFavEngineId(String favEngineId) {
		this.favEngineId = favEngineId;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long) o;
	}

	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if (!StringUtils.hasText(favEngineId)) {
			favEngineId = null;
		}
		if (recommend == null) {
			recommend = 0;
		}
	}

	@Override
	public void prepareForUpdate() {
		super.prepareForUpdate();
		if (!StringUtils.hasText(favEngineId))
			favEngineId = null;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Zone) {
            Zone zone = (Zone) obj;
            if (zone.getId().intValue() == this.id.intValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

}
