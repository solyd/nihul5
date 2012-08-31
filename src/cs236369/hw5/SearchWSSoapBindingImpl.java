/**
 * SearchWSSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cs236369.hw5;

import java.util.ArrayList;
import java.util.List;

public class SearchWSSoapBindingImpl implements cs236369.hw5.SearchWS{
    public java.lang.String[] searchLocal(double locationX, double locationY, double d) throws java.rmi.RemoteException {
        List<String> arr = new ArrayList<String>();
        
        arr.add("Moshe");
        arr.add("haya");
        arr.add("BAIT LOLLLLll");
        
        return arr.toArray(new String[0]);
    }

    public java.lang.String[] searchKeywords(java.lang.String[] keywords) throws java.rmi.RemoteException {
        return null;
    }

}
