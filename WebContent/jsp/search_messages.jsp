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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false&libraries=geometry"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/markerclusterer.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#lat').blur(function() {
			var lat = $(this).val();
			var lng = $('#lng').val();
			deployPosition(lat, lng);
		});
		$('#lng').blur(function() {
			var lng = $(this).val();
			var lat = $('#lat').val();
			deployPosition(lat, lng);
		});
		
		google.maps.event.addListener(map, 'click', function(event) {
			var marker = placeMarker(event.latLng);
			marker.setDraggable(true);
			changeLatLngValues(event);
			google.maps.event.addListener(marker, "drag", function(event) {
				changeLatLngValues(event);
			});
			
			var circle = new google.maps.Circle({
				  map: map,
				  radius: 12000,    // 10 miles in metres
				  fillColor: '#AA0000',
				  editable: true
			});
			circle.bindTo('center', marker, 'position');
		});
		$('#radius').blur(function() {

		});
/* 		$('#msg_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		});
		$('#event_container').pajinate({
					items_per_page : 5,
					item_container_id : '.list_content',
					nav_panel_id : '.page_navigation'
		}); */
	});

</script>


<title>Search Messages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	
	<div id="container">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right" align="center">
			<form id="search_form" name="msg_creation_form" action="javascript:submitMsg()">
				<table id="mainTable" align="center">
					<tr>
						<td id="title" colspan=2 align="center"><h2>Search</h2></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Latitude:</td>
						<td><input id="lat" type="text" name="123" value=""/></td>
					</tr>
					<tr>
						<td>Longitude:</td>
						<td><input id="lng" type="text" name="123" value=""/></td>
					</tr>
					<tr>
						<td>Radius:</td>
						<td><input id="radius" type="text" name="123" value=""/></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input id="search_button" type="submit" name="Search" value="Search" />
							<div id="try_again"></div>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>