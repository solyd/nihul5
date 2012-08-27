package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.Servlet;
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
 * Servlet implementation class Register
 */
@WebServlet(description = "Handles user registration to the webapp", urlPatterns = { "/register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Register.class);
	
	Storage _storage;
			
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		getServletContext().getRequestDispatcher("/jsp/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter(CONST.USERNAME);
		String password = request.getParameter(CONST.PASSWD);
		String firstName = request.getParameter(CONST.FIRST_NAME);
		String lastName = request.getParameter(CONST.LAST_NAME);
		String email = request.getParameter(CONST.EMAIL); 
		
		// TODO using ajax, send user feedback on wrong input type for the fields
//		if (username == null || !username.matches(CONST.REGX_ALPHANUMERIC) ||
//				password == null || !password.matches(CONST.REGX_ALPHANUMERIC)) {
//			
//		}
		
		User user = new User(username, password, firstName, lastName, email);
		String msg = null;
		switch (_storage.addUser(user)) {
		case ADDUSER_OK:
			logger.info("User " + user.toString() + " was successfully added to DB");
			msg = "Registration successful";
			request.setAttribute(CONST.REDIRECT_URL, "login.jsp");
			break;
		case ADDUSER_EXISTS:
			logger.info("User " + user.toString() + " already exists in the DB");
			msg = "A user with the same username already exists. Please pick another one.";
			request.setAttribute(CONST.REDIRECT_URL, "jsp/register.jsp");
			break;
		case ADDUSER_FAILED:
			logger.info("Add user failed");
			msg = "Registration has failed. Please try again.";
			request.setAttribute(CONST.REDIRECT_URL, "jsp/register.jsp");
			break;
		}
		request.setAttribute(CONST.MSGBOX_TXT, msg);
		getServletContext().getRequestDispatcher("/jsp/notification_box.jsp").forward(request, response);
	}

}
