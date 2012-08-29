<%@ page import="org.nihul5.other.CONST"%>

<link rel="stylesheet" type="text/css" media="all" href="jsDatePick_ltr.min.css" />
<script type="text/javascript" src="jquery.1.4.2.js"></script>
<script type="text/javascript" src="jsDatePick.jquery.min.1.3.js"></script>

<script type="text/javascript">
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
	
	function padStr(i) {
	    return (i < 10) ? "0" + i : "" + i;
	}


	function submitMsg() {
		var data = $('#msg_form').serializeArray();
		data.push({ name: '<%=CONST.MSG_CREATION_TIME%>', 
			value: getDateString() });
		$.post('/<%=CONST.WEBAPP_NAME%>/events/create',
				data, function(response) {
			alert(response.<%=CONST.MSG_RESULT%>);
		});
	}
</script>

<div id="create_msg_form">
	<form id="msg_form" name="msg_creation_form" action="javascript:submitMsg()">
		<table align="center">
			<tr>
				<td colspan=2 align="center">New Message</td>
			</tr>
			<tr>
				<td colspan=2 align="center" height="10px"></td>
			</tr>
			<tr>
				<td>Title:</td>
				<td><input id="msg_title" type="text"
					name="<%=CONST.MSG_TITLE%>" value=""></td>
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
			<tr>
				<td>Event Day:</td>
				<td><textarea name="<%=CONST.MSG_CONTENT%>" rows="10" cols="10"></textarea></td>
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


