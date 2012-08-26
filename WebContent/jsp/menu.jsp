<%@ page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.CONST"%>

<%
	Principal princ = request.getUserPrincipal();
	String myProfileLink = CONST.WEBAPP_NAME + "/login.jsp";
	if (princ != null) {
		myProfileLink = "/" + CONST.WEBAPP_NAME + "/users/profile/" + princ.getName();
	}
%>

<div id="menu" class="left">
	<ul>
		<li><a href="/<%=CONST.WEBAPP_NAME %>/">Home</a></li>
		<li><a href="/<%=myProfileLink%>">My Profile</a></li>
		
		<!-- TODO -->
		<li><a href="/<%=CONST.WEBAPP_NAME %>/">Search Events</a></li>
		<li><a href="/<%=CONST.WEBAPP_NAME %>/">My Events</a></li>
		
		<li><a href="/<%=CONST.WEBAPP_NAME %>/events/create">Create Event</a></li>
		
		<li><a href="/<%=CONST.WEBAPP_NAME %>/users">Users</a></li>

	</ul>
</div>
