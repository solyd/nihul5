package org.nihul5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;

import cs236369.hw5.RegistrationServiceProxy;
import cs236369.hw5.RegistrationServiceSoapBindingStub;

/**
 * Servlet implementation class YellowPages
 */
@WebServlet("/yellow_pages")
public class YellowPages extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TestServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	public YellowPages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			
			RegistrationServiceProxy proxy = new RegistrationServiceProxy();
			
			String[] regEndpoints = proxy.getRegisteredEndpoints();
			
			request.setAttribute(CONST.YELLOW_PAGES_ENDPOINTS, regEndpoints);
			
			getServletContext().getRequestDispatcher("/jsp/yellow_pages.jsp").forward(request, response);
			
		}
		catch (Exception e) {
			logger.error("Problem with yellow pages proxy", e);
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
