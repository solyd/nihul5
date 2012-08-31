package cs236369.hw5;

public class SearchWSProxy implements cs236369.hw5.SearchWS {
  private String _endpoint = null;
  private cs236369.hw5.SearchWS searchWS = null;
  
  public SearchWSProxy() {
    _initSearchWSProxy();
  }
  
  public SearchWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initSearchWSProxy();
  }
  
  private void _initSearchWSProxy() {
    try {
      searchWS = (new cs236369.hw5.SearchWSServiceLocator()).getSearchWS();
      if (searchWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)searchWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)searchWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (searchWS != null)
      ((javax.xml.rpc.Stub)searchWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cs236369.hw5.SearchWS getSearchWS() {
    if (searchWS == null)
      _initSearchWSProxy();
    return searchWS;
  }
  
  public java.lang.String[] searchLocal(double locationX, double locationY, double d) throws java.rmi.RemoteException{
    if (searchWS == null)
      _initSearchWSProxy();
    return searchWS.searchLocal(locationX, locationY, d);
  }
  
  public java.lang.String[] searchKeywords(java.lang.String[] keywords) throws java.rmi.RemoteException{
    if (searchWS == null)
      _initSearchWSProxy();
    return searchWS.searchKeywords(keywords);
  }
  
  
}