<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Contact List</title>
<link href="<c:url value="css/main.css" />" rel="stylesheet">
</head>
<body>
	
  <a href="/Authenticator/" accesskey="1" title="">Home</a>
  
  <h1>Contacts on the system</h1>
  
  
  <c:if test="${listSize == 0}">
 	<p> You don't have friend Requests </p>
   </c:if>
   
    <c:if test="${listSize > 0}">
 		<form name="form" method="post" action="">
 		
		  <input type="hidden" name="name" />
		  
		  <table class="center" cellspacing="0" cellpadding="0">
		    <thead>
		      <tr>
		        <th id="column_name"><span>Name</span></th>
		      </tr>
		    </thead>
		    <tbody>
		     	<c:forEach items="${list}" var="name">
		     		<tr id="elem" onclick="{document.form.name.value='${name}';document.form.submit();}" >
		            	<td id="username_${name}">${name}</td>
		        	</tr>
			    </c:forEach>
		    </tbody>
		  </table>
		 </form>
   </c:if>
  
</body>
</html>