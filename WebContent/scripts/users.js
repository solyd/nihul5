var appname = "/nihul5";

$(window).load(function() {
	loadPage("1");
});

function loadPage(pageNum) {
	$.post(appname + '/users', { page: pageNum }, function(data) {
		$('#user_list').replaceWith(data);

		$('#user_list li').click(function (event) {
			onUserClicked($(this).text());
		});
	});	
}

function onUserClicked(username_) {
	$.post(appname + '/users/profile/' + username_, { username: username_}, function(data) {
		$('#user_info').replaceWith(data);
	});
}





