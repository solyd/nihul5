package org.nihul5.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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

import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamSource; 
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Consensus;
import org.nihul5.other.Message;
import org.nihul5.other.Message.MessageType;
import org.nihul5.other.Storage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



/**
 * Servlet implementation class GetKML
 */
@WebServlet("/GetKML")
public class GetKML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GetKML.class);

	private Storage _storage;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetKML() {
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
		List<Message> messages = _storage.getAllMessages();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("app306932039");
			doc.appendChild(rootElement);
	 
			for (Message msg : messages) {
				if (msg.type == MessageType.EVENT) {
					Element event = doc.createElement("event");
					rootElement.appendChild(event);
					event.setAttribute("id", Integer.toString(msg.id));
					
					createPostXml(doc, msg, event);
					
					Element event_date = doc.createElement("event_date");
					event_date.appendChild(doc.createTextNode(new Date(msg.eventTime).toString()));
					event.appendChild(event_date);
					
					Element capacity = doc.createElement("capacity");
					capacity.appendChild(doc.createTextNode(Integer.toString(msg.capacity)));
					event.appendChild(capacity);
					
					Element num_of_registered = doc.createElement("num_of_registered");
					num_of_registered.appendChild(doc.createTextNode(Integer.toString(msg.nSubs)));
					event.appendChild(num_of_registered);
					
					Element registered_users = doc.createElement("registered_users");
					event.appendChild(registered_users);
					
					//User user; add all registered users
					for (String username : msg.registeredUsers) {
						Element reg_username = doc.createElement("username");
						reg_username.appendChild(doc.createTextNode(username));
						registered_users.appendChild(reg_username);
					}

					Element consensus_reqs = doc.createElement("consensus_reqs");
					event.appendChild(consensus_reqs);
					
					List<Consensus> consensuses = msg.consReqList;
					for (Consensus cons : consensuses){
						Element consensus = doc.createElement("consensus");
						consensus_reqs.appendChild(consensus);
						consensus.setAttribute("id", Integer.toString(cons.id));
						
						Element description = doc.createElement("description");
						description.appendChild(doc.createTextNode(cons.desc));
						consensus.appendChild(description);
						
						Element status = doc.createElement("status");
						status.appendChild(doc.createTextNode(cons.status.toString()));
						consensus.appendChild(status);
						
						Element votes_for_change = doc.createElement("votes_for_change");
						votes_for_change.appendChild(doc.createTextNode(Integer.toString(cons.nvotesForChange)));
						consensus.appendChild(votes_for_change);
					}
				}
			}
			
			for (Message msg : messages) {
				if (msg.type == MessageType.POST){
					Element post = doc.createElement("post");
					rootElement.appendChild(post);
					post.setAttribute("id", Integer.toString(msg.id));
					
					createPostXml(doc, msg, post);
				}
			}
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, CONST.HOST + "/" + CONST.WEBAPP_NAME + "/" + CONST.DTD_FILE_NAME);

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\file.xml"));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			
			
			
/*			File xmlFile = new File(args[0]);
			File xsltFile = new File(args[1]);
			 
			    javax.xml.transform.Source xmlSource =
			        new javax.xml.transform.stream.StreamSource(xmlFile);
			    javax.xml.transform.Source xsltSource =
			        new javax.xml.transform.stream.StreamSource(xsltFile);
			    javax.xml.transform.Result result =
			        new javax.xml.transform.stream.StreamResult(System.out);
			 
			    // create an instance of TransformerFactory
			    javax.xml.transform.TransformerFactory transFact =
			        javax.xml.transform.TransformerFactory.newInstance( );
			 
			    javax.xml.transform.Transformer trans =
			        transFact.newTransformer(xsltSource);
			 
			    trans.transform(xmlSource, result);*/
			      
			    
			    
			    

	 
			System.out.println("File saved!");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			//TODO
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
			//TODO
		  }
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void createPostXml(Document doc, Message msg, Element post){
		Element username = doc.createElement("username");
		username.appendChild(doc.createTextNode(msg.username));
		post.appendChild(username);
		
		Element lat = doc.createElement("lat");
		lat.appendChild(doc.createTextNode(Double.toString(msg.lat)));
		post.appendChild(lat);
		
		Element lng = doc.createElement("lng");
		lng.appendChild(doc.createTextNode(Double.toString(msg.lng)));
		post.appendChild(lng);
		
		Element creation_date = doc.createElement("creation_date");
		creation_date.appendChild(doc.createTextNode(new Date(msg.creationTime).toString()));
		post.appendChild(creation_date);
		
		Element title = doc.createElement("title");
		title.appendChild(doc.createTextNode(msg.title));
		post.appendChild(title);
		
		Element content = doc.createElement("content");
		content.appendChild(doc.createTextNode(msg.content));
		post.appendChild(content);
	}
	
	private void DOMValidateDTD(){
		try{
			  DocumentBuilderFactory factory = 
			  DocumentBuilderFactory.newInstance();
			  factory.setValidating(true);
			  DocumentBuilder builder = factory.newDocumentBuilder();
			  builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
			  //Ignore the fatal errors
			  public void fatalError(SAXParseException exception)
			   throws SAXException { }
			  //Validation errors 
			  public void error(SAXParseException e)
			  throws SAXParseException {
			  System.out.println("Error at " +e.getLineNumber() + " line.");
			  System.out.println(e.getMessage());
			  System.exit(0);
			  }
			  //Show warnings
			  public void warning(SAXParseException err)
			  throws SAXParseException{
			  System.out.println(err.getMessage());
			  System.exit(0);
			  }
			  });
			  Document xmlDocument = builder.parse(
			   new FileInputStream("Employeexy.xml"));
			  DOMSource source = new DOMSource(xmlDocument);
			  StreamResult result = new StreamResult(System.out);
			  TransformerFactory tf = TransformerFactory.newInstance();
			  Transformer transformer = tf.newTransformer();
			  transformer.setOutputProperty(
			  OutputKeys.DOCTYPE_SYSTEM, "Employee.dtd");
			  transformer.transform(source, result);
			  }
			  catch (Exception e) {
			  System.out.println(e.getMessage());
			  }
	}

}
