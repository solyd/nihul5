package org.nihul5.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.sql.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.apache.log4j.Logger;
import org.nihul5.other.CONST;
import org.nihul5.other.Consensus;
import org.nihul5.other.Message;
import org.nihul5.other.Message.MessageType;
import org.nihul5.other.Storage;
import org.nihul5.other.User;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Servlet implementation class GetKML
 */
@WebServlet("/GetKML")
public class GetKML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MessageInfo.class);

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
				if (msg.type == MessageType.EVENT){
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
					
					Element registered_users = doc.createElement("registered_users");
					event.appendChild(registered_users);
					
					//User user; add all registered users
					List<User> users = null; //msg.
					for (User user : users){
						Element reg_username = doc.createElement("username");
						reg_username.appendChild(doc.createTextNode(user.username));
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
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\file.xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
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
		
		Element num_of_registered = doc.createElement("num_of_registered");
		num_of_registered.appendChild(doc.createTextNode(Integer.toString(msg.nSubs)));
		post.appendChild(num_of_registered);
	}

}
