<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@page import="org.nihul5.other.Message.MessageType"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>
<%@ page import="org.nihul5.other.Consensus"%>
<%@ page import="org.nihul5.other.Storage"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>

<%
	Message message = (Message) request.getAttribute(CONST.MSG);
	Principal princ = request.getUserPrincipal();
	String username = "";
	String voteForChange = "Vote for status change";
	String voteCancel = "Cancel vote for status change";
	if (princ != null)
		username = princ.getName();
	boolean isPost = message.type == MessageType.POST;
%>

<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.lightbox_me.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/message.js"></script>

<script type="text/javascript">
	var latitude = <%=message.lat%>;
	var longtitude = <%=message.lng%>;
	var messageCreateDate = <%=message.creationTime%>;
	var eventDate = <%=message.eventTime%>;
	var currDate = new Date().getTime();
	var eventCapacity = <%=message.capacity%>;
	var eventRegistered = <%=message.nSubs%>;
	
	var eventFull = '<%=CONST.EVENT_FULL%>';
	
	var voteForChange = '<%=voteForChange%>';
	var voteCancel = '<%=voteCancel%>';
	
	function updateCapacity() {
		$('#capacity').text(eventRegistered + "/" + eventCapacity);
		
		$('.subs').each(function() {
			$(this).html(eventRegistered);
		});
	}
	
	$(document).ready(function() {
		$('#cons_container').pajinate({
					items_per_page : 1,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
		$('#cons_container').show();
		
		//map.setZoom(11);
		//deployPosition(latitude, longtitude);
		
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
						}
						else{
							$('#reg_button').attr('value', 'Register');
							if (eventCapacity <= eventRegistered){
								$('#reg_button').attr('disabled', 'disabled');
							}
							$('#vote').hide();
						}
						$('#reg_button').show();
			});
			
			
		<%} else { %>
			$('#reg_button').attr('disabled', 'disabled');
			$('#reg_button').show();
		<%}%>

		
		$('#reg_button').click(function() {
			$('#reg_button').attr('disabled', 'disabled');
			
			if ($('#reg_button').val() == 'Register') {
				$.post('/<%=CONST.WEBAPP_NAME%>/RegisterToEvent',
						{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
							if (response.result == 'success') {
								$('#reg_fail').hide();
								$('#reg_button').attr('value', 'Unregister');
								eventRegistered++;
								$('#vote').show();
								updateCapacity();

							}
							else {
								$('#reg_fail').html(response.reason);
								$('#reg_fail').show();
								
								if (response.reason == eventFull) {
									eventRegistered = eventCapacity;
									updateCapacity();
								}
							}
							$('#reg_button').removeAttr("disabled");
				});
			}
			else {
				$.post('/<%=CONST.WEBAPP_NAME%>/UnregisterFromEvent',
						{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
							if (response.result == 'success') {
								$('#reg_fail').hide();
								$('#reg_button').attr('value', 'Register');
								eventRegistered--;
								$('#vote').hide();
								updateCapacity();
								if (eventCapacity <= eventRegistered){
									$('#reg_button').attr('disabled', 'disabled');
								}
							}
							else{
								$('#reg_fail').html(response.reason);
								$('#reg_fail').show();
								
							}
							$('#reg_button').removeAttr('disabled');
				});			
			}
		});
		
		if (eventDate < currDate) {
			$('#reg_button').replaceWith('<i>The event has occured</i>');
			$('#vote').hide();
		}
		
       $('#show_reg_button').click(function(e) { //A button on clicking shows the popup
           	$.get('/<%=CONST.WEBAPP_NAME%>/EventRegisteredUsers',
   			{ <%=CONST.EVENT_ID%>: msgId }, function(response) {
   				$('#reg_users').html(response);
   				   	$('#reg_users').lightbox_me({
   					centered: true
   				});
	 		});
           	
			e.preventDefault();
		});
		
 		$('.vote_cons').click(function() {
 			var consid = $(this).attr('name');
 			var vote = "cancel";
 			var isVoteForChange = $(this).val() == voteForChange;
 			var votefield = $('#cons_votes').find('.nvotes');
 			var nvotes = parseInt(votefield.html());
 			console.log(nvotes);
 			if (isVoteForChange) {
 				vote = "accept";	
 			}
 			
			$.post('/<%=CONST.WEBAPP_NAME%>/VoteOnConsensusReq',
					{ <%=CONST.EVENT_ID%>: msgId, <%=CONST.USERNAME%>: userName, 
						<%=CONST.CONSENSUS_ID%>: consid, <%=CONST.VOTE%>: vote }, function(response) {
						if (response.result == 'success') {
							$.get('/<%=CONST.WEBAPP_NAME%>/GetMessage',
								{<%=CONST.MSG_ID%>: msgId}, function(response) {
									$('#content').replaceWith(response);
								});
						}
						else{
							$('#vote_fail').html(response.reason);
						}
			});
		});
		
		$('#delete_message').click(function(){
			$.post('/<%=CONST.WEBAPP_NAME%>/DeleteMessage',
					{ <%=CONST.MSG_ID%>: msgId }, function(response) {
						if (response.result == 'success') {
							$('#content').hide('slow', function() {
								$('#content').replaceWith('<div id="content" class="right" align="center"><h2>Message was successfuly deleted</h2><div>');
								$('#content').show('slow', function() {});
							});
						}
						else {
							$('#delete_message').hide('slow', function() {
								$(this).replaceWith('This event deadline has passed - it cannot be deleted.');
								$(this).show('slow');
							});
						}
			});
		});
	});

</script>

<div id="content" class="right">

	<br/>
	<%
		if (princ != null && princ.getName().equals(message.username)) {
	%>
		<input id="delete_message" type="button" value="Delete Message"/>
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
					<input id="reg_button" type="button" value="Register" style="display: none;"/>
					<input id="show_reg_button" type="button" value="Who's registered >>" />
					<div id="reg_fail" style="display: none;"></div>
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
							Storage storage = (Storage) getServletContext().getAttribute(CONST.STORAGE);
					%>

					<div id="cons_container" style="display: none;">
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
								<b>[Status]</b> <span id="accept_state">Not Accepted</span> <span id="cons_votes">(<span class="nvotes"><%=consensus.nvotesForChange%></span>/<span class="subs"><%=message.nSubs%></span>)</span>
							<%
								} else {
							%>
								<b>[Status]</b> <span id="accept_state">Accepted</span> <span id="cons_votes">(<span class="nvotes"><%=consensus.nvotesForChange%></span>/<span class="subs"><%=message.nSubs%></span>)</span>
							<%
								}
							%> <br/>
							
							<%
							
							if (username != null && username.length() > 0) { %>
								<% if (storage.didUserVote(username, consensus.id))  {%>
								<input class="vote_cons" id="vote" name="<%=consensus.id %>" type="button" value="Cancel vote for status change" />
								<%} else { %>
								<input class="vote_cons" id="vote" name="<%=consensus.id %>" type="button" value="Vote for status change" />
								<%} %>
								<div id="vote_fail"></div>
								</li>
							<%} %>
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

