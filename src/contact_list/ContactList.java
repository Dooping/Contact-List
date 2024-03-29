package contact_list;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import authenticator.Account;
import database.DatabaseConnection;
import exceptions.AccessControlError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.InvalidRequestException;
import exceptions.LockedAccount;
import exceptions.RequestSelfException;

public class ContactList {
	
	public List<String> listFriends(String username) throws EmptyFieldException{
		if (username.length() == 0)
			throw new EmptyFieldException();
		return DatabaseConnection.getFriends(username);
	}
	
	public List<String> listFriendRequests(String username) throws EmptyFieldException{
		if (username.length() == 0)
			throw new EmptyFieldException();
		return DatabaseConnection.getFriendRequests(username);
	}
	
	public List<Account> listContacts(Boolean withLocked){
		return DatabaseConnection.getUserList(withLocked);
	}

	public int getContactId(String username) throws EmptyFieldException, UndefinedAccount{
		if(username.length()==0)
			throw new EmptyFieldException();
		return DatabaseConnection.getAccountId(username);
	}
	
	public void newFriendRequest(String requester, String accepter) throws EmptyFieldException, InvalidRequestException, RequestSelfException{
		if (requester.length() == 0 || accepter.length() == 0)
			throw new EmptyFieldException();
		if (requester.equals(accepter))
			throw new RequestSelfException();
		if(!DatabaseConnection.newFriendRequest(requester, accepter))
			throw new InvalidRequestException();
	}
	
	public void deleteFriend(String user, String friend) throws EmptyFieldException, InvalidRequestException, RequestSelfException{
		if (user.length() == 0 || friend.length() == 0)
			throw new EmptyFieldException();
		if (user.equals(friend))
			throw new RequestSelfException();
		if(!DatabaseConnection.deleteFriend(user, friend))
			throw new InvalidRequestException();
		DatabaseConnection.deleteFriendAccessControl(user, friend);
		DatabaseConnection.deleteFriendAccessControl(friend, user);
	}
	
	public void acceptFriend(String requester, String accepter) throws EmptyFieldException, InvalidRequestException{
		if (requester.length() == 0 || requester.length() == 0)
			throw new EmptyFieldException();
		if(!DatabaseConnection.acceptFriendRequest(requester, accepter))
			throw new InvalidRequestException();
		DatabaseConnection.addFriendAccessControl(requester, accepter);
		DatabaseConnection.addFriendAccessControl(accepter, requester);
	}
	
	public void rejectFriend(String requester, String accepter) throws EmptyFieldException, InvalidRequestException{
		if (requester.length() == 0 || requester.length() == 0)
			throw new EmptyFieldException();
		if(!DatabaseConnection.denyFriendRequest(requester, accepter))
			throw new InvalidRequestException();
	}
	
	public ContactDetailed getContactDetails(String name) throws UndefinedAccount{
		return DatabaseConnection.getUserDetails(name);
	}
	
	public int getAge(long birthDateMillis){
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(System.currentTimeMillis() - birthDateMillis);
		return c.get(Calendar.YEAR) - 1970;
	}
	
	public void setContactDetails(ContactDetailed cd){
		DatabaseConnection.setUserDetails(cd);
	}
	
	public String checkInformationPermission(String owner, String resourceName) throws AccessControlError{
		return DatabaseConnection.checkInformationPermission(owner, resourceName);
	}
	
	public void setInformationPermission(String permission, String owner, String resourceName) throws InvalidRequestException{
		if(!DatabaseConnection.setInformationPermission(permission, owner, resourceName))
			throw new InvalidRequestException();
		if(permission.equals("internal"))
			DatabaseConnection.refreshFriendsAccessControl(owner, resourceName);
		else
			DatabaseConnection.deleteFriendsAccessControl(resourceName, owner);
	}
	
	public String getSelectedDropdown(int dropdown, String r1, String r2, String r3){
		String result = "";
		switch(dropdown){
		case 1:
			result = r1;
			break;
		case 2:
			result = r2;
			break;
		case 3:
			result = r3;
			break;
		}
		return result;
	}
	
	public void resetPermissions(String username){
		Random rn = new Random();
		int nonce = rn.nextInt();
		DatabaseConnection.resetNonce(username, nonce);
	}
	
	public boolean isLocked(String name) throws UndefinedAccount {
		return DatabaseConnection.isLocked(name);
	}
	
}
