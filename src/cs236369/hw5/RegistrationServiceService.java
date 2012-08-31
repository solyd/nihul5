/**
 * RegistrationServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

public interface RegistrationServiceService extends javax.xml.rpc.Service {
    public java.lang.String getRegistrationServiceAddress();

    public cs236369.hw5.RegistrationService getRegistrationService() throws javax.xml.rpc.ServiceException;

    public cs236369.hw5.RegistrationService getRegistrationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
