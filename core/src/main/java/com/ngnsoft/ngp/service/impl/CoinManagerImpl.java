package com.ngnsoft.ngp.service.impl;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.CoinHistory;
import com.ngnsoft.ngp.model.UserAccount;
import com.ngnsoft.ngp.service.CoinManager;

/**
 *
 * @author fcy
 */
@Service
public class CoinManagerImpl extends GenericManagerImpl implements CoinManager {

    @Override
    public Long updateCoin(Long userId, String appId, Long incrmt, String incrmtKey) {
        UserAccount ua = get(userId, UserAccount.class);
        if (ua == null) {
            ua = new UserAccount(userId);
            if (incrmt > 0) {
                ua.setCoin(incrmt);
                CoinHistory ch = new CoinHistory(null, userId, appId, incrmt, incrmtKey);
                save(ch);
            } else {
                ua.setCoin(0L);
            }
        } else {
            if (incrmt != 0) {
                CoinHistory ch = new CoinHistory(null, userId, appId, incrmt, incrmtKey);
                save(ch);
                Long upCoin = ua.getCoin() + incrmt;
                if (upCoin < 0) {
                    ch = new CoinHistory(null, userId, appId, -1 * upCoin, "reset");
                    save(ch);
                    ua.setCoin(0L);
                } else {
                    ua.setCoin(upCoin);
                }
                update(ua);
            }
        }
        return ua.getCoin();
    }
}
