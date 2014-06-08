package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.Message;

/**
 *
 * @author fcy
 */
public class CocMessage extends Message {
    
    public static final int TO_GLOBAL = 0;
    public static final int TO_CLAN = 1;
    public static final int TO_CLAN_MEMBER = 2;
    
    public static final int ATTACH_APPLY_JOIN_TYPE = 0;
    public static final int ATTACH_HAD_JOIN_TYPE = 1;
    public static final int ATTACH_EXIT_TYPE = 2; 
    public static final int ATTACH_CICKED_TYPE = 3;
    public static final int ATTACH_SURGE_TYPE = 4;
    public static final int ATTACH_ROLE_TYPE = 5;

    @Override
    public String getDbName() {
        return "coc";
    }

    @Override
    public String getTableName() {
        return "app_message";
    }
    
}
