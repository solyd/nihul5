<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />

<title>Login</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container">
		<div class="padding">
			<h2>This website was made by:</h2>

			<div class="even_left">
				<div class="center_box_colorless">
					<h3>
						Alexander Yavorovsky<br />
					</h3>
					s0li.rg@gmail.com
				</div>
			</div>

			<div class="even_right">
				<div class="center_box_colorless">
					<h3>
						Grisha Kotler<br />
					</h3>
					grisha.kotler@gmail.com
				</div>
			</div>
			
			<div class="padding">
				<h3><u>Advanced features implemented</u></h3>
				Location based search web service & XML to KML transformation using XSLT
				<br/>
				<h3><u>DB used</u></h3>
				This site is using MySQL DB for storage
				<h3><u>Comments</u></h3>
				In <b>src/org/nihul5/other/CONST.java</b> you will find several flags and definitions relevant to this web application
				startup:
				
				<table id="mainTable" align="center">
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td><b>WEBAPP_NAME</b></td>
						<td>Should match the base url of the webapp</td>
					</tr>
					<tr>
						<td><b>HOST</b></td>
						<td>The address of the hosting machine</td>
					</tr>
					
					<tr>
						<td><b>WEBSERVICE_TIMEOUT</b></td>
						<td>How long we wait for response from remote web services (this is gloabl for all web services used) in milliseconds. Default is 10seconds</td>
					</tr>
					
					<tr>
						<td><b>DEBUG_MODE</b></td>
						<td>Currently it only affects the registration phase with RegistrationService. When <i>false</i> this webapp will register itself with the registration service, otherwise it will not</td>
					</tr>
					
					<tr>
						<td><b>RESET_DB</b></td>
						<td>If <i>true</i> then all DB tables will be deleted and re-created on startup</td>
					</tr>
				</table>

			</div>
		</div>
	</div>


	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>
