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
  			
  			
  			<h3>Profile <select name="profile">
	  					  <option value="1" ${selectedProfilePermission eq "private" ? 'selected="selected"' : ''}>Private</option>
						  <option value="2"  ${selectedProfilePermission eq "internal" ? 'selected="selected"' : ''}>Internal</option>
						  <option value="3"  ${selectedProfilePermission eq "public" ? 'selected="selected"' : ''}>Public</option>
					   </select>
			</h3>
  			Sex:<select name="sexSelectedOption">
  					  <option value="1" ${selectedSex eq "-".charAt(0) ? 'selected="selected"' : ''}>-</option>
					  <option value="2" ${selectedSex eq "F".charAt(0) ? 'selected="selected"' : ''}>Female</option>
					  <option value="3" ${selectedSex eq "M".charAt(0) ? 'selected="selected"' : ''}>Male</option>
				 </select> <br>
  			Work:<input type="text" size=20 name="work" value="${contactDetails.work}"><br>
  			Birth:<input type="date" size=20 name="birth" value="${contactDetails.birthdate}"><br>
  			Lives in:<input type="text" size=20 name="livesin" value="${contactDetails.location}"><br>
  			From:<input type="text" size=20 name="from" value="${contactDetails.origin}"><br>
  			
  			
  			<h3>Contacts <select name="contacts">
	  					  <option value="1" ${selectedContactPermission eq "private" ? 'selected="selected"' : ''} >Private</option>
						  <option value="2" ${selectedContactPermission eq "internal" ? 'selected="selected"' : ''} >Internal</option>
						  <option value="3" ${selectedContactPermission eq "public" ? 'selected="selected"' : ''} >Public</option>
					   </select>
			</h3>
  			Email<input type="text" size=20 name="email" value="${contactDetails.email}"><br>
  			Phone Number:<input type="text" size=20 name="phonenumber" onkeypress='return event.charCode >= 48 && event.charCode <= 57' value="${contactDetails.phone}"><br>
  			
  			
  			<h3>Internal Statement</h3>
  			<textarea name="internal_statement_text" cols="60" rows="5">${contactDetails.internal_statement}</textarea>
  			
  			<h3>External Statement</h3>
  			<textarea name="external_statement_text" cols="60" rows="5">${contactDetails.external_statement}</textarea><br>
  			
  			<h3>Contact Friends Visibility <select name="contactsvisibility">
	  					  <option value="1" ${selectedfriendsPermission eq "private" ? 'selected="selected"' : ''} >Private</option>
						  <option value="2" ${selectedfriendsPermission eq "internal" ? 'selected="selected"' : ''} >Internal</option>
						  <option value="3" ${selectedfriendsPermission eq "public" ? 'selected="selected"' : ''} >Public</option>
					   </select>
			</h3>
  			
  			<input type="submit" value="Save" onClick="{document.form.submit();}"/>
	</form>
	
</body>
</html>