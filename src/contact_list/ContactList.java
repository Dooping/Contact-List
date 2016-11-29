package contact_list;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import database.DatabaseConnection;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.InvalidRequestException;
import exceptions.PermissionNotExistsException;

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
	
	public List<String> listContacts(Boolean withLocked){
		return DatabaseConnection.getUserList(withLocked);
	}

	public int getContactId(String username) throws EmptyFieldException, UndefinedAccount{
		if(username.length()==0)
			throw new EmptyFieldException();
		return DatabaseConnection.getAccountId(username);
	}
	
	public void newFriendRequest(String requester, String accepter) throws EmptyFieldException, InvalidRequestException{
		if (requester.length() == 0 || accepter.length() == 0)
			throw new EmptyFieldException();
		if(!DatabaseConnection.newFriendRequest(requester, accepter))
			throw new InvalidRequestException();
	}
	
	public void deleteFriend(String user, String friend) throws EmptyFieldException, InvalidRequestException{
		if (user.length() == 0 || friend.length() == 0)
			throw new EmptyFieldException();
		if(!DatabaseConnection.deleteFriend(user, friend))
			throw new InvalidRequestException();
	}
	
	public void acceptFriend(String requester, String accepter) throws EmptyFieldException, InvalidRequestException{
		if (requester.length() == 0 || requester.length() == 0)
			throw new EmptyFieldException();
		if(!DatabaseConnection.acceptFriendRequest(requester, accepter))
			throw new InvalidRequestException();
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
	
	public String checkInformationPermission(String owner, String resourceName) throws PermissionNotExistsException{
		return DatabaseConnection.checkInformationPermission(owner, resourceName);
	}
	
	public void setInformationPermission(String permission, String owner, String resourceName) throws InvalidRequestException{
		if(!DatabaseConnection.setInformationPermission(permission, owner, resourceName))
			throw new InvalidRequestException();	
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
	
}
