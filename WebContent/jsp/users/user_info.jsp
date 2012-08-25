<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>

<%
	response.setContentType("text/html");
	User user = (User) request.getAttribute(CONST.USER);
	String msg = (String) request.getAttribute(CONST.MSGBOX_USER_INFO_TXT);
%>

<div id="user_info" class="right">
	<%
		if (msg != null) {
	%>
	<div id="center_box">
		<%=msg%>
	</div>
	<%
		} else if (user != null) {
	%>
	<div id="center_box">
		<table align="center">
			<tr>
				<td colspan=2 align="center">Profile</td>
			</tr>
			<tr>
				<td colspan=2 align="center" height="10px"></td>
			</tr>
			<tr>
				<td>Username: </td>
				<td><%=user.username%></td>
			</tr>
			<tr>
				<td>First Name: </td>
				<td><%=user.firstName%></td>
			</tr>
			<tr>
				<td>Last Name: </td>
				<td><%=user.lastName%></td>
			</tr>
			<tr>
				<td>Email: </td>
				<td><%=user.email%></td>
			</tr>
			<tr>
				<td colspan=2 align="center" height="10px"></td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>
