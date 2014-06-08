package com.ngnsoft.ngp.component.dragon.model;

import com.ngnsoft.ngp.model.UserActivityHistory;

/**
 *
 * @author fcy
 */
public class DragonUserActivityHistory extends UserActivityHistory {

    @Override
    public String getDbName() {
        return "dragon";
    }

    @Override
    public String getTableName() {
        return "user_activity_history";
    }

    @Override
    public String getModelName() {
        return "UserActivityHistory";
    }
    
}
