<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.pajinate.js"></script>
<script type="text/javascript">
$(document).ready(function(){
				$('#page_container').pajinate();
			});
						$(document).ready(function(){
				$('li:odd, .content > *:odd').css('background-color','#FFD9BF');
			});
	
</script>
<title>Insert title here</title>
</head>
<body>
	<div id="page_container">
		<div class="page_navigation"></div>
		<ul class="content">
			<li>
				<p>One</p>
			</li>
			<li>
				<p>Two</p>
			</li>
			<li>
				<p>Three</p>
			</li>
			<li>
				<p>Four</p>
			</li>
			<li>
				<p>Five</p>
			</li>
			<li>
				<p>Six</p>
			</li>
			<li>
				<p>Seven</p>
			</li>
			<li>
				<p>Eight</p>
			</li>
			<li>
				<p>Eight</p>
			</li><li>
				<p>Eigifohewfiewht</p>
			</li><li>
				<p>Eighefoihwfoiewt</p>
			</li><li>
				<p>Eigfoehwoifewfht</p>
			</li><li>
				<p>Eighoiewhfewt</p>
			</li><li>
				<p>Eigoiewhfewoiht</p>
			</li><li>
				<p>Eigwoeihfoewhht</p>
			</li><li>
				<p>Eigoewihfoewiht</p>
			</li><li>
				<p>Eigoewihfoewifht</p>
			</li><li>
				<p>Eigeowifhewofiweht</p>
			</li><li>
				<p>Eigoewihfewoihfht</p>
			</li><li>
				<p>Eightweeowihfoewi</p>
			</li><li>
				<p>Eigweoihfoewifhwoiefht</p>
			</li><li>
				<p>Eiwoeifhoewight</p>
			</li>
		</ul>
	</div>
</body>
</html>