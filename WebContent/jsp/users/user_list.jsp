<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="java.util.List"%>

<%
	response.setContentType("text/html");
	List<String> userList = (List<String>) request.getAttribute(CONST.USERS_LIST);
%>

<div id="user_list" class="left">
	<%
		if (userList == null || userList.size() == 0) {
	%>
	<div id="center_box">There are no users registered yet</div>
	<%
		} else {
	%>

	<ul>
		<%
			for (String username : userList) {
		%>
		<li><%=username%></li>
		<%
			}
		%>
	</ul>

	<%
		}
	%>
</div>
