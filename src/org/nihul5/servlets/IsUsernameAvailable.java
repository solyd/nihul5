package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Storage;
import org.nihul5.other.User;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class IsUsernameAvailable
 */
@WebServlet("/IsUsernameAvailable")
public class IsUsernameAvailable extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(CreateUser.class);
	Storage _storage;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsUsernameAvailable() {
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
	    
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		response.setContentType("application/json; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
        
    	if (userName == null) {
    		logger.info("User is null");
    		out.print("{\"result\":\"false\"}");
    	}
		else if (_storage.getUser(userName) != null){
	    	logger.info("User " + userName.toString() + " already taken");
	    	out.println("{\"result\":\"false\"}");
	    }
	    else{
	    	logger.info("User " + userName.toString() + " is available");
	    	out.println("{\"result\":\"true\"}");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
