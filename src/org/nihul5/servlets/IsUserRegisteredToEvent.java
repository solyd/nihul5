package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Message;
import org.nihul5.other.Storage;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class IsUserRegisteredToEvent
 */
@WebServlet("/IsUserRegisteredToEvent")
public class IsUserRegisteredToEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RegisterToEvent.class);
	
	private Storage _storage;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsUserRegisteredToEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	_storage = (Storage) getServletContext().getAttribute(CONST.STORAGE);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter(CONST.USERNAME);
		String msgId = request.getParameter(CONST.MSG_ID);
	    
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		response.setContentType("application/json; charset=UTF-8");
		
    	if ((userName == null) || (msgId == null)) {
    		logger.info("userName is " + userName + ", Message ID is: " + msgId);
    		Utility.writeResponse(response, false, "Bad Parameters");
    		//out.print("{\"result\":\"false\"}");
    		return;
    	}

    	int eventid = -1;
    	try { 
    		eventid = Integer.valueOf(msgId);
    	} 
    	catch (Exception e) { 
    		Utility.writeResponse(response, false, "Can't parse msgid to int");
    		return;
    	}
    	
    	if (_storage.isUserRegisteredToEvent(userName, eventid)) {
    		logger.info("User " + userName + " is already registered to message" + eventid);
    		Utility.writeResponse(response, true, userName + " is registered to " + eventid);    		
    	}
    	else {
    		logger.info("User " + userName + " is not registered to message" + eventid);
    		Utility.writeResponse(response, false, userName + " is not registered to " + eventid);
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
