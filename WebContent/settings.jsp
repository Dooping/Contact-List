<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Settings</title>
</head>
<body>
	<a href="/Authenticator/" accesskey="1" title="">Home</a>
	<h1>Settings</h1>
	
	<form name="settingsform" action="/Authenticator/Settings" method="POST">
  			Sex:<select name="sexSelectedOption">
  					  <option value="-" ${selectedSex eq "F".charAt(0) ? 'selected="selected"' : ''}>-</option>
					  <option value="female" ${selectedSex eq "F".charAt(0) ? 'selected="selected"' : ''}>Female</option>
					  <option value="male" ${selectedSex eq "M".charAt(0) ? 'selected="selected"' : ''}>Male</option>
				 </select> <br>
  			Work:<input type="text" size=20 name="work" value="${contactDetails.work}"><br>
  			Birth:<input type="text" size=20 name="birth" value="${contactDetails.birthdate}"><br>
  			Lives in:<input type="text" size=20 name="livesin" value="${contactDetails.location}"><br>
  			From:<input type="text" size=20 name="from" value="${contactDetails.origin}"><br>
  			Email<input type="text" size=20 name="email" value="${contactDetails.email}"><br>
  			Phone Number:<input type="number" size=20 name="phonenumber" value="${contactDetails.phone}"><br>
  			<input type="submit" value="Save" onClick="{document.form.submit();}"/>
	</form>
	
</body>
</html>