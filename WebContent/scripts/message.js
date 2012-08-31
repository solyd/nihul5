function showDateInFormat(miliseconds, tag){
	var date = new Date(miliseconds);
	var day = padStr(date.getUTCDate()) + "/" + padStr(date.getUTCMonth()+1) + "/" + padStr(date.getUTCFullYear());
	var time = padStr(date.getHours()) + ":" + padStr(date.getMinutes());
	$('#' + tag).text(day + ", " + time);
}

function padStr(i) {
    return (i < 10) ? "0" + i : "" + i;
}
