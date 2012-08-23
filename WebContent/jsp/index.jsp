<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.nihul5.other.CONST"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map_loader.js"></script>

<script type="text/javascript">

	function onLoadButtonClick() {
		$("#content").load("jsp/register.jsp #center_box");
	}

	function onUsersOnlyClick() {

	}
</script>


<title>Welcome</title>
</head>

<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<div id="content" class="left">
			<p>some content blabla</p>
			<button onclick="onLoadButtonClick()">Load other content</button>
			<button onclick="onUsersOnlyClick()">Users only</button>
		</div>

		<div id="map_canvas" class="right"></div>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>