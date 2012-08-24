var map;

function initialize() {
	var mapOptions = {
			center : new google.maps.LatLng(31.767, 35.194),
			zoom : 8,
			mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	
	map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);