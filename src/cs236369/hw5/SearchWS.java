/**
 * SearchWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public interface SearchWS extends java.rmi.Remote {
    public java.lang.String[] searchLocal(double locationX, double locationY, double d) throws java.rmi.RemoteException;
    public java.lang.String[] searchKeywords(java.lang.String[] keywords) throws java.rmi.RemoteException;
}
