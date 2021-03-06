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
import org.nihul5.other.Message;
import org.nihul5.other.Storage;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class GetMessage
 */
@WebServlet("/GetMessage")
public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserProfile.class);

	private Storage _storage;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
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
/*		String[] parts = request.getRequestURI().split("/");
		int msgid = -1;
		Message msg = null;*/

		String messageIdString = request.getParameter(CONST.MSG_ID);
		int msgId = -1;
		Message msg = null;
		
		try {
			msgId = Integer.valueOf(messageIdString);
		}
		catch (Exception e) {
			// TODO 
		}
	
		msg = _storage.getMessage(msgId);
		if (msg == null){
			getServletContext().getRequestDispatcher("/jsp/messages/message_error.jsp").forward(request, response);
			return;
		}
		logger.info("Message number: " + msgId);
		request.setAttribute(CONST.MSG, msg);
		getServletContext().getRequestDispatcher("/jsp/messages/info.jsp").forward(request, response);
/*		request.setAttribute(CONST.MSG, msg);
		getServletContext().getRequestDispatcher("/jsp/messages/message_info.jsp").forward(request, response);*/
		//logger.debug("lat: " + Double.valueOf(msg.lat));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
