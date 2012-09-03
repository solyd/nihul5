/**
 * SearchWSSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.xml.ws.WebServiceContext;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.nihul5.other.CONST;
import org.nihul5.other.Message;
import org.nihul5.other.Storage;



@WebService
public class SearchWSSoapBindingImpl implements cs236369.hw5.SearchWS{
	@Resource
	private WebServiceContext context;


    public java.lang.String[] searchLocal(double locationX, double locationY, double d) throws java.rmi.RemoteException {
    	MessageContext msgContext = MessageContext.getCurrentContext();
    	ServletContext servletContext =((HttpServlet)msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();

    	Storage storage = (Storage) servletContext.getAttribute(CONST.STORAGE);
    	if (storage == null)
    		return null;
    	
    	List<Message> messages = storage.searchMessages(locationX, locationY, d);
    	if (messages == null)
    		return null;
    	
    	StringBuilder sb;
    	List<String> res = new ArrayList<String>();
    	for (Message m : messages) {
    		sb = new StringBuilder();
    		sb.append(m.lng);
    		sb.append("||");
    		sb.append(m.lat);
    		sb.append("||");
    		sb.append(m.content);
    		
    		res.add(sb.toString());
    	}
    	
        return res.toArray(new String[0]);
    }

    public java.lang.String[] searchKeywords(java.lang.String[] keywords) throws java.rmi.RemoteException {
        return null;
    }
}
