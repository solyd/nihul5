<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@page import="org.nihul5.other.Message.MessageType"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.Consensus"%>
<%@ page import="org.nihul5.other.Storage"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>

<%
	Message minfo_message = (Message) request.getAttribute(CONST.MSG);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.lightbox_me.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/message.js"></script>

<script type="text/javascript">
	var latitude = <%=minfo_message.lat%>;
	var longtitude = <%=minfo_message.lng%>;
	
	$(document).ready(function() {
		map.setZoom(11);
		deployPosition(latitude, longtitude);
	});
</script>

<title>Message Info</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>

		<%@ include file="/jsp/messages/info.jsp"%>
				
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
