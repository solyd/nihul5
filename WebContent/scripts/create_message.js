$(document).ready(addMessage);

var markersArray = new Array();
var messageType;

function tagsAction(action){
	$("#date_title").animate({ height: action, opacity: action }, 'slow');
	$("#date_data").animate({ height: action, opacity: action }, 'slow');
	$("#event_capacity").animate({ height: action, opacity: action }, 'slow');
}
//Add Event or Message
function addMessage() {
	messageType = "Post";
	$('#messageType').click(function() {
		$('#create_head').text('New Post');
		tagsAction("hide");
		messageType = "POST";
	});
	$('#eventType').click(function() {
		$('#create_head').text('New Event');
		tagsAction("show");
		messageType = "EVENT";
	});
	
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
	
	dateObject = new JsDatePick({
		weekStartDay:0,
		useMode:2,
		target:"eventDay",
		dateFormat:"%j/%n/%Y"
	});
	document.getElementById('eventDay').value = getDay();
	
	$("#eventTime").timePicker({
		  endTime: new Date(0, 0, 0, 23, 45, 0), // Using Date object here.
		  show24Hours: true,
		  step: 15});
	document.getElementById('eventTime').value = getTime();
}

function placeMarker(position) {
	clearMap();
	var marker = new google.maps.Marker({
		position: position,
		map: map
	});
	markersArray.push(marker);
	map.panTo(position);
}

function clearMap(){
	if (markersArray) {
		for (var i = 0; i < markersArray.length; i++ ) {
			markersArray[i].setMap(null);
		}
	}
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
}

function getEventDate(){

	var splitDay = ($('#eventDay').val()).split("/");
	var day = padStr(splitDay[0]);
	var month = padStr(splitDay[1]);
	var year = padStr(splitDay[2]);
	
	var splitTime = ($('#eventTime').val()).split(":");
	var hours = padStr(splitTime[0]);
	var minutes = padStr(splitTime[1]);
	var sec = padStr('0');
	
	var date = new Date(year, month-1, day, hours, minutes, sec, '0');
	var utc = new Date(date.toUTCString());
	
	return utc.getTime();
}

function getEventCapacity(){
	return ($('#capacity').val());
}

function padStr(i) {
    return (i < 10) ? "0" + i : "" + i;
}

function getDay(){
	var temp = new Date();
    var dayStr = padStr(temp.getDate()) + '/' +
    				padStr(1 + temp.getMonth()) + '/' +
    				padStr(temp.getFullYear());
    return dayStr;
}

function getTime(){
	var temp = new Date();
	var timeStr = padStr(temp.getHours()) + ':' +
					padStr(1 + temp.getMinutes());
	return timeStr;
}
