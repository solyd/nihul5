package org.nihul5.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Consensus;
import org.nihul5.other.Message;
import org.nihul5.other.Utility;
import org.nihul5.other.Message.MessageType;
import org.nihul5.other.Storage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Servlet implementation class XMLExport
 */
@WebServlet("/XMLExport")
public class XMLExport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Export.class);

	private Storage _storage;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public XMLExport() {
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
		File msgXml = null;
		try {
			msgXml = Utility.generateXMLExport(_storage);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to generate messages xml");
		}
		response.setContentType("application/force-download");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Length", String.valueOf(msgXml.length()));
		response.setHeader("Content-Disposition","attachment; filename=\"messages" + ".xml\"");

		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(msgXml));
		byte[] buff = new byte[4096];
		int bytesRead = 0;
		
		while ((bytesRead = in.read(buff)) > 0) {
			out.write(buff, 0, bytesRead);
		}
		
		in.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}
}
