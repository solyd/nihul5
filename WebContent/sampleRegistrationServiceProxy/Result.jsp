<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleRegistrationServiceProxyid" scope="session" class="cs236369.hw5.RegistrationServiceProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleRegistrationServiceProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleRegistrationServiceProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleRegistrationServiceProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        cs236369.hw5.RegistrationService getRegistrationService10mtemp = sampleRegistrationServiceProxyid.getRegistrationService();
if(getRegistrationService10mtemp == null){
%>
<%=getRegistrationService10mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
</TABLE>
<%
}
break;
case 15:
        gotMethod = true;
        java.lang.String[] getRegisteredEndpoints15mtemp = sampleRegistrationServiceProxyid.getRegisteredEndpoints();
if(getRegisteredEndpoints15mtemp == null){
%>
<%=getRegisteredEndpoints15mtemp %>
<%
}else{
        String tempreturnp16 = null;
        if(getRegisteredEndpoints15mtemp != null){
        java.util.List listreturnp16= java.util.Arrays.asList(getRegisteredEndpoints15mtemp);
        tempreturnp16 = listreturnp16.toString();
        }
        %>
        <%=tempreturnp16%>
        <%
}
break;
case 18:
        gotMethod = true;
        String url_1id=  request.getParameter("url21");
            java.lang.String url_1idTemp = null;
        if(!url_1id.equals("")){
         url_1idTemp  = url_1id;
        }
        java.lang.String addEndpoint18mtemp = sampleRegistrationServiceProxyid.addEndpoint(url_1idTemp);
if(addEndpoint18mtemp == null){
%>
<%=addEndpoint18mtemp %>
<%
}else{
        String tempResultreturnp19 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(addEndpoint18mtemp));
        %>
        <%= tempResultreturnp19 %>
        <%
}
break;
case 23:
        gotMethod = true;
        String url_2id=  request.getParameter("url26");
            java.lang.String url_2idTemp = null;
        if(!url_2id.equals("")){
         url_2idTemp  = url_2id;
        }
        java.lang.String deleteEndpoint23mtemp = sampleRegistrationServiceProxyid.deleteEndpoint(url_2idTemp);
if(deleteEndpoint23mtemp == null){
%>
<%=deleteEndpoint23mtemp %>
<%
}else{
        String tempResultreturnp24 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(deleteEndpoint23mtemp));
        %>
        <%= tempResultreturnp24 %>
        <%
}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>