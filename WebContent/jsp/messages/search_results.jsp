<%@ page import="java.util.List"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="org.nihul5.other.CONST"%>


<%
	List<Message> msgs = (List<Message>) request.getAttribute(CONST.MESSAGES);
%>


<script type="text/javascript">
	$(document).ready(function(){
		$('#msg_container').pajinate({
					items_per_page : 10,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
	});

</script>

<%if (msgs == null)  {%>

<div id="center_box">Invalid search</div>

<%} else if (msgs.size() == 0){ %>
<div id="center_box">No Results</div>
<%} else { %>
<div id="msg_container">
	<div class="page_navigation"></div>
	<div class="iphone_list">
		<ul class="list_content">
			<% for (Message msg : msgs) { %>
			<li><a href="/<%=CONST.WEBAPP_NAME%>/message/info/<%=msg.id%>"><%=msg.title %>(<%=msg.type.toString() %>)</a></li>
			<% } %>
		</ul>
	</div>
</div>
<%}%>