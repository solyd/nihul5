<%@ page language="java" contentType="text/html; charset=windows-1255" pageEncoding="windows-1255"%>
    
<%@ page import="org.nihul5.other.CONST"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">

<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<link rel="stylesheet" type="text/css" media="all" href="/<%=CONST.WEBAPP_NAME%>/styles/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jsDatePick.jquery.min.1.3.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.timePicker.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/create_message.js"></script>
<script>
	var messageResult = <%=CONST.MSG_RESULT%>;
	
	function getDateString() {
	    var temp = new Date();
	    var dateStr = padStr(temp.getFullYear()) +
	                  padStr(1 + temp.getMonth()) +
	                  padStr(temp.getDate()) +
	                  padStr(temp.getHours()) +
	                  padStr(temp.getMinutes()) +
	                  padStr(temp.getSeconds());
	    return dateStr;
	}
	
	function submitMsg() {
		var data = $('#msg_form').serializeArray();
		data.push({ name: '<%=CONST.MSG_CREATION_TIME%>', 
			value: getDateString() });
		data.push({ name: '<%=CONST.EVENT_DATE%>', 
			value: takeEventDate() });
		data.push({ name: '<%=CONST.MSG_TYPE%>', 
			value: messageType });
		$.post('/<%=CONST.WEBAPP_NAME%>/messages/create',
				data, function(response) {
			alert(response.<%=CONST.MSG_RESULT%>);
		});
	}
	
</script>

<title>Create Message</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right">
			<form id="msg_form" name="msg_creation_form" action="javascript:submitMsg()">
				<table id="mainTable" align="center">
					<tr>
						<td id="title" colspan=2 align="center">New Message</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td><button id="messageType" type="button">Message</button> </td>
						<td><button id="eventType" type="button">Event</button> </td>
					</tr>
					<tr>
						<td>Title:</td>
						<td><input id="msg_title" type="text" name="<%=CONST.MSG_TITLE%>" value=""></td>
					</tr>
					<tr>
						<td>Latitude:</td>
						<td><input id="lat" type="text" name="<%=CONST.MSG_LATITUDE%>" value=""></td>
					</tr>
					<tr>
						<td>Longitude:</td>
						<td><input id="lng" type="text" name="<%=CONST.MSG_LONGITUDE%>" value=""></td>
					</tr>
					<tr>
						<td>Content:</td>
						<td><textarea name="<%=CONST.MSG_CONTENT%>" rows="10" cols="10"></textarea></td>
					</tr>
					<tr id="date_title" style="display: none">
						<td>Date:</td>
					</tr>
					<tr id="date_data" style="display: none">
						<td><input id="eventDay" type="text" size="10" /></td>
						<td><input id="eventTime" type="text" size="10" /></td>
					</tr>
					<tr id="capacity" style="display: none">
						<td>Capacity:</td>
						<td><input id="capacity" type="text" size="10" value="0" /></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
		
					<tr>
						<td></td>
						<td><input type="submit" name="Submit" value="Create" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>