package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Storage;

/**
 * Servlet implementation class UserDelete
 */
@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DeleteUser.class);
	
	private Storage _storage;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUser() {
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
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		Principal princ = request.getUserPrincipal();
		if (princ == null) {
			logger.error("Coulnd't get user principal for user deletion");
			out.println("{\"result\":\"failed\"");
			return;
		}
		String loggedInUser = request.getUserPrincipal().getName();
		String userToDelete = request.getParameter("username");
		
		if (userToDelete == null || !loggedInUser.equals(userToDelete)) {
			logger.error("User to delete: " + userToDelete + ", Logged in user: " + loggedInUser);
			out.println("{\"result\":\"failed\"");
			return;
		}
		
		request.getSession().invalidate();
		
		if (_storage.deleteUser(userToDelete)) {
			logger.error("User deletion successful: " + userToDelete);
			out.println("{\"result\":\"success\"");
		}
		else {
			logger.error("User deletion failed: " + userToDelete);
			out.println("{\"result\":\"failed\"");
		}
		
		out.flush();
		out.close();
	}
}
