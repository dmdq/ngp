package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class RoleData extends BaseModel {
    
    public static final String TOUCH_TIME_KEY = "touchTime";
    public static final String ACTION_TIME_KEY = "actionTime";
    
    protected Long id;
    protected String baid;
    protected Integer status;
    protected String statusDetail;
    protected String jsonData;
    protected Date touchTime;
    protected Date actionTime;
    protected String nick;
    protected String avatar;
    protected String ulld;
    protected Long coin;
    protected Integer level;
    
    public RoleData() {
        
    }
    
    public RoleData(Long id) {
        this.id = id;
    }
    
    public RoleData(Long id, String baid, String jsonData) {
        this.id = id;
        this.baid = baid;
        this.jsonData = jsonData;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBaid() {
        return baid;
    }

    public void setBaid(String baid) {
        this.baid = baid;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public Date getTouchTime() {
        return touchTime;
    }

    public void setTouchTime(Date touchTime) {
        this.touchTime = touchTime;
    }

    public String getUlld() {
        return ulld;
    }

    public void setUlld(String ulld) {
        this.ulld = ulld;
    }
    
    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
            if (jsonData == null) {
                jo = new JSONObject();
            } else {
                jo = new JSONObject(jsonData);
            }
            jo.put(TOUCH_TIME_KEY, (touchTime.getTime() / 1000) * 1000);
            jo.put(ACTION_TIME_KEY, (actionTime.getTime() / 1000) * 1000);
        } catch (Exception ex) {
        }
        return jo;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (status == null) {
            status = User.STATUS_NORMAL;
        }
        if (touchTime == null) {
            touchTime = createTime;
        }
        if (actionTime == null) {
            actionTime = createTime;
        }
        if(coin == null) {
            coin = 0l;
        }
        if (level == null) {
            level = 0;
        }
        
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long) o;
    }
}
