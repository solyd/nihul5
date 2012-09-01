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
import org.nihul5.other.Storage;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class VoteOnConsensusReq
 */
@WebServlet("/VoteOnConsensusReq")
public class VoteOnConsensusReq extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MessageInfo.class);

	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoteOnConsensusReq() {
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
		String username = request.getParameter(CONST.USERNAME);
		String consid_str = request.getParameter(CONST.CONSENSUS_ID);
		int consid;
		
		if (username == null || consid_str == null) {
			Utility.writeResponse(response, false, "Invalid input");
			return;
		}
		
		try {
			consid = Integer.valueOf(consid_str);
		}
		catch (Exception e) {
			Utility.writeResponse(response, false, "bad id");
			return;
		}
		
		Utility.writeResponse(response, true, new Boolean(_storage.didUserVote(username, consid)).toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter(CONST.USERNAME);
		String consid_str = request.getParameter(CONST.CONSENSUS_ID);
		String eventid_str = request.getParameter(CONST.EVENT_ID);
		String vote = request.getParameter(CONST.VOTE);
		
		if (consid_str == null || vote == null) {
			Utility.writeResponse(response, false, "invalid params");
			return;
		}

		int consid = -1;
		int eventid = -1;
		try {
			consid = Integer.valueOf(consid_str);
			eventid = Integer.valueOf(eventid_str);
		}
		catch (Exception e) {
			Utility.writeResponse(response, false, "bad consid");
			return;
		}
		

		if (_storage.voteOnConsensusReq(username, eventid, consid, !vote.equals("cancel"))) {
			Utility.writeResponse(response, true, "");
		}
		else {
			Utility.writeResponse(response, false, "Event was deleted");
		}
	}
}
