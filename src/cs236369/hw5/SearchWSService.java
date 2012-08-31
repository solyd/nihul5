/**
 * SearchWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public interface SearchWSService extends javax.xml.rpc.Service {
    public java.lang.String getSearchWSAddress();

    public cs236369.hw5.SearchWS getSearchWS() throws javax.xml.rpc.ServiceException;

    public cs236369.hw5.SearchWS getSearchWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
