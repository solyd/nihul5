/**
 * RegistrationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public interface RegistrationService extends java.rmi.Remote {
    public java.lang.String[] getRegisteredEndpoints() throws java.rmi.RemoteException;
    public java.lang.String addEndpoint(java.lang.String url) throws java.rmi.RemoteException;
    public java.lang.String deleteEndpoint(java.lang.String url) throws java.rmi.RemoteException;
}
