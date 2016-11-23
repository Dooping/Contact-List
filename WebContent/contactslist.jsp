<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Contact List</title>
</head>
<body>
	<div> 
		<h3> Contacts List </h3>
	</div>
	<div>
		<table>
	    	<c:forEach items="${list}" var="acc">
	        	<tr>
	            	<td>Username : ${acc.username}</td>
	        	</tr>
	    	</c:forEach>
	     </table>
	 </div>
</body>
</html>