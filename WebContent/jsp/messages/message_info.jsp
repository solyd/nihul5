<%@page import="org.nihul5.other.Message.MessageType"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="org.nihul5.other.CONST"%>
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
	boolean isPost = message.type == MessageType.POST;

	//boolean isFull = (message.capacity == message.nSubs);

	//Date date = new Date();
	//date.setTime(message.creationTime);
	/* ;
	 //message.consensusDescList;
	 ;
	 ;
	 ;
	 ;
	 ;
	 ;
	 ; */
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.lightbox_me.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/message.js"></script>

<script type="text/javascript">

	$(document).ready(function() {
		$('#cons_container').pajinate({
					items_per_page : 1,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
		
		var latitude = <%=message.lat%>;
		var longtitude = <%=message.lng%>;
		var messageCreateDate = <%=message.creationTime%>;
		var eventDate = <%=message.eventTime%>;
		var eventCapacity = <%=message.capacity%>;
		var eventRegistered = <%=message.nSubs%>;
		//var isCapacityFull = isFull;
		
		map.setZoom(11);
		deployPosition(latitude, longtitude);
		
		showDateInFormat(messageCreateDate, 'craeted_date');
		showDateInFormat(eventDate, 'event_date');
		$('#capacity').text(eventRegistered + "/" + eventCapacity);
		
 		var msgId = <%=message.id%>;
 		<%if (princ != null) {%>
			var userName = "<%=princ.getName()%>";
			$.get('/<%=CONST.WEBAPP_NAME%>/IsUserRegisteredToEvent',
					{<%=CONST.USERNAME%>: userName , <%=CONST.MSG_ID%>: msgId}, function(response) {
						if (response.result == 'success'){
							$('#reg_button').attr('value', 'Unregister');
							//$('#unregister').removeAttr("disabled");
						}
						else{
							$('#reg_button').attr('value', 'Register');
							if (eventCapacity <= eventRegistered){
								//$('#register').removeAttr("disabled");
								$('#reg_button').attr('disabled', 'disabled');
							}
						}
			});
		<%} else { %>
		$('#reg_button').attr('disabled', 'disabled');
		<%}%>
		
		$('#reg_button').click(function() {
			$('#reg_button').attr('disabled', 'disabled');
			
			if ($('#reg_button').val() == 'Register') {
				$.post('/<%=CONST.WEBAPP_NAME%>/RegisterToEvent',
						{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
							if (response.result == 'success') {
								$('#reg_button').attr('value', 'Unregister');
								eventRegistered++;
								$('#capacity').text(eventRegistered + "/" + eventCapacity);
							}
							else {
								alert(response.reason);
							}
							$('#reg_button').removeAttr("disabled");
				});
			}
			else {
				$.post('/<%=CONST.WEBAPP_NAME%>/UnregisterFromEvent',
						{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
							if (response.result == 'success') {
								$('#reg_button').attr('value', 'Register');
								eventRegistered--;
								$('#capacity').text(eventRegistered + "/" + eventCapacity);
								if (eventCapacity <= eventRegistered){
									$('#reg_button').attr('disabled', 'disabled');
								}	
							}
							else{
								alert(response.reason);
							}
							$('#reg_button').removeAttr('disabled');
				});			
			}
		});
		

		function deselect() {
    		$(".pop").slideFadeToggle(function() {
	        	$("#contact").removeClass("selected");
    		});    
		}
		
		
       $('#show_reg_button').click(function(e){ //A button on clicking shows the popup
           	$.get('/<%=CONST.WEBAPP_NAME%>/EventRegisteredUsers',
   			{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
   				$('#reg_users').html(response);
   				//$('#reg_users').css('display','block');
   				$('#reg_users').lightbox_me({
   					centered: true
   				});
	 		});
           	
			e.preventDefault();
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
						if (response.result == 'success') {
							$('#delete_message').hide('slow', function() {
								$(this).replaceWith('<p>Message was successfuly deleted, redirecting in 2 seconds</p>');
								$(this).show('slow', function() {
									setTimeout(function() {
										window.location.href = "/<%=CONST.WEBAPP_NAME%>/";
									}, 2000);		
								});
							});
						}
			});
		});
	});
</script>



<title>Message Info</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>

		<div id="content" class="right">

			<br/>
			<%
				if (princ != null && princ.getName().equals(message.username)) {
			%>
				<input id="delete_message" type="button" value="Delete Message" class="right" align="middle" />
			<%
				}
			%>
			<table id="mainTable" align="center">
					<tr>
						<td id="title" colspan=2 align="center">
						<%
							if (isPost) {
						%>
							<h2 id="create_head">Post</h2> 
						<%
 							} else {
 						%>
							<h2 id="create_head">Event</h2>
						<%
							}
						%>
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
						<td><textarea readonly name="<%=CONST.MSG_CONTENT%>" rows="10" cols="10"><%=message.content%></textarea></td>
					</tr>
					<tr>
						<td>Created:</td>
						<td id="craeted_date" ></td>
					</tr>
					
					<%
						if (!isPost) {
					%>
					<tr>
						<td>When?</td>
						<td id="event_date" ></td>
					</tr>
					<tr>
						<td>Capacity:</td>
						<td id="capacity"></td>
					</tr>
					<tr>
						<td>Posted By:</td>
						<td><%=message.username%></td>
					</tr>
					<tr>
						<td colspan=2>
							<input id="reg_button" type="button" value="Register" />
							<input id="show_reg_button" type="button" value="Who's registered >>" />

						<div id="reg_users" style="display: none;">
						</div>
						
					</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<%
						}
					%>
				</table>
				<%
					if (!isPost) {
				%>
				<table id="consTable" align="center">
					<tr>
						<td id="title" colspan=2 align="center">
						<b>Consensus Requirements:</b>					
						</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td colspan=2>
							<%
								List<Consensus> consensusesList = message.consReqList;
								if (consensusesList == null || consensusesList.size() == 0) {
							%>
									None.
							<%
								} else {
							%>
	
							<div id="cons_container">
							<div class="page_navigation"></div>
							<div class="navlist">
							<ul class="list_content">
							<%
								for (Consensus consensus : consensusesList) {
							%>
								    <li><textarea readonly name="<%=CONST.MSG_CONTENT%>" rows="7" cols="10"><%=consensus.desc%></textarea>
									<br />
									<%
										if (consensus.status == Consensus.Status.NOT_ACCEPTED) {
									%>
										<u>Status:</u> Not Accepted <span id="not_accepted">(<%=consensus.nvotesForChange%>/<%=message.capacity%>)</span>
									<%
										} else {
									%>
										<u>Status:</u> Accepted <span id="accepted">(<%=consensus.nvotesForChange%>/<%=message.capacity%>)</span>
									<%
										}
									%> <br/>
									<input id="vote" type="button" value="Vote" disabled="disabled"/>
									</li>
								<%
									}
								%>
								</ul>
							</div>
							</div>
								<%
									}
								%> 
						<td>
					</tr>
					</table>
					<%
						}
					%>
				
		</div>
				
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
