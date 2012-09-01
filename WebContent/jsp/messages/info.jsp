<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.Consensus"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>

<%
	Message message = (Message) request.getAttribute(CONST.MSG);
	Principal princ = request.getUserPrincipal();
	boolean isPost = message.type.toString().equals("POST");
%>

<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/message.js"></script>

<script>
	$(document).ready(function() {
		var latitude = <%=message.lat%>;
		var longtitude = <%=message.lng%>;
		var messageCreateDate = <%=message.creationTime%>;
		var eventDate = <%=message.eventTime%>;
		var eventCapacity = <%=message.capacity%>;
		var eventRegistered = <%=message.nSubs%>;
		//var isCapacityFull = isFull;
		
		showDateInFormat(messageCreateDate, 'craeted_date');
		showDateInFormat(eventDate, 'event_date');
		$('#capacity').text(eventRegistered + "/" + eventCapacity);
		
 		var msgId = <%=message.id%>;
 		<%if (princ != null){%>
			var userName = "<%=princ.getName()%>";
			$.get('/<%=CONST.WEBAPP_NAME%>/IsUserRegisteredToEvent',
					{<%=CONST.USERNAME%>: userName , <%=CONST.MSG_ID%>: msgId}, function(response) {
						if (response.result == 'success'){
							$('#unregister').removeAttr("disabled");
						}
						else{
							if (eventCapacity > eventRegistered){
								$('#register').removeAttr("disabled");
							}
						}
			});
		<%}%>
		
		$('#register').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/RegisterToEvent',
					{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
						if (response.result == 'success'){
							$('#register').attr('disabled', 'disabled');
							$('#unregister').removeAttr("disabled");
							eventRegistered++;
							$('#capacity').text(eventRegistered + "/" + eventCapacity);
						}
						else{
							alert(response.reason);
						}
			});
		});
		$('#unregister').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/UnregisterFromEvent',
					{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
						if (response.result == 'success'){
							$('#unregister').attr('disabled', 'disabled');
							$('#register').removeAttr("disabled");
							eventRegistered--;
							$('#capacity').text(eventRegistered + "/" + eventCapacity);
						}
						else{
							alert(response.reason);
						}
			});
		});
		
<%-- 		$('#vote').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/VoteOnConsensusReq',
					{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
						if (response.result == 'success'){
							$('#unregister').attr('disabled', 'disabled');
							$('#register').removeAttr("disabled");
							eventRegistered--;
							$('#capacity').text(eventRegistered + "/" + eventCapacity);
						}
						else{
							alert(response.reason);
						}
			});
		});
		
		$('#delete_vote').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/DeleteVoteOnConsensusReq',
					{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
						if (response.result == 'success'){
							$('#unregister').attr('disabled', 'disabled');
							$('#register').removeAttr("disabled");
							eventRegistered--;
							$('#capacity').text(eventRegistered + "/" + eventCapacity);
						}
						else{
							alert(response.reason);
						}
			});
		}); --%>
		
		$('#delete_message').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/DeleteMessage',
					{ <%=CONST.MSG_ID%>: msgId }, function(response) {
						if (response.result == 'success'){
							$('#content').hide('slow', function() {
								$('#content').replaceWith('<h2 align="center">Message was successfuly deleted</h2>');
								//$('#content').show('slow', function() {;});
							});
						};
			});
		});
	});
</script>

<div id="content" class="right">
	<table id="mainTable" align="center">
			<tr>
				<td id="title" colspan=2 align="center">
				<% if (isPost) { %>
					<h2 id="create_head">Post</h2> 
				<%} else { %>
					<h2 id="create_head">Event</h2>
				<% } %>
				</td>
			</tr>
			<tr>
				<td colspan=2 align="center" height="10px"></td>
			</tr>
			<tr>
				<td>Posted By:</td>
				<td><%=message.username%></td>
			</tr>
			<tr>
				<td>Title:</td>
				<td><%=message.title%></td>
			</tr>
			<tr>
				<td>Latitude:</td>
				<td><%=message.lat%></td>
			</tr>
			<tr>
				<td>Longitude:</td>
				<td><%=message.lng%></td>
			</tr>
			<tr>
				<td>Content:</td>
				<td><%=message.content%></td>
			</tr>
			<tr>
				<td>Created On:</td>
				<td id="craeted_date" ></td>
			</tr>
			
			<%if (!isPost) {%>
			<tr>
				<td>When?</td>
				<td id="event_date" ></td>
			</tr>
			<tr>
				<td>Capacity:</td>
				<td id="capacity"></td>
			</tr>
			<tr>
				<td colspan=2>
					<input id="register" type="button" value="Register" disabled="disabled" />
					<input id="unregister" type="button" value="Unregister" disabled="disabled" />
				</td>
			</tr>
			<tr><td>Consensuses:</td></tr>
			<tr>
			<td colspan=2>
				<ul>
					<%List<Consensus> consensusesList = (List<Consensus>) message.consReqList;
					int i = 1;
					for (Consensus consensus : consensusesList) { %>
						<li><%=consensus.desc %> <br />
						
						<%if(consensus.status.toString().equals("NOT_ACCEPTED")){%>
							<span style='text-decoration:underline'>Status:</span> Not Accepted <span id="not_accepted">(<%=consensus.nvotesForChange %>/<%=message.capacity %>)</span>
						<%}else{ %>
							<span style='text-decoration:underline'>Status:</span> Accepted <span id="accepted">(<%=consensus.nvotesForChange %>/<%=message.capacity %>)</span>
							<%} %> <br/>
						<input id="vote" type="button" value="Vote" disabled="disabled"/>
						</li>
					<% } %> 
				</ul>					
			<td>
			</tr>
			<% } %>

			<%if (princ != null){
				if (princ.getName().equals(message.username)){%>
				<tr>
					<td colspan=2 align="center"><input id="delete_message" type="button" value="Delete Message"/></td>
				</tr>
				<%}
			}%>

			<tr>
				<td colspan=2 align="center" height="10px"></td>
			</tr>
		</table>
</div>
