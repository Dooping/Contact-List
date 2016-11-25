<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friends Requests</title>
<link href="<c:url value="css/main.css" />" rel="stylesheet">
</head>
<body>
	<a href="/Authenticator/" accesskey="1" title="">Home</a>
  
  <h1>Friend Requests</h1>
  
  <c:if test="${listSize == 0}">
 	<p> You don't have friend Requests </p>
   </c:if>
   
   <c:if test="${(listSize > 0)}">
 		<form name="form" method="post" action="">
 		
			  <input type="hidden" name="name" />
			  <input type="hidden" name="accept_reject"/>
			  
			  <table class="center" cellspacing="0" cellpadding="0">
			    <thead>
			      <tr>
			        <th colspan="1" scope="colgroup" id="column_name"><span>Name</span></th>
			        <th colspan="2" scope="colgroup" id="column_name"></th>
			        
			      </tr>
			    </thead>
			    <tbody>
		     	<c:forEach items="${list}" var="name">
		     		<tr>
		            	<td id="elem" onclick="{document.form.name.value='${name}';document.form.submit();}" >${name}</td>
		            	<td>
		            		 <input type="submit" value="Accept" onclick="{document.form.accept_reject.value='accepted';document.form.name.value='${name}';document.form.submit();}">
		            	</td>
		            	<td>
		            		 <input type="submit" value="Reject"onclick="{document.form.accept_reject.value='rejected';document.form.name.value='${name}';document.form.submit();}">
		            	</td>
		        	</tr>
			    </c:forEach>
		    </tbody>
		  </table>
	  </form>
   </c:if>
   
	
	
</body>
</html>