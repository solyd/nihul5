<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="java.util.List"%>

<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.lightbox_me.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $('#page_container').pajinate({
        items_per_page : 10,
        item_container_id : '.list_content',
        nav_panel_id : '.page_navigation'
    });
});

</script>


<%
	response.setContentType("text/html");
	List<String> userList = (List<String>) request.getAttribute(CONST.USERS_LIST);
%>

<div id="center_box">
	<%
		if (userList == null || userList.size() == 0) {
	%>
	There are no users registered yet
	<%
		} else {
	%>

	<div id="page_container" class="container">
	<div class="page_navigation"></div>
	<div class="iphone_list">
	<ul class="list_content">
		<%
			for (String username : userList) {
		%>
		<li><a href="/<%=CONST.WEBAPP_NAME%>/users/profile/<%=username%>"><%=username%></a></li>
		<%
			}
		%>
		
	</ul>
	</div>
	</div>

	<%
		}
	%>
</div>
