<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false&libraries=geometry"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#xml_button').click(function() {
		    window.location.href = '/<%=CONST.WEBAPP_NAME %>/XMLExport';
		});
		
		$('#post_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_POST%>';
			
<%-- 			$.get('/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_POST%>', {}, --%>
// 			function(response) {
// 				var kmlLayer = new google.maps.KmlLayer(response);
// 				kmlLayer.setMap(map);
// 			});
		});
		
		$('#event_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_EVENT%>';
		});
		
		$('#path_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_PATH%>';
		});
		
	 	$('#display_kml').click(function(){
			$.get('/<%=CONST.WEBAPP_NAME%>/CreateKml', {},
			function(response) {
				var kmlLayer = new google.maps.KmlLayer(response);
				kmlLayer.setMap(map);
			});
		});
	 	
	});
</script>


<title>Export Messages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right" align="center">
			<div class="center_box_colorless">
				<button class="cool_button" id="xml_button" type="button">Export all messages to XML</button>
				<button class="cool_button" id="post_button" type="button">Export Posts to KML (and show on map)</button>
				<button class="cool_button" id="event_button" type="button">Export Events to KML (and show on map)</button>
				<button class="cool_button" id="path_button" type="button">Export Path to KML (and show on map)</button>
			</div>
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>
