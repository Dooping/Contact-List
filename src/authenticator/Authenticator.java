package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseConnection;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotLockedException;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.UserNotCreatedException;
import exceptions.UserNotDeletedException;
import exceptions.UserNotExistsException;
import exceptions.WrongConfirmationPasswordException;
import servlet.Login;

public class Authenticator implements IAuthenticator{

	public void create_account(String name, String pwd1, String pwd2) throws EmptyFieldException, UserAlreadyExistsException, WrongConfirmationPasswordException,UserNotCreatedException, Exception {
		
		if(name.length() == 0 || pwd1.length() == 0 || pwd2.length() == 0 )
			throw new EmptyFieldException();
	
		try{
			DatabaseConnection.getAccount(name);
			throw new UserAlreadyExistsException();
		} catch(UndefinedAccount e) {
			if(!pwd1.equals(pwd2))
				throw new WrongConfirmationPasswordException();
			
			pwd1 = AESencrp.encrypt(pwd1);
						
			if(!DatabaseConnection.createUser(name, pwd1))
				throw new UserNotCreatedException();
		}
			
		
		
	}

	public void delete_account(String name) throws EmptyFieldException, UserNotExistsException, UserNotLockedException ,Exception{
		/*
	 	• deleted account should be deleted from the associated persistent storage
		• preconditions
		• the account cannot be logged in
		• the account must be locked (so no one will authenticate on the way) 
		 */
		
		if(name.length()== 0)
			throw new EmptyFieldException();
		
		IAccount account = DatabaseConnection.getAccount(name);
		
		
		if(account==null)
			throw new UserNotExistsException();
		
		
		if(!account.isLocked())
			throw new UserNotLockedException();
		
		// FALTA VERIFICAR SE ESTA LOGADO OU NAO 
		
		if(!DatabaseConnection.deleteUser(name))
			throw new UserNotDeletedException();
		
	}
	public Account get_account(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void change_pwd(String name, String pwd1, String pwd2) {
		// TODO Auto-generated method stub

	}

	public Account login(String name, String pwd) throws AuthenticationError, UndefinedAccount, WrongConfirmationPasswordException {
		try{
			Account acc = DatabaseConnection.getAccount(name);
			if(!AESencrp.encrypt(pwd).equals(acc.getPassword()))
				throw new WrongConfirmationPasswordException();
			DatabaseConnection.login(acc);
			return acc;
		} catch(AuthenticationError e){
			throw new AuthenticationError();
		} catch (UndefinedAccount e) {
			throw new UndefinedAccount();
		} catch (WrongConfirmationPasswordException e) {
			throw new WrongConfirmationPasswordException();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void logout(Account acc) {
		DatabaseConnection.logout(acc);
	}

	public Account login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, UndefinedAccount, WrongConfirmationPasswordException {
		HttpSession session = req.getSession(false);
		if(session == null)
			throw new AuthenticationError();
		
		String username = (String)session.getAttribute(Login.USER);
		String pwdhash = (String)session.getAttribute(Login.PWD);
		String pwd = null;
		try {
			pwd = AESencrp.decrypt(pwdhash);
		} catch(Exception e){
			e.printStackTrace();
		}
		return login(username, pwd);
	}

}
