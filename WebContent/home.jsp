<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
		<a href="/Authenticator/" accesskey="1" title="">Home</a>
		<p>${welcomeMessage}<p> 
		
		<h1>Software Security</h1>
			
		<form name=“createuser” action="/Authenticator/CreateUser" method="GET">
			  <input type="submit" value="Create User">
		</form>
		
		<form name="deleteuser" action="/Authenticator/DeleteUser" method="GET">
			  <input type="submit" value="Delete User">
		</form>
		
		<form name=changepassword action="/Authenticator/ChangePassword" method="GET">
			  <input type="submit" value="Change Password">
		</form>
		
		<form name="lockuser" action="/Authenticator/LockUser" method="GET">
			<input type="submit" value="Lock User">
		</form>
		
		<form name=“logout” action="/Authenticator/Logout" method="GET">
			  <input type="submit" value="Logout">
		</form>
		
		<form name="addfriend" action="/Authenticator/AddFriend" method="GET">
			  <input type="submit" value="Add Friend">
		</form>
		
		<form name="removefriend" action="/Authenticator/RemoveFriend" method="GET">
			  <input type="submit" value="Remove Friend">
		</form>
		
		<form name="friendlist" action="/Authenticator/FriendList" method="GET">
			  <input type="submit" value="Friends List">
		</form>
		
		<form name="contactlist" action="/Authenticator/ContactsList" method="GET">
			  <input type="submit" value="Contacts List">
		</form>
		
		<form name="settings" action="/Authenticator/Settings" method="GET">
			  <input type="submit" value="Settings">
		</form>
		
		<div>
			<h3>Personal Info</h3>
			<p>Name: ${name}</p>
			<p>Age: ${age}</p>
			<p>Sex: ${sex}</p>
			<p>Work: ${work}</p>
			<p>Birth: ${birth}</p>
			<p>Lives in: ${lives}</p>
			<p>From: ${from}</p>
			<p>Email: ${email}</p>
			<p>Phone Number: ${phonenumber}</p>
		</div>
		
</body>
</html>