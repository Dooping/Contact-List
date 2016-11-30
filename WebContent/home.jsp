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
			
		<form name="createuser"€ action="/Authenticator/CreateUser" method="GET">
			  <input type="submit" value="Create User">
		</form>
		
		<form name="deleteuser" action="/Authenticator/DeleteUser" method="GET">
			  <input type="submit" value="Delete User">
		</form>
		
		<form name=changepassword action="/Authenticator/ChangePassword" method="GET">
			  <input type="submit" value="Change Password">
		</form>
		
		<form name="lockuser" action="/Authenticator/LockUser" method="GET">
			<input type="submit" value="Lock/Unlock User">
		</form>
		
		<form name="logout"€ action="/Authenticator/Logout" method="GET">
			  <input type="submit" value="Logout">
		</form>
		
		<form name="addfriend" action="/Authenticator/AddFriend" method="GET">
			  <input type="submit" value="Add Friend">
			  <input type="hidden" name="friendName" value="${name}"></input>
		</form>
		
		<form name="removefriend" action="/Authenticator/RemoveFriend" method="GET">
			  <input type="submit" value="Remove Friend">
			  <input type="hidden" name="friendName" value="${name}"></input>
		</form>
		
		<form name="friendlist" action="/Authenticator/FriendsList" method="GET">
			  <input type="submit" value="Friends List">
		</form>
		
		<form name="contactlist" action="/Authenticator/ContactsList" method="GET">
			  <input type="submit" value="Contacts List">
		</form>
		
		<form name="friendRequests" action="/Authenticator/FriendRequests" method="GET">
			  <input type="submit" value="Friend Requests">
		</form>
		<div style="display:${displayUserFriendButton}">
			<form name="userFriends" action="/Authenticator/UserFriends" method="GET">
				  <input type="submit" value="User Friends">
			</form>
		</div>
		<form name="settings" action="/Authenticator/Settings" method="GET">
			  <input type="submit" value="Settings">
		</form>
			
		<h3>Profile</h3>
		<p>Name: ${name}</p>
		<div style="display:${displayProfile}">
			<p>Age: ${age}</p>
			<p>Sex: ${sex}</p>
			<p>Work: ${work}</p>
			<p>Birth: ${birth}</p>
			<p>Lives in: ${lives}</p>
			<p>From: ${from}</p>
		</div>
		<div style="display:${displayContacts}">
			<h3>Contacts</h3>
			<p>Email: ${email}</p>
			<p>Phone Number: ${phonenumber}</p>
		</div>
		<div style="display:${displayInternal}">
			<h3>Internal Statement</h3>
			<p>${internal_statement}<p>
		</div>
		<div>	
			<h3>External Statment</h3>
			<p>${external_statement}</p>
		</div>
		
</body>
</html>