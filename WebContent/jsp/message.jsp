<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map_loader.js"></script>
<script>
	window.onload = addMessage; 
</script>
<title>Add Message</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right">
			<table align="center">
				<tr>
					<td colspan=2 align="center">New Message</td>
				</tr>
				<tr>
					<td colspan=2 align="center" height="10px"></td>
				</tr>
				<tr>
					<td>Latitude</td>
					<td><input id='lat' type='text' size='20' name='lat' value='' /></td>
				</tr>
				<tr>
					<td>Longitude</td>
					<td><input id='lng' type='text' size='20' name='lng' value='' /></td>
				</tr>
				<tr>
					<td>Name</td>
					<td><input id='name' type='text' size='20' name='name' value='' /> </td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan=2 align="center" height="10px"></td>
				</tr>
			</table>
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>