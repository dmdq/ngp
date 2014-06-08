package com.ngnsoft.ngp.server;

import com.ngnsoft.ngp.Engine;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author fcy
 */
public class GameServer implements ServletContextListener, HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Engine ge = Engine.getInstance();
        ge.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Engine ge = Engine.getInstance();
        ge.destory();
    }
    
}
