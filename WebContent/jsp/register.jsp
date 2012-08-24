<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>

<script>
	var textRegExp  = /^[a-z]+$/;
	var minNameLength = 1;

	function initRegister(){
		$('#email').blur(function(){  
			var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
			var emailToCheck = $(this).val();	
			if (pattern.test(emailToCheck)){
				tagHide("#email_check");
			}
			else{
				tagShow("#email_check");
			}
		});
		
		$('#firstname').blur(function(){
			checkInput($(this).val(), '#firstname_check', 1);
			
		});
		
		$('#lastname').blur(function(){
			checkInput($(this).val(), '#lastname_check', 1);
		});
		$('#password').blur(function(){
			if ($(this).val().length >= 5){
				tagHide("#password_check");
			}
			else{
				tagShow("#password_check");
			}
		});
	}
	
	function checkInput(value, tag, neededLength){
		if ((value.length >= neededLength) && (textRegExp.test(value))){
			tagHide(tag);
		}
		else{
			tagShow(tag);
		}
	}
	
	function tagHide(tag){
		$(tag).animate({ height: 'hide', opacity: 'hide' }, 'slow');
	}
	
	function tagShow(tag){
		$(tag).animate({ height: 'show', opacity: 'show' }, 'slow');
	}
	
	//$(this).replaceWith("<div>New Div!</div>");
	window.onload = initRegister;
</script>

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
						<td><input id="username" type="text" name="<%=CONST.USERNAME %>" value="" /></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input id="password" type="password" name="<%=CONST.PASSWD %>" value="" /></td>
					</tr>
					<tr>
						<td id="password_check" colspan=2 align="center" height="10px" style="display: none">Please enter a valid Password</td>
					</tr>					
					<tr>
						<td>First Name</td>
						<td><input id="firstname" type="text" name="<%=CONST.FIRST_NAME %>" value="" /></td>
					</tr>
					<tr>
						<td id="firstname_check" colspan=2 align="center" height="10px" style="display: none">Please enter a valid First Name</td>
					</tr>					
					<tr>
						<td>Last Name</td>
						<td><input id="lastname" type="text" name="<%=CONST.LAST_NAME %>" value="" /></td>
					</tr>
					<tr>
						<td id="lastname_check" colspan=2 align="center" height="10px" style="display: none">Please enter a valid Last Name</td>
					</tr>					
					<tr>
						<td>Email</td>
						<td><input id="email" type="text" name="<%=CONST.EMAIL %>" value="" /></td>
					</tr>
					<tr>
						<td id="email_check" colspan=2 align="center" height="10px" style="display: none">Please enter a valid email address</td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" name="Submit" value="Register" /></td>
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