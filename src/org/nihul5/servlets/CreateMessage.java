package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Date;
import java.util.GregorianCalendar;

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
 * Servlet implementation class CreateEvent
 */
@WebServlet("/messages/create")
public class CreateMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CreateMessage.class);
	
	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMessage() {
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
		// Forward to the jsp page
		
		getServletContext().getRequestDispatcher("/jsp/messages/create_message.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Input: message type + creation info
		
		// TODO check if principal is null return error if so
		String owner = request.getUserPrincipal().getName();
		Principal s;

		
		String[] outerArray = request.getParameterValues(CONST.EVENT_CONSENSUSES);
		String[] innerArray=outerArray[0].split(",");

		
		
		String title = request.getParameter(CONST.MSG_TITLE);
		String lat = request.getParameter(CONST.MSG_LATITUDE);
		String lng = request.getParameter(CONST.MSG_LONGITUDE);
		String content = request.getParameter(CONST.MSG_CONTENT);
		String creationDate = request.getParameter(CONST.MSG_CREATION_TIME);
		
	}

	//"2012 08 26 03 49 18"
	private Date extractDate(String strdate) {
		int year = Integer.valueOf(strdate.substring(0, 4));
		int month = Integer.valueOf(strdate.substring(4, 6));
		int day = Integer.valueOf(strdate.substring(6, 8));
		int hour = Integer.valueOf(strdate.substring(8, 10));
		int min = Integer.valueOf(strdate.substring(10, 12));
		int sec = Integer.valueOf(strdate.substring(12, 14));
		
		return new GregorianCalendar(year, month, day, hour, min, sec).getTime();
	}
}
