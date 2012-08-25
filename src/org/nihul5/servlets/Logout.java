package org.nihul5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nihul5.other.CONST;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null)
			session.invalidate();

		/*
		request.setAttribute(CONST.MSGBOX_TXT, "Logout successful");
		// When we logout the status bar thinks we're still logged in before refreshing
		// this is a hack around that
		request.setAttribute("logout", "true");	
		getServletContext().getRequestDispatcher("/jsp/notification_box.jsp").forward(request, response);
		*/
		getServletContext().getRequestDispatcher("/jsp/logout.jsp").forward(request, response);
	}
}
