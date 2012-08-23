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
		<div id="center_box">
			<form name="registration_form" method="post" action="/<%=CONST.WEBAPP_NAME %>/register">
				<table align="center">
					<tr>
						<td colspan=2 align="center">Registration</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Username</td>
						<td><input type="text" name="<%=CONST.USERNAME %>" value=""></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="<%=CONST.PASSWD %>" value=""></td>
					</tr>
					<tr>
						<td>First Name</td>
						<td><input type="text" name="<%=CONST.FIRST_NAME %>" value=""></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="<%=CONST.LAST_NAME %>" value=""></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" name="Submit" value="Register"></td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>