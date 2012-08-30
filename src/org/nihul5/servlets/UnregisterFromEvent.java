package org.nihul5.servlets;

import java.io.IOException;
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
import org.nihul5.other.Utility;

/**
 * Servlet implementation class UnregisterFromEvent
 */
@WebServlet("/UnregisterFromEvent")
public class UnregisterFromEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RegisterToEvent.class);
	
	private Storage _storage;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnregisterFromEvent() {
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
		// TODO Auto-generated method stub
		Principal princ = request.getUserPrincipal();
		if (princ == null)
			return;

		String username = princ.getName();
		int eventid = 0;
		try {
			eventid = Integer.valueOf(request.getParameter(CONST.EVENT_ID));
		}
		catch (Exception e) {
			Utility.writeResponse(response, false, "Invalid eventid");
		}


		if (_storage.deleteEventRegistration(eventid, username)) {
			Utility.writeResponse(response, true, "Registration deletion successful");
		}
		else {
			Utility.writeResponse(response, false, "Registration deletion failed, try again");
		}
	}
}
