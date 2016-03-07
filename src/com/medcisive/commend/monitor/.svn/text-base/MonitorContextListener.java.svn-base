package com.medcisive.commend.monitor;

import com.medcisive.utility.UtilityFramework;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 * @author vhapalchambj
 */
public class MonitorContextListener extends UtilityFramework implements ServletContextListener {
	
	private void setProperties(ServletContext sc) {
        String propertiesFile = sc.getInitParameter("PropertiesFile");
        synchronized(this){
            if(_properties==null) {
                init2(propertiesFile);
            }
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized started!");
        setProperties(sce.getServletContext());
    }
}
