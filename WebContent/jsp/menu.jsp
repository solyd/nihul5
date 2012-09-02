<%@ page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>


<div id="menu" class="left">
	<ul>
		<%
			Principal menu_princ = request.getUserPrincipal(); 
			String menu_username = null;
			if (menu_princ != null)
				menu_username = menu_princ.getName();
			
			if (menu_username != null) {
		%>
		
		
		<li><a href="/<%=CONST.WEBAPP_NAME %>/users/profile/<%=menu_username%>">My Profile</a></li>
		
		<li><a href="/<%=CONST.WEBAPP_NAME %>/messages/create">Create Message</a></li>
		
		
		<%} %>
		
		<li><a href="/<%=CONST.WEBAPP_NAME %>/">Browse Messages</a></li>
		
		<!-- TODO -->

		<li><a href="/<%=CONST.WEBAPP_NAME %>/message/search">Search Messages</a></li>

		
		

		<li><a href="/<%=CONST.WEBAPP_NAME %>/users">Users</a></li>
		<li><a href="/<%=CONST.WEBAPP_NAME %>/yellow_pages">Yellow Pages</a></li>
		

	</ul>
</div>
