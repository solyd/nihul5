$(document).ready(addMessage);
var markersArray = new Array();
var messageType;

function tagsAction(action){
	$("#date_title").animate({ height: action, opacity: action }, 'slow');
	$("#date_data").animate({ height: action, opacity: action }, 'slow');
	$("#event_capacity").animate({ height: action, opacity: action }, 'slow');
	$("#consensus1").animate({ height: action, opacity: action }, 'slow');
	$("#consensus2").animate({ height: action, opacity: action }, 'slow');
	$("#consensus3").animate({ height: action, opacity: action }, 'slow');
}
//Add Event or Message
function addMessage(){
	messageType = "Post";
	$('#messageType').click(function() {
		document.getElementById('title').innerHTML = "New Message";
		tagsAction("hide");
		messageType = "Post";
	});
	$('#eventType').click(function() {
		document.getElementById('title').innerHTML = "New Event";
		tagsAction("show");
		messageType = "Event";
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
		getConsensuses();
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
	
	$('#add_row').click(addEvent);
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
	var day = splitDay[0];
	var month = splitDay[1];
	var year = splitDay[2];
	
	var splitTime = ($('#eventTime').val()).split(":");
	var hours = splitTime[0];
	var minutes = splitTime[1];
	
	//if (isNaN(day) || isNaN(month) ||isNaN(year) ||isNaN(hours) ||isNaN(minutes)) {  // d.valueOf() could also work
	   	//if date is invalid
	//}
	//else{
		var date = year + month + day + hours + minutes;
		return date;
	//}
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

var consensusNum = 0;
var consensuses = [];

function addEvent() {
	var text = $('#consensus_text').val();
	if (text){
		var appendToText = document.getElementById('myDiv');
		consensusNum++;
		var divIdName = "my" + consensusNum + "Div";
		var newDiv = document.createElement('tr');
		newDiv.setAttribute("id", divIdName);
		//newDiv.innerHTML = text + " <a href=\"javascript:;\" onclick=\"removeElement(\'"+divIdName+"\')\">Remove</a>";
		newDiv.innerHTML = text + " <button onclick=\"removeElement(\'"+divIdName+"','" +text+"\')\">Remove</button>";
		appendToText.appendChild(newDiv);
		consensuses.push(text);
	}
}

function removeElement(divNum,text) {
	var index = consensuses.indexOf(text);
	consensuses.splice(index, 1);
	var d = document.getElementById('myDiv');
	var oldDiv = document.getElementById(divNum);
	d.removeChild(oldDiv);
	consensusNum--;
}

function getConsensuses(){
/*	for (var i = 0; i < consensusNum; i++ ) {
		var text = consensuses[i];
		alert(text);
	}	*/
	return consensuses;
}
