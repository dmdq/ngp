package com.ngnsoft.ngp.model;

import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class UserRoleData {
    
    private Long userId;
    
    private RoleData roleData;
    
    public UserRoleData() {
        
    }
    
    public UserRoleData(Long userId) {
        this.userId = userId;
    }
    
    public UserRoleData(Long userId, RoleData roleData) {
        this.userId = userId;
        this.roleData = roleData;
    }

    public RoleData getRoleData() {
        return roleData;
    }

    public void setRoleData(RoleData roleData) {
        this.roleData = roleData;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public JSONObject toJSONObject() {
        if (roleData != null) {
            JSONObject jo = roleData.toJSONObject();
            jo.put("userId", userId);
            return jo;
        }
        return null;     
    }
    
}
