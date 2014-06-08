package com.ngnsoft.ngp.model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Message extends BaseModel {
    
    public static final int STATUS_UNREAD = 0;
    public static final int STATUS_READ = 1;
    public static final int ATTACH_STATUS_UNDEALED = 0;
    public static final int ATTACH_STATUS_DEALED = 1;
    
    public static final int TYPE_USER = 0;
    public static final int TYPE_SYSTEM = 1;
    
    public static final int FROM_SYSTEM = 0;
    
    private static final String REDIS_PREFIX_UNREAD_NUM = "msg_unread_num_";
    
    protected Long id;
    protected Long from;
    protected Long to;
    protected String title;
    protected String body;
    protected String attach;
    protected Integer attachStatus;
    protected Integer type;
    protected Integer status;
    
    public Message() {
        
    }
    
    public Message(Long id) {
        this.id = id;
    }
    
    public Message(Long id, Long from, Long to, String title, String body, String attach, int attachStatus, int type, int status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.title = title;
        this.body = body;
        this.attach = attach;
        this.attachStatus = attachStatus;
        this.type = type;
        this.status = status;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Integer getAttachStatus() {
        return attachStatus;
    }

    public void setAttachStatus(Integer attachStatus) {
        this.attachStatus = attachStatus;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long)o;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (type == null) {
            type = TYPE_USER;
        }
        if (from == null && type == TYPE_SYSTEM) {
            from = Long.valueOf(FROM_SYSTEM);
        }
        if (attachStatus == null) {
            attachStatus = ATTACH_STATUS_UNDEALED;
        }
        if (status == null) {
            status = STATUS_UNREAD;
        }
    }
    
    public static String redisUnreadNumKey(Long userId) {
        return REDIS_PREFIX_UNREAD_NUM + userId;
    }
    
    @Override
    public JSONObject toJSONObject() {
    	JSONObject jsonObj = super.toJSONObject();
    	if(attach != null) {
    		jsonObj.put("attach", new JSONObject(attach));
    	}
    	if(createTime != null) {
    		jsonObj.put("createTime", createTime.getTime());
    	}
    	return jsonObj;
    }
    
}
