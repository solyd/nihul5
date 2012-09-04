package org.nihul5.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cs236369.hw5.RegistrationServiceProxy;
import cs236369.hw5.RegistrationServiceSoapBindingStub;



/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TestServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    																		throws ServletException,
    																		IOException {
    	// TODO Auto-generated method stub
    	super.doGet(req, resp);

    	// TODO remove
    	/*
    	Storage storage = (Storage) getServletContext().getAttribute(CONST.STORAGE);
    	MySQLStorageTest dbtest = new MySQLStorageTest((MySQLStorage) storage);
    	dbtest.run();
    	*/
    	
    	try {


    		
    	}

    	catch (Exception e) {      
    		logger.error("", e);
    	}


    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(req.getRequestURI());

		Enumeration<String> paramEnum = req.getParameterNames();

		while (paramEnum.hasMoreElements()) {
			String paramName = (String) paramEnum.nextElement();
			String paramValue = req.getParameter(paramName);
			sb.append("\n\t" + paramName + " " + paramValue);
		}

		String[] lol = req.getParameterValues("fljewfjew");

		logger.info(sb.toString());
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.println("{\"result\":\"success\"}");
		out.flush();
		out.close();
	}
}


