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
import org.nihul5.other.Message;
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
		String[] parts = request.getRequestURI().split("/");
		int msgid = -1;
		Message msg = null;

		try {
			msgid = Integer.valueOf(parts[parts.length - 1]);
		}
		catch (Exception e) {
			// TODO 
		}
		
		msg = _storage.getMessage(msgid);
		request.setAttribute(CONST.MSG, msg);
		getServletContext().getRequestDispatcher("/jsp/messages/message_info.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
