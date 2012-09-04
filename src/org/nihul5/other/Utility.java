package org.nihul5.other;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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
import org.nihul5.other.Message.MessageType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Utility {
	public static boolean verifyAlphaNumeric(String str, int maxLen) {
		return str != null && 
				str.length() > 0 && str.length() < maxLen && 
				str.matches(CONST.REGX_ALPHANUMERIC);
	}
	
	public static void logFile(Logger logger, File f) {
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(f);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Print the content on the console
				
				logger.info(strLine);
			}

			in.close();
		}catch (Exception e){//Catch exception if any
			logger.error("Error: " + e.getMessage());
		}
	
	}
	
	/**
	 * http://stackoverflow.com/questions/415953/generate-md5-hash-in-java/421696#421696
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5(String str) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(str.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
			hashtext = "0" + hashtext;
		}
		
		return hashtext;
	}
	
	public static String millitimeToStr(long mili) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new Date(mili));
	}
	
	public static long getCurrentTime() {
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		return c.getTimeInMillis();
	}
	
	public static void writeResponse(HttpServletResponse response, boolean success, String reason) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder sb = new StringBuilder();
		if (success)
			sb.append("{\"result\":\"success\", ");
		else
			sb.append("{\"result\":\"failure\", ");
		
		sb.append("\"reason\":\"" + reason + "\"}");
		
		out.println(sb.toString());
	}
	
	public static File generateXMLExport(Storage storage) throws IOException, ParserConfigurationException, TransformerException {
		List<Message> messages = storage.getAllMessages();

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
				event_date.appendChild(doc.createTextNode(new java.sql.Date(msg.eventTime).toString()));
				event.appendChild(event_date);

				Element capacity = doc.createElement("capacity");
				capacity.appendChild(doc.createTextNode(Integer.toString(msg.capacity)));
				event.appendChild(capacity);

				Element num_of_registered = doc.createElement("num_of_registered");
				int nsubs = msg.registeredUsers == null ? 0 : msg.registeredUsers.size();
				num_of_registered.appendChild(doc.createTextNode(Integer.toString(nsubs)));
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
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, CONST.HOST + CONST.WEBAPP_NAME + "/" + CONST.DTD_FILE_NAME);

		

		File tmpres = File.createTempFile("messages", "xml");
		tmpres.deleteOnExit();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(tmpres);

		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		return tmpres;
	}

	private static void createPostXml(Document doc, Message msg, Element post){
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
		creation_date.appendChild(doc.createTextNode(new java.sql.Date(msg.creationTime).toString()));
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
