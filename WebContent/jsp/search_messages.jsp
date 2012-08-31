<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.User"%>
<%@ page import="org.nihul5.other.Message"%>
<%@ page import="java.security.Principal"%>
<%@ page import="java.util.List"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="refresh" content="5; URL=/<%=CONST.WEBAPP_NAME%>/" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#msg_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
		$('#event_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
	});

</script>


<title>Search Messages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	
	<div id="container">
	
	</div>
	
	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>