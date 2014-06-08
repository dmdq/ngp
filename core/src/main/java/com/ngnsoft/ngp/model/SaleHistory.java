package com.ngnsoft.ngp.model;

/**
 *
 * @author fcy
 */
public class SaleHistory extends Sale {
    
    private Long userId;
    
    public SaleHistory() {
        
    }
    
    public SaleHistory(Long id) {
        this.id = id;
    }
    
    public SaleHistory(Long id, String ad, String appId, String deviceId, String pid, String jsonData, Long userId, String ip) {
        this.id = id;
        this.ad = ad;
        this.appId = appId;
        this.deviceId = deviceId;
        this.pid = pid;
        this.jsonData = jsonData;
        this.userId = userId;
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
}
