package contact_list;

import java.util.List;

import database.DatabaseConnection;
import exceptions.EmptyFieldException;
import exceptions.InvalidRequestException;

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
	
}
