package org.nihul5.servlets;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Message;
import org.nihul5.other.Message.MessageType;
import org.nihul5.other.Storage;
import org.nihul5.other.Utility;

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
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		logger.debug("Current time: \t\t" + new Date(c.getTimeInMillis()).toLocaleString());
		
		// Input: message type + creation info
		Principal princ = request.getUserPrincipal();
		if (princ == null)
			return;

		Message msg = new Message();
		msg.username = princ.getName();
		msg.title = request.getParameter(CONST.MSG_TITLE);
		try {
			
			 msg.lat = Double.valueOf(request.getParameter(CONST.MSG_LATITUDE));
			 msg.lng = Double.valueOf(request.getParameter(CONST.MSG_LONGITUDE));
			 logger.debug("lat: " + msg.lat + ", lng: " + msg.lng);
		}
		catch (Exception e) {
			logger.error("couldn't parse double values for lat and lng on create msg");
			Utility.writeResponse(response, false, "Invalid lat/lng");
			return;
		}

		msg.content = request.getParameter(CONST.MSG_CONTENT);
		msg.creationTime = Long.valueOf(request.getParameter(CONST.MSG_CREATION_TIME));
		
		logger.debug("creation time: \t\t" + new Date(msg.creationTime).toLocaleString());
		
		msg.type = Message.stringToType(request.getParameter(CONST.MSG_TYPE));
		if (msg.type == MessageType.EVENT) {
			try {
				msg.eventTime = Long.valueOf(request.getParameter(CONST.EVENT_DATE));
			} 
			catch (Exception e) {
				logger.error("Can't parse event date");
				Utility.writeResponse(response, false, "Invalid event date");
				return;
			}
			
			logger.debug("event time: \t\t" + new Date(msg.eventTime).toLocaleString());
			
			long currDate = new GregorianCalendar().getTimeInMillis();
			if (msg.eventTime < currDate) {
				logger.error("Can't add event that has a dead line in the past.");
				Utility.writeResponse(response, false, "Event date is set in the past");
				return;
			}
			try {
				msg.capacity = Integer.valueOf(request.getParameter(CONST.EVENT_CAPACITY));
				if (msg.capacity <= 0)
					throw new Exception("catch meeeee");
			}
			catch (Exception e) {
				logger.error("Coulnd't parse capacity on create event");
				Utility.writeResponse(response, false, "Invalid capacity");
				return;
			}
			
			String[] outerArray = request.getParameterValues(CONST.EVENT_CONSENSUSES);
			String[] innerArray = outerArray[0].split(",");
			for (String cons : innerArray)
				msg.consensusDescList.add(cons);
		}
		
		if (_storage.saveMessage(msg)) {
			Utility.writeResponse(response, true, "Message created");
		}
		else {
			Utility.writeResponse(response, false, "Error in message creation. Please try again");
		}
	}

	//"2012 08 26 03 49 18"
	private long extractTime(String strdate) {
		int year = Integer.valueOf(strdate.substring(0, 4));
		int month = Integer.valueOf(strdate.substring(4, 6));
		int day = Integer.valueOf(strdate.substring(6, 8));
		int hour = Integer.valueOf(strdate.substring(8, 10));
		int min = Integer.valueOf(strdate.substring(10, 12));
		int sec = Integer.valueOf(strdate.substring(12, 14));
		
		// lul
		return new GregorianCalendar(year, month, day, hour, min, sec).getTimeInMillis();
		//return new GregorianCalendar(year, month, day, hour, min, sec).getTime();
	}
}



