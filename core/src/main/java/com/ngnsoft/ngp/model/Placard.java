package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Placard extends BaseModel {
    
    public static int STATUS_NORMAL = 0;
    public static int STATUS_OFF = -1;

    private Long id;
    private String title;
    private String titleSize;
    private String titleColor;
    private String body;
    private String bodySize;
    private String bodyColor;
    private Integer status;
    private String createBy;
    private Date startTime;
    private Date endTime;

    public String getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(String titleSize) {
		this.titleSize = titleSize;
	}

	public String getBodySize() {
		return bodySize;
	}

	public void setBodySize(String bodySize) {
		this.bodySize = bodySize;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        if (status == null) {
            status = STATUS_NORMAL;
        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = super.toJSONObject();
        try {
            jo.remove("createBy");
            jo.put("startTime", startTime.getTime());
            jo.put("endTime", endTime.getTime());
            jo.put("type", 0);
        } catch (JSONException ex) {
            //
        }
        return jo;
    }
}
