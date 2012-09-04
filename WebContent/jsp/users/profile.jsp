<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>
<%@ page import="java.util.List"%>

<%
	User user = (User) request.getAttribute(CONST.USER);
	Principal princ = request.getUserPrincipal();
%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#msg_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
		$('#event_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
	});
	
	function deleteProfile(username) {
		<%if (user != null) {%>
			$.post('/<%=CONST.WEBAPP_NAME%>/DeleteUser', { username: "<%=user.username%>"} , function(data) {
				if (data.result == 'success') {
					$('#delete_button').hide('slow', function() {
						$(this).replaceWith('<p>Profile was successfuly deleted, redirecting in 2 seconds</p>');
						$(this).show('slow', function() {
							setTimeout(function() {
								window.location.href = "/<%=CONST.WEBAPP_NAME%>/";
							}, 2000);		
						});
					});
				}
			}, "json");
		<%}%>
	}
</script>

<title>User Profile</title>
</head>

<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<%
			if (user != null) {
		%>
		<div id="user_info" class="left">
			
			<h1><%=user.username%>'s Profile</h1>
			
			<div class="center_box_colorless">
				<table align="center">
					<tr>
						<td colspan=2 align="center"><h3>Details</h3></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Username:</td>
						<td><%=user.username%></td>
					</tr>
					<tr>
						<td>First Name:</td>
						<td><%=user.firstName%></td>
					</tr>
					<tr>
						<td>Last Name:</td>
						<td><%=user.lastName%></td>
					</tr>
					<tr>
						<td>Email:</td>
						<td><%=user.email%></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
				</table>

				<%
					if (user != null && princ != null
								&& user.username.equals(princ.getName())) {
				%>
				<button id="delete_button" type="button" class="cool_button2" onClick="deleteProfile('<%=user.username%>')">Delete Profile</button>
				<%
					}
				%>

			</div>
		</div>
		
		<div id="content" class="right">
			<h3>Created Messages</h3>
			<%
				List<Message> profile_usermsgs = (List<Message>) request.getAttribute(CONST.USER_CREATED_MSGS);
				if (profile_usermsgs != null && profile_usermsgs.size() > 0) {
			%>

			<div id="msg_container">
				<div class="page_navigation"></div>
				<div class="iphone_list">
				<ul class="list_content">
				<% for (Message msg : profile_usermsgs) { %>
					<li><a href="/<%=CONST.WEBAPP_NAME%>/message/info/<%=msg.id%>"><%=msg.title %>   (<%=msg.type.toString() %>)</a></li>
				<% } %> 
				</ul>
				</div>
			</div>

			
			<%} else { %>
			<div id="center_box">
				No created messages for this user
			</div>
			<%} %>
			
			<h3>Registered Events</h3>
			
			<%
				List<Message> profile_usrevents = (List<Message>) request.getAttribute(CONST.USER_REG_EVENTS);
				if (profile_usrevents != null && profile_usrevents.size() > 0) {
			%>
			
			<div id="event_container">
				<div class="page_navigation"></div>
				<div class="iphone_list">
				<ul class="list_content">
				<% for (Message msg : profile_usrevents) { %>
					<li><a href="/<%=CONST.WEBAPP_NAME%>/message/info/<%=msg.id%>"><%=msg.title %></a></li>
				<% } %> 
				</ul>
				</div>
			</div>
			
			
			<%} else { %>
			<div id="center_box">
				This user hasn't registered to any events yet.
			</div>
			<%} %>			
			
		</div>

		<%
			} else {
		%>
		<div id="center_box">User does not exist</div>
		<%
			}
		%>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
