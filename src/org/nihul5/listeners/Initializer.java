package org.nihul5.listeners;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.nihul5.other.CONST;
import org.nihul5.other.MySQLStorage;
import org.nihul5.other.MySQLStorageTest;
import org.nihul5.other.Storage;

/**
 * Application Lifecycle Listener implementation class LoggerInit
 *
 */
@WebListener
public class Initializer implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(Initializer.class);
    /**
     * Default constructor. 
     */
    public Initializer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	System.out.println("contextInitialized()");
    	
    	String log4jPropsLocation = arg0.getServletContext().getRealPath("/") + "WEB-INF/log4j.properties";
    	initLogger(log4jPropsLocation);
    	
    	logger.info("log4j init complete");
    	
    	DataSource ds = null;
		try {
			Context context = (Context) new InitialContext().lookup("java:comp/env");
			ds = (DataSource) context.lookup(CONST.DB_NAME);
		} catch (NamingException e) {
			logger.error("Can't get connection pool object", e);
		}

    	Storage storage = new MySQLStorage(ds);
    	storage.init();
    	arg0.getServletContext().setAttribute(CONST.STORAGE, storage);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("contextDestroted()");
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private void initLogger(String configFileLocation) {
    	File log4jprops = new File(configFileLocation);
    	if (log4jprops.exists()) {
    		PropertyConfigurator.configure(configFileLocation);
    	}
    	else {
    		System.err.println("[ERROR] " + configFileLocation + " file not found, so initializing log4j with BasicConfigurator");
    		BasicConfigurator.configure();
    	}
    }
}
