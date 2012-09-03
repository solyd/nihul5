//google.maps.event.addDomListener(window, 'load', initialize);
$(document).ready(initialize);

var minLat = -90;
var maxLat = 90;
var minLng = -180;
var maxLng = 180;

var map;
var mainMarker = null;
var markersArray = new Array();

function initialize() {
	var mapOptions = {
			center : new google.maps.LatLng(31.767, 35.194),
			zoom : 8,
			mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	
	map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
}

function placeMarker(position) {
	if (mainMarker){
		mainMarker.setPosition(position);
	}
	else{
		mainMarker = new google.maps.Marker({
			position: position,
			map: map,
		});
	}
	map.panTo(position);

	mainMarker.setAnimation(google.maps.Animation.DROP);
	mainMarker.setAnimation(google.maps.Animation.BOUNCE);
	setTimeout(function() {
		mainMarker.setAnimation(null);
	}, 2000);
	
	return mainMarker;
}

/*function clearMarkers(){
	if (markersArray) {
		for (var i = 0; i < markersArray.length; i++ ) {
			google.maps.event.clearInstanceListeners(markersArray[i]);
			markersArray[i].setMap(null);
		}
	}
}*/

function deployPosition(lat, lng){
	if ((lat) && (lng)){
		if (inRange(minLat, lat, maxLat) && (inRange(minLng, lng, maxLng))){
			var newPosition = new google.maps.LatLng(lat, lng);
			var marker = mainMarker;
			placeMarker(newPosition);
			if (marker == null){
				mainMarker.setDraggable(true);
				google.maps.event.addListener(mainMarker, "drag", function(event) {
					changeLatLngValues(event);
				});
			}
		}
	}
}

function inRange(min, number, max){
	if ((!isNaN(number)) && (number >= min) && (number <= max)){
		return true;
	}
	else {
		return false;
	}
}

function changeLatLngValues(event){
	document.getElementById('lat').value = event.latLng.lat();
	document.getElementById('lng').value = event.latLng.lng();
}


/*	google.maps.event.addListener(map, 'click', function(event) {
		placeMarker(event.latLng);
		$('#latitude').value = event.latLng.lat();
		$('#longitude').value = event.latLng.lat();
	});*/

/*function placeMarker(position) {
	var marker = new google.maps.Marker({
		position: position,
		map: map
	});
	
	markers.push(marker);
	map.panTo(position);
}

function clearMap(){
	if (markersArray) {
		for (var i = 0; i < markersArray.length; i++ ) {
			markersArray[i].setMap(null);
		}
	}
}

//Add Event or Message
function addMessage(){
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

function deployPosition(lat, lng){
	if (inRange(minLat, lat, maxLat) && (inRange(minLng, lng, maxLng))){
		var newPosition = new google.maps.LatLng(lat, lng);
		placeMarker(newPosition);
	}
}

function inRange(min, number, max){
	if ((!isNaN(number)) && (number >= min) && (number <= max)){
		return true;
	}
	else {
		return false;
	}
}*/
