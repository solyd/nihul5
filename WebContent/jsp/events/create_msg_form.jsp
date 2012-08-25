<%@ page import="org.nihul5.other.CONST"%>

<div id="create_msg_form">
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
			<td><input id="latitude" type="text"
				name="<%=CONST.MSG_LATITUDE%>" value=""></td>
		</tr>
		<tr>
			<td>Longitude:</td>
			<td><input id="longitude" type="text"
				name="<%=CONST.MSG_LONGITUDE%>" value=""></td>
		</tr>
		<tr>
			<td colspan=2 align="center" height="10px"></td>
		</tr>
	</table>
</div>
