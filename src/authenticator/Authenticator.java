package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;

public class Authenticator implements IAuthenticator{

	public void create_account(String name, String pwd1, String pwd2) throws EmptyFieldException {
		if(name.length() == 0 || pwd1.length() == 0 || pwd2.length() == 0 ){
			throw new EmptyFieldException();
		} else {
			//do something	
		}

		
	}
	public void delete_account(String name) {
		// TODO Auto-generated method stub
		
	}
	public Account get_account(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void change_pwd(String name, String pwd1, String pwd2) {
		// TODO Auto-generated method stub
		
	}
	
	public Account login(String name, String pwd) throws AuthenticationError {
		// TODO Auto-generated method stub
		throw new AuthenticationError();
		//return null;
	}

	public void logout(Account acc) {
		// TODO Auto-generated method stub
		
	}
	
	public Account login(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return null;
	}

}
