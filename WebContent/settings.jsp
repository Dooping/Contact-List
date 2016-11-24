<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
			Name:<input type=“text" size=20” name="name" autofocus><br>
  			Age:<input type="text" size=20 name="age"><br>
  			Sex:<select>
  					  <option value="-">-</option>
					  <option value="female">Female</option>
					  <option value="male">Male</option>
				 </select> <br>
  			Work:<input type="text" size=20 name="work"><br>
  			Birth:<input type="text" size=20 name="birth"><br>
  			Lives in:<input type="text" size=20 name="livesin"><br>
  			From:<input type="text" size=20 name="from"><br>
  			Email<input type="text" size=20 name="email"><br>
  			Phone Number:<input type="number" size=20 name="phonenumber"><br>
  			<input type="submit" value="Save"/>
	</form>
	
</body>
</html>