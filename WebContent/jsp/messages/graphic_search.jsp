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
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false&libraries=geometry"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/markerclusterer.js"></script>

<script type="text/javascript">
	var mainCircle = null;
	var defaultRadius = 30000;
	
	$(document).ready(function(){
		$('#lat').blur(function() {
			var lat = $(this).val();
			var lng = $('#lng').val();
			var radius = $('#radius').val();
			deployPosition2(lat, lng, radius);
		});
		$('#lng').blur(function() {
			var lat = $('#lat').val();
			var lng = $(this).val();
			var radius = $('#radius').val();
			deployPosition2(lat, lng, radius);
		});
		$('#radius').blur(function() {
			var lng = $('#lng').val();
			var lat = $('#lat').val();
			var radius = $(this).val();
			deployPosition2(lat, lng, radius);
		});
		
		google.maps.event.addListener(map, 'click', function(event) {
			var marker = mainMarker;
			placeMarker(event.latLng);
			changeLatLngValues(event);
			createCircle(defaultRadius);
			if (marker == null){
				mainMarker.setDraggable(true);
				google.maps.event.addListener(mainMarker, "drag", function(event) {
					changeLatLngValues(event);
				});
			}
		});
	});
	
	function deployPosition2(lat, lng, radius){
		if ((lat) && (lng) && (radius)){
			if (inRange(minLat, lat, maxLat) && (inRange(minLng, lng, maxLng))){
				var newPosition = new google.maps.LatLng(lat, lng);
				var marker = mainMarker;
				placeMarker(newPosition);
				createCircle(parseInt(radius)*1000);
				if (marker == null){
					mainMarker.setDraggable(true);
					google.maps.event.addListener(mainMarker, "drag", function(event) {
						changeLatLngValues(event);
					});
				}
			}
		}
	}
	
	function createCircle(radius) {
		if (!mainCircle){
			mainCircle = new google.maps.Circle({
				  map: map,
				  radius: parseInt(radius),
				  fillColor: '#AA0000',
				  editable: true
			});
			mainCircle.bindTo('center', mainMarker, 'position');

	 		google.maps.event.addListener(mainCircle, 'radius_changed', function(event) {
				document.getElementById('radius').value = mainCircle.getRadius() / 1000.0;
			});
		}
		mainCircle.setRadius(radius);
		document.getElementById('radius').value = parseInt(radius) / 1000.0;
	}
	
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
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	
	<div id="container">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right" align="center">
			<form id="location_form" action="javascript:onLocationSearch()">
				<table align="center">
					<tr>
						<td colspan=2 align="center"><h3>Search by location</h3></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Latitude (degrees):</td>
						<td><input id="lat" type="text" name="<%=CONST.MSG_LATITUDE%>" value=""/></td>
					</tr>
					<tr>
						<td>Longitude (degrees):</td>
						<td><input id="lng" type="text" name="<%=CONST.MSG_LONGITUDE%>" value=""/></td>
					</tr>
					<tr>
						<td>Search radius (km):</td>
						<td><input id="radius" type="text" name="<%=CONST.RADIUS%>" value=""/></td>
					</tr>

					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>

					<tr>
						<td></td>
						<td><input type="submit" name="Submit"
							value="Search by location" /></td>
					</tr>
				</table>
			</form>

			<br/><br/>
			<div id="search_results"></div>

		</div>
	</div>
	
	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>