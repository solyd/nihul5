package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
 * Servlet implementation class SearchMessage
 */
@WebServlet("/message/search")
public class SearchMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MessageInfo.class);

	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMessage() {
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
		getServletContext().getRequestDispatcher("/jsp/messages/graphic_search.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String lat_str = request.getParameter(CONST.MSG_LATITUDE);
		String lng_str = request.getParameter(CONST.MSG_LONGITUDE);
		String radius_str = request.getParameter(CONST.RADIUS);
		String isjson = request.getParameter(CONST.IS_JSON);
		
		double lat = 0, lng = 0, radius = 0;
		try {
			lat = Double.valueOf(lat_str);
			lng = Double.valueOf(lng_str);
			radius = Double.valueOf(radius_str);
		}
		catch (Exception e) {
			getServletContext().getRequestDispatcher("/jsp/messages/search_results.jsp").forward(request, response);
			return;
		}
		
		List<Message> res = _storage.searchMessages(lat, lng, radius);
		if (res != null)
			request.setAttribute(CONST.MESSAGES, res);
		
		if (isjson != null && isjson.equals("true")) {
			writeJson(response, res);
			return;
		}
		
		getServletContext().getRequestDispatcher("/jsp/messages/search_results.jsp").forward(request, response);
	}

	
	private void writeJson(HttpServletResponse response, List<Message> res) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder sb = new StringBuilder();
		
		if (res == null || res.size() == 0) {
			out.println("{\"result\": [] }");
			return;
		}
		
		sb.append("{\"result\": [");
		for (Message msg : res) {
			sb.append(String.format("{\"lat\": " + msg.lat + ", \"lng\": " + msg.lng + ", \"id\": " + msg.id + "},"));
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");
		
		String r = sb.toString();
		logger.debug(r);
		
		out.println(r);
	}
}
