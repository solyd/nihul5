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
 * Servlet implementation class DeleteMessage
 */
@WebServlet("/DeleteMessage")
public class DeleteMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DeleteMessage.class);
	
	private Storage _storage;

	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_storage = (Storage) getServletContext().getAttribute(CONST.STORAGE);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteMessage() {
        super();
        // TODO Auto-generated constructor stub
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
		Principal princ = request.getUserPrincipal();
		if (princ == null) {
			Utility.writeResponse(response, false, "No permission");
			return;
		}
		
		int msgid;
		try {
			msgid = Integer.valueOf(request.getParameter(CONST.MSG_ID));
		}
		catch (Exception e) {
			Utility.writeResponse(response, false, "Invalid Message id");
			return;
		}
		
		if (_storage.deleteMessage(msgid)) {
			Utility.writeResponse(response, true, "Successfully delete message");
		}
		else {
			Utility.writeResponse(response, false, "Failed to delete message");
		}
	}

}
