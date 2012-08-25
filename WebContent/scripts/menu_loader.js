var markersArray = new Array();
var minLat = -90;
var maxLat = 90;
var minLng = -180;
var maxLng = 180;

function bindMenuClicks() {
	$("#menu li a").click(function (event) {
		switch ($(this).text()) {
		case 'Home':
			onMenuHomeClicked();
			break;
		case 'Users':
			onMenuUsersClicked();
			break;
		case 'Add Message':
			onAddMessageClicked();
			break;
		}

		event.stopPropagation();
	});
}

function onMenuHomeClicked() {
	//clearMap();

}

function onMenuUsersClicked() {

}

function onAddMessageClicked() {
//	Add Event or Message
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
		if ((lat.length > 0) && (lng.length > 0)){
			if (inRange(minLat, lat, maxLat) && (inRange(minLng, lng, maxLng))){
				var newPosition = new google.maps.LatLng(lat, lng);
				placeMarker(newPosition);
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

//	Place a new marker on the map
	function placeMarker(position) {
		clearMap();
		var marker = new google.maps.Marker({
			position: position,
			map: map
		});
		markersArray.push(marker);
		map.panTo(position);
	}

//	Clean the map from markers
	function clearMap(){
		if (markersArray) {
			for (var i = 0; i < markersArray.length; i++ ) {
				markersArray[i].setMap(null);
			}
		}

	}
}
