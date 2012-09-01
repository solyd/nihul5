package org.nihul5.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class EventRegisteredUsers
 */
@WebServlet("/EventRegisteredUsers")
public class EventRegisteredUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventRegisteredUsers.class);
	
	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventRegisteredUsers() {
        super();
        // TODO Auto-generated constructor stub
    }
        
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	_storage = (Storage) getServletContext().getAttribute(CONST.STORAGE);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String eventid_str = request.getParameter(CONST.EVENT_ID);
		int eventid = -1;
		try {
			eventid = Integer.valueOf(eventid_str);
		}
		catch (Exception e) {
			Utility.writeResponse(response, false, "Invalid eventid");
			return;
		}
		
		StringBuilder users = new StringBuilder();
		users.append("[");
		
		List<User> regUsers = _storage.getEventRegisteredUsers(eventid);
		List<String> regnames = new ArrayList<String>();
		for (User u : regUsers) 
			regnames.add(u.username);
		request.setAttribute(CONST.USERS_LIST, regnames);
		getServletContext().getRequestDispatcher("/jsp/users/user_list.jsp").forward(request, response);
		
		/*
		for (User u : regUsers) {
			users.append("'" + u.username + "',");
		}
		users.deleteCharAt(users.length() - 1);
		users.append("]");
		
		Utility.writeResponse(response, true, users.toString());
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
