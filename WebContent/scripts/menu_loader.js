google.maps.event.addDomListener(window, 'load', bindMenuClicks);

function bindMenuClicks() {
	$("#menu li").click(function (event) {
		switch ($(this).text()) {
		case 'Home':
			onMenuHomeClicked();
			break;
		case 'Users':
			onMenuUsersClicked();
			break;
		case 'Events':
			onEventsClicked();
			break;
		}
		
		event.stopPropagation();
	});
}

function onMenuHomeClicked() {
	alert('home clicked');
}

function onMenuUsersClicked() {
	$('#container').empty();
	history.pushState({}, 'Users', WEBAPP_NAME.concat('/users'));
	$('#container').load("jsp/users_list.jsp");
}

function onMenuEventsClicked() {
	
}
