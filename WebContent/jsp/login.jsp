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
		<div class="center_box">

			<form method="post" action="j_security_check">
				<table align="center">
					<tr>
						<td colspan=2 align="center">Login</td>
					</tr>
					<tr>
						<td colspan=2 align="center" height="10px"></td>
					</tr>
					<tr>
						<td>Username:</td>
						<td><input type="text" name="j_username" /></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="j_password" /></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="2"><input type="submit" value="Go" /></td>
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
