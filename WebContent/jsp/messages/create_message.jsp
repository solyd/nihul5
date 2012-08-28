<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
    
<%@ page import="org.nihul5.other.CONST"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">

<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME%>/styles/style.css" />

<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/menu_loader.js"></script>

<script type="text/javascript">
	google.maps.event.addDomListener(window, 'load', initializeMap);

	var minLat = -90;
	var maxLat = 90;
	var minLng = -180;
	var maxLng = 180;

	var map;
	var markers = new Array();

	function initializeMap() {
		var mapOptions = {
			center : new google.maps.LatLng(31.767, 35.194),
			zoom : 8,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);

		google.maps.event.addListener(map, 'click', function(event) {
			clearMap();						
			placeMarker(event.latLng);

			document.getElementById('lat').value = event.latLng.lat();
			document.getElementById('lng').value = event.latLng.lng();
		});
	}

	function placeMarker(position) {
		var marker = new google.maps.Marker({
			position : position,
			map : map
		});

		markers.push(marker);
		map.panTo(position);
	}

	function clearMap() {
		if (markers) {
			for ( var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
		}
	}

	//Add Event or Message
	function addMessage() {
		//clearMap();
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
			placeMarker(event.latLng);
			document.getElementById('lat').value = event.latLng.lat();
			document.getElementById('lng').value = event.latLng.lng();
		});
	}

	function deployPosition(lat, lng) {
		if (inRange(minLat, lat, maxLat) && (inRange(minLng, lng, maxLng))) {
			var newPosition = new google.maps.LatLng(lat, lng);
			placeMarker(newPosition);
		}
	}

	function inRange(min, number, max) {
		if ((!isNaN(number)) && (number >= min) && (number <= max)) {
			return true;
		} else {
			return false;
		}
	}
</script>



<title>Create Event</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right">
			<%@ include file="/jsp/messages/create_msg_form.jsp"%>
		</div>
	</div>
	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>