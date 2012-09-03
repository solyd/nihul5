$(document).ready(addMessage);

var messageType;
var MAX_CONSENSUS = 5;

function tagsAction(action){
	$("#date_title").animate({ height: action, opacity: action }, 'slow');
	$("#date_data").animate({ height: action, opacity: action }, 'slow');
	$("#event_capacity").animate({ height: action, opacity: action }, 'slow');
	$("#consensus1").animate({ height: action, opacity: action }, 'slow');
	$("#consensus2").animate({ height: action, opacity: action }, 'slow');
	$("#consensus3").animate({ height: action, opacity: action }, 'slow');
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
		var marker = mainMarker;
		placeMarker(event.latLng);
		changeLatLngValues(event);
		if (marker == null){
			mainMarker.setDraggable(true);
			google.maps.event.addListener(mainMarker, "drag", function(event) {
				changeLatLngValues(event);
			});
		}
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
	
	$('#add_row').click(addElement);
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
					padStr(temp.getMinutes());
	return timeStr;
}

var consensusNum = 0;
var consensuses = [];

function addElement() {
	var text = $('#consensus_text').val();
	if ((text) && (consensusNum<MAX_CONSENSUS)){
		var appendToText = document.getElementById('myDiv');
		consensusNum++;
		var divIdName = "my" + consensusNum + "Div";
		var newDiv = document.createElement('tr');
		newDiv.setAttribute("id", divIdName);
		//newDiv.innerHTML = text + " <a href=\"javascript:;\" onclick=\"removeElement(\'"+divIdName+"\')\">Remove</a>";
		newDiv.innerHTML = text + " <button onclick=\"removeElement(\'"+divIdName+"','" +text+"\')\">Remove</button>";
		appendToText.appendChild(newDiv);
		consensuses.push(text);
		document.getElementById('consensus_text').value = "";
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

function getDateString() {
	return new Date().getTime();
	
    var temp = new Date();
    var dateStr = padStr(temp.getFullYear()) +
                  padStr(1 + temp.getMonth()) +
                  padStr(temp.getDate()) +
                  padStr(temp.getHours()) +
                  padStr(temp.getMinutes()) +
                  padStr(temp.getSeconds());
    return dateStr;
}
