package org.nihul5.servlets;

import java.io.IOException;

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

/**
 * Servlet implementation class UserInfo
 */
@WebServlet("/users/profile/*")
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserProfile.class);
	
	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfile() {
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
		String uri = request.getRequestURI();
		String[] uriparts = uri.split("/");
		String username = uriparts[uriparts.length - 1];
		
		User user = _storage.getUser(username);
		if (user == null)
			request.setAttribute(CONST.MSGBOX_USER_INFO_TXT, "User does not exist");
		else 
			request.setAttribute(CONST.USER, user);
		
		getServletContext().getRequestDispatcher("/jsp/users/users.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		if (username == null)
			return;
		
		User user = _storage.getUser(username);
		if (user == null)
			return;
		
		request.setAttribute(CONST.USER, user);
		getServletContext().getRequestDispatcher("/jsp/users/user_info.jsp").forward(request, response);
	}

}
