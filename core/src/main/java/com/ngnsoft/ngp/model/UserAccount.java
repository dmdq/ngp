package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class UserAccount extends BaseModel {
    
    private Long userId;
    
    private Long coin;
    
    private Long coinReset;
    
    public UserAccount() {
        
    }
    
    public UserAccount(Long userId) {
        this.userId = userId;
    }
    
    public UserAccount(Long userId, Long coin, Long coinReset) {
        this.userId = userId;
        this.coin = coin;
        this.coinReset = coinReset;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getCoinReset() {
        return coinReset;
    }

    public void setCoinReset(Long coinReset) {
        this.coinReset = coinReset;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Serializable getPrimaryKey() {
        return userId;
    }

    @Override
    public void setPrimaryKey(Object o) {
        userId = (Long)o;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (coin == null) {
            coin = 0L;
        }
        if (coinReset == null) {
            coinReset = 0L;
        }
    }
    
}
