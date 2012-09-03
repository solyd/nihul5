<%@ page import="org.nihul5.other.CONST"%>
<%@ page import="org.nihul5.other.Message"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false&libraries=geometry"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/markerclusterer.js"></script>

<script>
$(document).ready(function(){
	
	$('#demo_button').click(function() {
		$.post('/<%=CONST.WEBAPP_NAME%>/message/search',
				{<%=CONST.MSG_LATITUDE%>: 32.7101808529167, <%=CONST.MSG_LONGITUDE%>: 35.03469824218746, 
					<%=CONST.RADIUS%>: 45, <%=CONST.IS_JSON%>: "true"}, function(response) {
						var msgArr = response.result;
						if (msgArr.length == 0)
							console.log("no such messages");
						for (var i = 0; i < msgArr.length; i++) {
							console.log(msgArr[i].lat + ' ' + msgArr[i].lng + ' ' + msgArr[i].id);
						}
		});		
	});
	
	var latLng1 = new google.maps.LatLng(31.962943042014572, 35.54006933593746);
	var latLng2 = new google.maps.LatLng(32.2,35.2);
	var latLng3 = new google.maps.LatLng(32.4,35.4);
	var latLng4 = new google.maps.LatLng(32.6,35.6);
	var marker1 = new google.maps.Marker({ position: latLng1 });
	var marker2 = new google.maps.Marker({ position: latLng2 });
	var marker3 = new google.maps.Marker({ position: latLng3 });
	var marker4 = new google.maps.Marker({ position: latLng4 });
	marker1.setTitle("4");
	marker2.setTitle("3");
	marker3.setTitle("28");
	marker4.setTitle("31");
	markersArray.push(marker1);
	markersArray.push(marker2);
	markersArray.push(marker3);
	markersArray.push(marker4);
//var mcOptions = {gridSize: 50, maxZoom: 15};
/* 	var infowindow = new google.maps.InfoWindow(
      { content: marker1.getTitle(),
        size: new google.maps.Size(50,50)
      }); */
    var markerCluster = new MarkerClusterer(map, markersArray);
      //markerCluster.clearMarkers(); //clear all markers
      //markerCluster.resetViewport(); //regroup all markers
      
      //bounds_changed //event of bounds change

	google.maps.event.addListener(marker1, 'click', function() {
  		var msgId = marker1.getTitle();
		$.get('/<%=CONST.WEBAPP_NAME%>/GetMessage',
				{<%=CONST.MSG_ID%>: msgId}, function(response) {
					$('#content').replaceWith(response);
 					var $data=$(response);
					var result = $data.find('#message_status').text();
					if (result == 'Deleted'){
						markerCluster.removeMarker(marker1);
					}
		});
	}); 
	
	google.maps.event.addListener(marker2, 'click', function() {
  		var msgId = marker2.getTitle();
		$.get('/<%=CONST.WEBAPP_NAME%>/GetMessage',
				{<%=CONST.MSG_ID%>: msgId}, function(response) {
					$('#content').replaceWith(response);
 					var $data=$(response);
					var result = $data.find('#message_status').text();
					if (result == 'Deleted'){
						markerCluster.removeMarker(marker2);
					} 
		});
	}); 


/* 	var test = new Array();
	test[marker1.getTitle()] = true;
	test[marker2.getTitle()] = true;
	
	alert(test[marker3.getTitle()]); */


    google.maps.event.addListener(map, 'idle', function(){
    	var bounds = this.getBounds();
    	var center = bounds.getCenter();
    	var northEast = bounds.getNorthEast();
    	var southWest = bounds.getSouthWest();

        var distance1 = google.maps.geometry.spherical.computeDistanceBetween(center, northEast);
        var distance2 = google.maps.geometry.spherical.computeDistanceBetween(center, southWest);
        var radius = (distance1>distance2) ? distance1 : distance2;

    });

	//apply kml layer to map
<%-- 	$('#display_kml').click(function(){
		$.get('/<%=CONST.WEBAPP_NAME%>/CreateKml', {},
			function(response) {
				var kmlLayer = new google.maps.KmlLayer(response);
				kmlLayer.setMap(map);
		});
	}); --%>

});



</script>
<title>Home</title>
</head>

<body>
	<%@ include file="/jsp/status_bar.jsp"%>
	<%@ include file="/jsp/menu.jsp"%>

	<div id="container" class="right">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right" align="center">
			<div id="center_box">Select messages on the map to see their info</div>
			<input id="demo_button" type="button" value="demo"/>
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
