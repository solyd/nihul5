package org.nihul5.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Storage;
import org.nihul5.other.Utility;

/**
 * Servlet implementation class KMLExport
 */
@WebServlet("/KMLExport/*")
public class KMLExport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Export.class);

	private Storage _storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KMLExport() {
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
		String[] reqparts = request.getRequestURI().split("/");
		String type = reqparts[reqparts.length - 1];

		File msgXml;
		try {
			msgXml = Utility.generateXMLExport(_storage);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to generate message xml");
		}
		
		response.setContentType("application/force-download");
		response.setHeader("Content-Transfer-Encoding", "binary");
		String absoluteXsltPath = "";
		if (type.equals(CONST.KML_EXPORT_POST)) {
			absoluteXsltPath = getServletContext().getRealPath(CONST.POST_XSL_PATH);
			response.setHeader("Content-Disposition","attachment; filename=\"posts" + ".kml\"");
		}
		else if (type.equals(CONST.KML_EXPORT_EVENT)) {
			absoluteXsltPath = getServletContext().getRealPath(CONST.EVENT_XSL_PATH);
			response.setHeader("Content-Disposition","attachment; filename=\"events" + ".kml\"");
		}
		else if (type.equals(CONST.KML_EXPORT_PATH)) {
			absoluteXsltPath = getServletContext().getRealPath(CONST.MESSAGES_XSL_PATH);
			response.setHeader("Content-Disposition","attachment; filename=\"path" + ".kml\"");
		}
		else {
			throw new IllegalArgumentException("Invalid KML export type");
		}
		
		PrintWriter out = response.getWriter();

		try {
			File xsltFile = new File(absoluteXsltPath);
			javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(msgXml);
			javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
			javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(out);
			// create an instance of TransformerFactory
			javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
			javax.xml.transform.Transformer trans = transFact.newTransformer(xsltSource);
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.transform(xmlSource, result);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed generating KML");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
