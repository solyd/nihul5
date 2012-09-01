<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ page import="org.nihul5.other.CONST"%>

<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>

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
	String[] regEndpoints = (String[]) request.getAttribute(CONST.YELLOW_PAGES_ENDPOINTS);
%>


<title>Yellow Pages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container">
		<div id="center_box">
			
			<% if (regEndpoints != null) { %>
				
				<%if (regEndpoints.length > 0) { %>
				
				<div id="page_container">
					<div class="page_navigation"></div>
					<div class="navlist">
					<ul class="list_content">
						<%
							for (String endpoint : regEndpoints) {
						%>
						<li><a href="#"><%=endpoint%></a></li>
						<%
						}
						%>
					</ul>
					</div>
					
				<%} else { %>
				There are currently no available web services registered.				
				<%} %>
				</div>
			<% } else { %>
			There is a problem with the yellow pages service, please try again later.
			<%} %>

		</div>
	</div>

	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>
