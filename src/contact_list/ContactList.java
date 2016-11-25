package contact_list;

import java.util.List;

import database.DatabaseConnection;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;

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
	
}
