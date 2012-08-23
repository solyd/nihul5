package org.nihul5.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nihul5.other.CONST;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// User is already logged in, prevent from logging in twice
		if (request.getUserPrincipal() != null)
			return;
		
		request.setAttribute(CONST.LOGIN_CLICKED, "true");
		getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(request, response);
	}

	/**
	 * @SEE HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// We get here if the user pressed the login button explicitly
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		
		if (!Utility.verifyAlphaNumeric(username, CONST.MAX_USERNAME_LEN) ||
				!Utility.verifyAlphaNumeric(password, CONST.MAX_PASSWORD_LEN)) {
			response.sendRedirect("/jsp/login_failed.jsp");
			return;
		}
			
		request.login(username, password);
	}

}
