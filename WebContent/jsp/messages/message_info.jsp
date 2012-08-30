<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>

<%
	User user = (User) request.getAttribute(CONST.USER);
	Principal princ = request.getUserPrincipal();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>

<script>
function submitMsg() {
	var data = $('#msg_form').serializeArray();
	
	data.push({ name: '<%=CONST.MSG_CREATION_TIME%>', 
		value: getDateString() });
	
	data.push({ name: '<%=CONST.MSG_TYPE%>', 
		value: messageType });
	
	data.push({ name: '<%=CONST.EVENT_DATE%>', 
		value: getEventDate() });
	
	data.push({ name: '<%=CONST.EVENT_CAPACITY%>', 
		value: getEventCapacity() });

	data.push({ name: '<%=CONST.EVENT_CONSENSUSES%>', 
		value: getConsensuses() });

	$.post('/<%=CONST.WEBAPP_NAME%>/messages/create',
			data, function(response) {
		alert(response.result + ': ' + response.reason);
	});
}
</script>

<title>Message Info</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right">
			<table id="mainTable" align="center">
					<tr>
						<td id="title" colspan=2 align="center">
							<h2 id="create_head">New Post</h2>
						</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Title:</td>
						<td><%=CONST.MSG_TITLE%></td>
					</tr>
					<tr>
						<td>Latitude:</td>
						<td><%=CONST.MSG_LATITUDE%></td>
					</tr>
					<tr>
						<td>Longitude:</td>
						<td><%=CONST.MSG_LONGITUDE%></td>
					</tr>
					<tr>
						<td>Content:</td>
						<td><%=CONST.MSG_CONTENT%></td>
					</tr>
					<tr id="date_title" style="display: none">
						<td>Date:</td>
					</tr>
					<tr id="date_data" style="display: none">
						<td><input id="eventDay" type="text" size="10" /></td>
						<td><input id="eventTime" type="text" size="10" /></td>
					</tr>
					<tr id="event_capacity" style="display: none">
						<td>Capacity:</td>
						<td><input id="capacity" type="text" size="10" value="0" /></td>
					</tr>
					<tr id="consensus1" style="display: none">
						<td>Consensus:</td>
						<td><textarea id="consensus_text" rows="10" cols="10"></textarea></td>
					</tr>
					<tr id="consensus2" style="display: none">
						<td><input id="add_row" type="button" value="Add Consensus" /></td>
    					<td></td>
					</tr>
					<tr id="consensus3" style="display: none">
						<td id="myDiv" colspan="2" width="70%"></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
				</table>
		</div>
	</div>


	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>