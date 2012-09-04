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
var markerCluster;

$(document).ready(function(){
	
	markerCluster = new MarkerClusterer(map);
	var markersToAdd = new Array();
	var markersOnMap = new Array();
	
	google.maps.event.addListener(map, 'idle', function() {
    	var bounds = this.getBounds();
    	var center = bounds.getCenter();
    	var latitude = bounds.getCenter().lat();
    	var longtitude = bounds.getCenter().lng();
    	var northEast = bounds.getNorthEast();
    	var southWest = bounds.getSouthWest();
        var distance1 = google.maps.geometry.spherical.computeDistanceBetween(center, northEast);
        var distance2 = google.maps.geometry.spherical.computeDistanceBetween(center, southWest);
        var radius = (distance1>distance2) ? distance1 : distance2;
        
        $.post('/<%=CONST.WEBAPP_NAME%>/message/search',
				{<%=CONST.MSG_LATITUDE%>: latitude, <%=CONST.MSG_LONGITUDE%>: longtitude, 
					<%=CONST.RADIUS%>: (radius/1000.0), <%=CONST.IS_JSON%>: "true"}, function(response) {
						var msgArr = response.result;
						
						if (msgArr.length == 0)
							console.log("no such messages");	
						for (var i = 0; i < msgArr.length; i++) {
							//console.log(msgArr[i].lat + ' ' + msgArr[i].lng + ' ' + msgArr[i].id);
							var msgId = msgArr[i].id;
							if (markersOnMap[msgId] != true){ 
								var position = new google.maps.LatLng(msgArr[i].lat, msgArr[i].lng);
								var marker = new google.maps.Marker({ position: position });
								marker.setTitle(msgId.toString());
								
								markersToAdd.push(marker);
								markersOnMap[msgId] = true;
								addMarkerListener(marker);
							}
						}
						/*for (var i = 0; i < markersToAdd.length; i++) {
							console.log(markersToAdd[i]);
						}*/
						markerCluster.addMarkers(markersToAdd); //add the markers to 
						markersToAdd.splice(0, markersToAdd.length); //delete the array
		});
	});
});

function addMarkerListener(marker){
	google.maps.event.addListener(marker, 'click', function() {
		var messageId = marker.getTitle();
		$.get('/<%=CONST.WEBAPP_NAME%>/GetMessage',
				{<%=CONST.MSG_ID%>: messageId}, function(response) {
					
					$('#content').replaceWith(response);
								
 					var $data=$(response);
					var result = $data.find('#message_status').text();
					if (result == 'Deleted'){
						console.log(marker.getTitle());
						google.maps.event.clearInstanceListeners(marker);
						markerCluster.removeMarker(marker);
					}
		});
	});	
}

	//apply kml layer to map
<%-- 	$('#display_kml').click(function(){
		$.get('/<%=CONST.WEBAPP_NAME%>/CreateKml', {},
			function(response) {
				var kmlLayer = new google.maps.KmlLayer(response);
				kmlLayer.setMap(map);
		});
	}); --%>

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
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp"%>
</body>
</html>
