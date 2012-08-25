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

}
