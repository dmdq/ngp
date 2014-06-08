package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class EngineNode extends BaseModel {
    
    public static final long DOWN_TIME_SECOND = 600;//10 * 60
    
    public static final int FINE_POINT_PERCENT = 80;
    
    public static final String TABLE_NAME = "engine";
    
    public static final int STATUS_LIVE = 0;
    
    public static final String STATUS_DESC_LIVE = "live";
    
    public static final int STATUS_BUSY = 1;
    
    public static final String STATUS_DESC_BUSY = "Server is busy.";
    
    public static final int STATUS_DOWN = -1;
    
    public static final String STATUS_DESC_DOWN = "Server in maintenance.";
    
    public static final int STATUS_TO_MAINTENANCE  = -2;
    
    public static final String STATUS_DESC_TO_MAINTENANCE = "Maintenance is coming soon.";
    
    public static final int STATUS_MAINTENANCE  = -3;
    
    public static final String STATUS_DESC_MAINTENANCE  = "Maintenance break. Please try again later.";
    
    public static final int STATUS_STOP = -4;
    
    public static final String STATUS_DESC_STOP = "Server was stopped.";
    
    public static final int STATUS_LOCK_N = 0;
    
    public static final int STATUS_LOCK_Y = 1;
    
    private String id;
    
    private String name;
    
    private String host;
    
    private Integer status;
    
    private String statusDesc;
    
    private Integer statusLock;
    
    private Date touchTime;
    
    private Long load;//理想负载（在线数量）
    
    private Long loading;//实际负载（实际在线，非实时，由EngineJob定时更新）
    
    public EngineNode() {
        tableName = TABLE_NAME;
    }
    
    public EngineNode(String id) {
        this.id = id;
        tableName = TABLE_NAME;
    }
    
    public EngineNode(String id, String name, String host, int status, String statusDesc, int statusLock, Long load) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.status = status;
        this.statusDesc = statusDesc;
        this.statusLock = statusLock;
        this.load = load;
        tableName = TABLE_NAME;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public Integer getStatusLock() {
        return statusLock;
    }

    public void setStatusLock(Integer statusLock) {
        this.statusLock = statusLock;
    }

    public Date getTouchTime() {
        return touchTime;
    }

    public void setTouchTime(Date touchTime) {
        this.touchTime = touchTime;
    }

    public Long getLoad() {
		return load;
	}

	public void setLoad(Long load) {
		this.load = load;
	}

	public Long getLoading() {
        return loading;
    }

    public void setLoading(Long loading) {
        this.loading = loading;
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
    public JSONObject toJSONObject() {
        JSONObject jo = super.toJSONObject();
        jo.remove("apps");
        jo.remove("statusLock");
        jo.remove("touchTime");
        jo.remove("load");
        jo.remove("loading");
        return jo;
    }

    @Override
    public void prepareForSave() {
        if (status == null) {
            status = STATUS_LIVE;
            statusDesc = STATUS_DESC_LIVE;
            statusLock = STATUS_LOCK_N;
        }
        if (touchTime == null) {
            createTime = new Date();
            touchTime = createTime;
        }
        if (loading == null) {
            loading = 0L;
        }
    }
    
}
