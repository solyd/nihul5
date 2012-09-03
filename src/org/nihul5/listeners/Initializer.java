package org.nihul5.listeners;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.axis.client.Stub;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.nihul5.other.CONST;
import org.nihul5.other.MySQLStorage;
import org.nihul5.other.Storage;

import cs236369.hw5.RegistrationService;
import cs236369.hw5.RegistrationServiceProxy;
import cs236369.hw5.RegistrationServiceServiceLocator;

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
    public void contextInitialized(ServletContextEvent event) {
    	System.out.println("contextInitialized()");
    	
    	String log4jPropsLocation = event.getServletContext().getRealPath("/") + "WEB-INF/log4j.properties";
    	initLogger(log4jPropsLocation);
    	logger.info("log4j init complete");

    	
    	initDB(event);
    	if (!CONST.DEBUG_MODE)
    		registerSearchWS();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("contextDestroted()");
    	if (!CONST.DEBUG_MODE)
    		unregisterSearchWS();
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
    
    private void initDB(ServletContextEvent event) {
    	DataSource ds = null;
    	try {
    		Context context = (Context) new InitialContext().lookup("java:comp/env");
    		ds = (DataSource) context.lookup(CONST.DB_NAME);
    	} catch (NamingException e) {
    		logger.error("Can't get connection pool object", e);
    	}

    	Storage storage = new MySQLStorage(ds);
    	storage.init();
    	event.getServletContext().setAttribute(CONST.STORAGE, storage);    	
    }
    
    private void registerSearchWS() {
    	try {
    		RegistrationServiceServiceLocator loc = new RegistrationServiceServiceLocator();
    		RegistrationService binding = loc.getRegistrationService();
    		
    		Stub s = (Stub) binding;
    		s.setTimeout(CONST.WEBSERVICE_TIMEOUT);
    		
    		binding.addEndpoint(CONST.HOST + CONST.WEBAPP_NAME + "/services/SearchWS");
    		
    		//RegistrationServiceProxy proxy = new RegistrationServiceProxy();
    		//proxy.addEndpoint(CONST.HOST+ CONST.WEBAPP_NAME + "/services/SearchWS");
    	}
    	catch (Exception e) {
    		logger.error("Failed to register search web service with the registration service", e);
    	}

    	logger.info("init complete");    	
    }
    
    private void unregisterSearchWS() {
    	try {
    		RegistrationServiceServiceLocator loc = new RegistrationServiceServiceLocator();
    		RegistrationService binding = loc.getRegistrationService();

    		Stub s = (Stub) binding;
    		s.setTimeout(CONST.WEBSERVICE_TIMEOUT);

    		binding.deleteEndpoint(CONST.HOST+ CONST.WEBAPP_NAME + "/services/SearchWS");
    		
    		
    		//RegistrationServiceProxy proxy = new RegistrationServiceProxy();
    		//proxy.deleteEndpoint(CONST.HOST+ CONST.WEBAPP_NAME + "/services/SearchWS");
    	}
    	catch (Exception e) {
    		logger.error("Failed to remove registeration of search web service with the registration service", e);
    	}    	
    	logger.info("tear down complete");
    }
}
