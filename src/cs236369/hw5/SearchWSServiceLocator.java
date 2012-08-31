/**
 * SearchWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public class SearchWSServiceLocator extends org.apache.axis.client.Service implements cs236369.hw5.SearchWSService {

    public SearchWSServiceLocator() {
    }


    public SearchWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SearchWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SearchWS
    private java.lang.String SearchWS_address = "http://ibm411.cs.technion.ac.il/HW5_copy/services/SearchWS";

    public java.lang.String getSearchWSAddress() {
        return SearchWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SearchWSWSDDServiceName = "SearchWS";

    public java.lang.String getSearchWSWSDDServiceName() {
        return SearchWSWSDDServiceName;
    }

    public void setSearchWSWSDDServiceName(java.lang.String name) {
        SearchWSWSDDServiceName = name;
    }

    public cs236369.hw5.SearchWS getSearchWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SearchWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSearchWS(endpoint);
    }

    public cs236369.hw5.SearchWS getSearchWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cs236369.hw5.SearchWSSoapBindingStub _stub = new cs236369.hw5.SearchWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getSearchWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSearchWSEndpointAddress(java.lang.String address) {
        SearchWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cs236369.hw5.SearchWS.class.isAssignableFrom(serviceEndpointInterface)) {
                cs236369.hw5.SearchWSSoapBindingStub _stub = new cs236369.hw5.SearchWSSoapBindingStub(new java.net.URL(SearchWS_address), this);
                _stub.setPortName(getSearchWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SearchWS".equals(inputPortName)) {
            return getSearchWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://hw5.cs236369", "SearchWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://hw5.cs236369", "SearchWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SearchWS".equals(portName)) {
            setSearchWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
