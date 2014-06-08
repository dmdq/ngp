package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.UserActivityHistory;

/**
 *
 * @author fcy
 */
public class CocUserActivityHistory extends UserActivityHistory {

    @Override
    public String getDbName() {
        return "coc";
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
