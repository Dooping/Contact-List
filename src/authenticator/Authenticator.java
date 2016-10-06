package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DatabaseConnection;
import exceptions.UserAlreadyExistsException;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.UserNotCreatedException;
import exceptions.WrongConfirmationPasswordException;

public class Authenticator implements IAuthenticator{

	public void create_account(String name, String pwd1, String pwd2) throws EmptyFieldException, UserAlreadyExistsException, WrongConfirmationPasswordException,UserNotCreatedException, Exception {
		
		if(name.length() == 0 || pwd1.length() == 0 || pwd2.length() == 0 ){
			throw new EmptyFieldException();
		} else {
			IAccount account = DatabaseConnection.getAccount(name);
			if ( account != null ){
				throw new UserAlreadyExistsException();
			} else {
				if(!pwd1.equals(pwd2)){
					throw new WrongConfirmationPasswordException();
				} else {
					pwd1 = AESencrp.encrypt(pwd1);
					
					if(!DatabaseConnection.createUser(name, pwd1))
						throw new UserNotCreatedException();
				}
			}
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
		try{
			Account acc = new Account(name, AESencrp.encrypt(pwd),true, false);
			DatabaseConnection.login(acc);
			return acc;
		} catch(Exception e){
			throw new AuthenticationError();
		}
	}

	public void logout(Account acc) {
		DatabaseConnection.logout(acc);
	}

	public Account login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError {
		// TODO Auto-generated method stub
		throw new AuthenticationError();
		//return null;
	}

}
