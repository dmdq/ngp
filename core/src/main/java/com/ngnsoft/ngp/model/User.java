package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class User extends BaseModel {
    
    public static final String PREFIX_NICK = "Player";
    
    public static final String PREFIX_NAME = "player";
    
    public static final int TYPE_ROBOT = 1;
    
    public static final int TYPE_USER = 0;
    
    public static final int STATUS_NORMAL = 0;
    
    public static final int STATUS_FORBID = 1;
    
    private Long id;
    
    private String name;
    
    private String nick;
    
    private String opswd;
    
    private String epswd;
    
    private Integer type;
    
    private Integer status;
    
    private String statusDetail;
    
    private UserAccount userAccount;
    
	public User() {
        
    }
    
    public User(Long id) {
        this.id = id;
    }
    
    public User(Long id, String name, String nick, String opswd, Integer type) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.opswd = opswd;
        this.type = type;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOpswd() {
        return opswd;
    }

    public void setOpswd(String opswd) {
        this.opswd = opswd;
    }

    public String getEpswd() {
        return epswd;
    }

    public void setEpswd(String epswd) {
        this.epswd = epswd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        if (status == null) {
            status = STATUS_NORMAL;
        }
    }
    
}
