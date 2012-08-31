/**
 * SearchWSSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public class SearchWSSoapBindingSkeleton implements cs236369.hw5.SearchWS, org.apache.axis.wsdl.Skeleton {
    private cs236369.hw5.SearchWS impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://hw5.cs236369", "locationX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://hw5.cs236369", "locationY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://hw5.cs236369", "d"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchLocal", _params, new javax.xml.namespace.QName("http://hw5.cs236369", "searchLocalReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://hw5.cs236369", "searchLocal"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchLocal") == null) {
            _myOperations.put("searchLocal", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchLocal")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://hw5.cs236369", "keywords"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchKeywords", _params, new javax.xml.namespace.QName("http://hw5.cs236369", "searchKeywordsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://hw5.cs236369", "searchKeywords"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchKeywords") == null) {
            _myOperations.put("searchKeywords", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchKeywords")).add(_oper);
    }

    public SearchWSSoapBindingSkeleton() {
        this.impl = new cs236369.hw5.SearchWSSoapBindingImpl();
    }

    public SearchWSSoapBindingSkeleton(cs236369.hw5.SearchWS impl) {
        this.impl = impl;
    }
    public java.lang.String[] searchLocal(double locationX, double locationY, double d) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.searchLocal(locationX, locationY, d);
        return ret;
    }

    public java.lang.String[] searchKeywords(java.lang.String[] keywords) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.searchKeywords(keywords);
        return ret;
    }

}
