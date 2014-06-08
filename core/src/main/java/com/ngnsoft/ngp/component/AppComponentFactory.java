package com.ngnsoft.ngp.component;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.model.UserAppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author fcy
 */
public class AppComponentFactory {
    
    public static String COMPONENT_PREFIX = "APP_";
    
    public static String DEFAULT_COMPONENT = "defaultComponent";
    
    private static final Logger logger = LoggerFactory.getLogger(AppComponentFactory.class);
    
    private static ApplicationContext ctx = null;
    
    public static AppComponent getComponent(String baid) {
        if (ctx == null) {
            ctx = Engine.getInstance().getCtx();
        }
        if (ctx == null) {
            return null;
        }
        AppComponent component = null;
        try {
            component = ctx.getBean(COMPONENT_PREFIX + baid, AppComponent.class);
        } catch (Exception e) {
//            logger.warn(e.getMessage());
        }
        if (component == null) {
            component = ctx.getBean(DEFAULT_COMPONENT, AppComponent.class);
        }
        component.setBaid(baid);
        return component;
    }
    
    public static UserAppData getUadModel(Long userId, String baid) {
        AppComponent component = getComponent(baid);
        return component.getUadModel(userId);
    }
    
}
