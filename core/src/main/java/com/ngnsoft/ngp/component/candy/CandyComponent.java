package com.ngnsoft.ngp.component.candy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.model.AppData;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.model.SaleHistory;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.UserDataManager;

/**
 *
 * @author fcy
 */
public class CandyComponent extends AppComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(CandyComponent.class);
    
    public static final String APP_BASE_ID = "100003";
    
    private static final String DB_NAME = "candy";
    
    @Override
    public String getDbName() {
        return DB_NAME;
    }
    
    @Override
    public String getDbName(BaseModel baseModel) {
        if (baseModel instanceof Sale || baseModel instanceof SaleHistory) {
            return DB_NAME;
        } else {
            return super.getDbName();
        }
    }
    
    @Override
    public UserAppData getUadModel(Long userId) {
    	UserAppData appData = new AppData(userId);
    	appData.setDbName(getDbName());
        return new AppData(userId);
    }
    
}
