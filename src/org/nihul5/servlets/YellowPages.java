package org.nihul5.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Message;
import org.nihul5.other.Utility;

import cs236369.hw5.RegistrationService;
import cs236369.hw5.RegistrationServiceServiceLocator;
import cs236369.hw5.SearchWS;
import cs236369.hw5.SearchWSServiceLocator;

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
		request.setAttribute(CONST.YELLOW_PAGES_ENDPOINTS, getEndpoints());
		getServletContext().getRequestDispatcher("/jsp/yellow_pages.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchMethod = request.getParameter(CONST.SEARCH_TYPE);
		if (searchMethod == null) {
			Utility.writeResponse(response, false, "Invalid search type");
			return;
		}
			
		List<String> wsres = new ArrayList<String>();
		if (searchMethod.equals(CONST.SEARCH_KEYWORDS)) {
			String keywords = request.getParameter(CONST.KEYWORDS);
			String[] split = keywords.split(" ");
			
			String[] endpoints = getEndpoints();
			if (endpoints == null) {
				Utility.writeResponse(response, false, "Problem contacting remote web services");
				return;
			}
			
			for (String endpoint : endpoints) {
				try {
					SearchWSServiceLocator loc = new SearchWSServiceLocator();
					SearchWS binding = loc.getSearchWS();
					
					Stub s = (Stub) binding;
					s.setTimeout(CONST.WEBSERVICE_TIMEOUT);
					s._setProperty("javax.xml.rpc.service.endpoint.address", endpoint);
					
					Object[] blabla = binding.searchKeywords(split);
					String[] stringArray = Arrays.copyOf(blabla, blabla.length, String[].class);
					wsres.addAll(Arrays.asList(stringArray));
					//wsres.addAll(Arrays.asList(binding.searchKeywords(split)));
				}
				catch (Exception e) {}
			}
		}
		else {
			String lat_str = request.getParameter(CONST.MSG_LATITUDE);
			String lng_str = request.getParameter(CONST.MSG_LONGITUDE);
			String radius_str = request.getParameter(CONST.RADIUS);
			
			
			Double lat = 0.0, lng = 0.0, radius = 0.0;
			try {
				lat = Double.valueOf(lat_str);
				lng = Double.valueOf(lng_str);
				radius = Double.valueOf(radius_str);
			}
			catch (Exception e) {
				Utility.writeResponse(response, false, "Invalid search parameters");
			}

			String[] endpoints = getEndpoints();
			if (endpoints == null) {
				Utility.writeResponse(response, false, "Problem contacting remote web services");
				return;
			}
			
			for (String endpoint : endpoints) {
				logger.info("Trying enpoint: " + endpoint);
				try {
					SearchWSServiceLocator loc = new SearchWSServiceLocator();
					SearchWS binding = loc.getSearchWS();

					Stub s = (Stub) binding;
					s.setTimeout(CONST.WEBSERVICE_TIMEOUT);
					s._setProperty("javax.xml.rpc.service.endpoint.address", endpoint);
					
					Object[] blabla = binding.searchLocal(lat, lng, radius);
					String[] stringArray = Arrays.copyOf(blabla, blabla.length, String[].class);
					wsres.addAll(Arrays.asList(stringArray));
					//wsres.addAll(Arrays.asList(binding.searchLocal(lat, lng, radius)));
				}
				catch (Exception e) { logger.info("", e); }
			}
		}
		
		List<Message> res = new ArrayList<Message>();
		for (String s : wsres) {
			Message msg = new Message();
			String[] parts = s.split("\\|\\|");
			if (parts.length != 3)
				continue;
			
			try {
				msg.lng = Double.valueOf(parts[0]);
				msg.lat = Double.valueOf(parts[1]);
				msg.content = parts[2];
			}
			catch (Exception e) {
				continue;
			}

			res.add(msg);
		}
		
		request.setAttribute(CONST.MESSAGES, res);
		getServletContext().getRequestDispatcher("/jsp/yellow_pages_results.jsp").forward(request, response);
	}

	private String[] getEndpoints() {
		try {

			RegistrationServiceServiceLocator loc = new RegistrationServiceServiceLocator();
    		RegistrationService binding = loc.getRegistrationService();

    		Stub s = (Stub) binding;
    		s.setTimeout(CONST.WEBSERVICE_TIMEOUT);
			
    		return binding.getRegisteredEndpoints();
		}
		catch (Exception e) {
			logger.error("Problem with yellow pages proxy", e);
			return null;
		}
	}
}
