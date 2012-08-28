<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="java.security.Principal"%>

<%
	User requestedUser = (User) request.getAttribute(CONST.USER);
	Principal princ = request.getUserPrincipal();
%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript">
	// Wer'e resusing the info box, but don't want the floating right thing
	$(document).ready(function() {
		$('div#user_info').removeAttr('class');
	});
	
	function deleteProfile(username) {
		<% if (requestedUser != null) { %>
		
		$.post('/<%=CONST.WEBAPP_NAME%>/users/delete', { username : '<%=requestedUser.username%>'},
				function(response) {
			
		});
		
		<%}%>
	}
	
</script>

<title>User Profile</title>
</head>

<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<%@ include file="/jsp/users/user_info.jsp"%>

		<%
			if (requestedUser != null && princ != null
				&& requestedUser.username.equals(princ.getName())) {
		%>
		<button type="button" onClick="deleteProfile('<%=user.username%>')">Delete
			Profile</button>
		<%
			}
		%>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
