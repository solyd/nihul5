<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@page import="org.nihul5.other.Message.MessageType"%>
<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<%@ page import="org.nihul5.other.CONST"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>

<script type="text/javascript">
	function onLocationSearch() {
		$.post('/<%=CONST.WEBAPP_NAME%>/message/search',
				$('#location_form').serialize(), function(response) {
			$('#search_results').html(response);
		});
	}
	
</script>

<title>Search Messages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container" class="right">
		<div id="search_form" class="left">
			<%@ include file="/jsp/messages/search_form.jsp"%>
		</div>
		
		<div id="search_results" class="right">
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
