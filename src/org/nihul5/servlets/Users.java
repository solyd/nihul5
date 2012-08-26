package org.nihul5.servlets;

import java.io.IOException;
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

/**
 * Servlet implementation class GetUsers
 */
@WebServlet("/users")
public class Users extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Users.class);
	
	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Users() {
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
		request.setAttribute(CONST.MSGBOX_USER_INFO_TXT, "Click on a user to see his/her profile");
		getServletContext().getRequestDispatcher("/jsp/users/users.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pageNumber;
		try {
			pageNumber = Integer.valueOf(request.getParameter("page"));
		}
		catch (NumberFormatException e) {
			logger.error("Inavlid user page requested");
			return;
		}

		int offset = CONST.USERS_PER_PAGE * (pageNumber - 1);
		List<String> usersList = _storage.getUserNames(offset, CONST.USERS_PER_PAGE);

		request.setAttribute(CONST.USERS_LIST, usersList);
		getServletContext().getRequestDispatcher("/jsp/users/user_list.jsp").forward(request, response);
	}
}
