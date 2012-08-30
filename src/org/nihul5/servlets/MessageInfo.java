package org.nihul5.servlets;

import java.io.IOException;
import java.sql.Date;

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
 * Servlet implementation class MessageInfo
 */
@WebServlet("/message/info/*")
public class MessageInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserProfile.class);
	
	private Storage _storage;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageInfo() {
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
		String msgId = uriparts[uriparts.length - 1];
		
		logger.debug("user id: " + msgId);
		_storage.getMessage(msgId);
		getServletContext().getRequestDispatcher("/jsp/messages/message_info.jsp").forward(request, response);
		// Input: msgid in the url
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
