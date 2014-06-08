package com.ngnsoft.ngp.service;

/**
 *
 * @author fcy
 */
public interface CoinManager extends GenericManager {
    
    public Long updateCoin(Long userId, String appId, Long incrmt, String incrmtKey);
    
}
