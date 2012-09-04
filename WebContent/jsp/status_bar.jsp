<%@page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.CONST"%>

<div id="status_bar">
	<div style="float: left;">
		Welcome to LOL, The Location Oriented LOL
	</div>

	<div id="login_info" style="float: right;">
		<%
			Principal p = request.getUserPrincipal();
			String name = "";
			if (p != null && request.getAttribute("logout") == null) {
				name = p.getName();
		%>
		Logged in as
		<a href="/<%=CONST.WEBAPP_NAME%>/users/profile/<%=name%>"><%=name%></a>, <a href="/<%=CONST.WEBAPP_NAME%>/logout">logout</a>
		<%
			} else {
		%>
		<a href="/<%=CONST.WEBAPP_NAME%>/login.jsp">login</a> / <a
			href="/<%=CONST.WEBAPP_NAME%>/register">register</a>
		<%
			}
		%>
	</div>
</div>
