package authenticator;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseConnection;
import exceptions.UserAlreadyExistsException;
import exceptions.UserIsLoggedInException;
import exceptions.UserNotLockedException;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PasswordNotChangedException;
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
			this.get_account(name);
			throw new UserAlreadyExistsException();
		} catch(UndefinedAccount e) {
			if(!pwd1.equals(pwd2))
				throw new WrongConfirmationPasswordException();
			
			pwd1 = AESencrp.encrypt(pwd1);
			
			Random rn = new Random();
						
			if(!DatabaseConnection.createUser(name, pwd1, rn.nextInt()))
				throw new UserNotCreatedException();
			DatabaseConnection.createUserDetails(name);
			DatabaseConnection.createUserResources(name);
		}	
		
	}

	public void delete_account(String name) throws EmptyFieldException, UserNotExistsException, UserNotLockedException ,Exception{
		
		if(name.length()== 0)
			throw new EmptyFieldException();
		
		try{
			IAccount account = this.get_account(name);
			if(!account.isLocked())
				throw new UserNotLockedException();
			
			if(account.isLoggedIn())
				throw new UserIsLoggedInException();
				
			if(!DatabaseConnection.deleteUser(name))
				throw new UserNotDeletedException();
			
		} catch (UndefinedAccount e){
			throw new UserNotExistsException();
		}
	}
	
	public void lock_user(String name) throws EmptyFieldException, UserNotExistsException {
		if (name.length() == 0)
			throw new EmptyFieldException();
		
		try{
			Account acc = this.get_account(name);
			if (!acc.isLocked())
				DatabaseConnection.lockUser(name);
			else
				DatabaseConnection.unlockUser(name);
				
		} catch(UndefinedAccount e){
			throw new UserNotExistsException();
		}
		
	}

	
	public Account get_account(String name) throws UndefinedAccount {
		return DatabaseConnection.getAccount(name);
	}

	public void change_pwd(String name, String pwd1, String pwd2) throws EmptyFieldException, WrongConfirmationPasswordException, PasswordNotChangedException {
		
		if(name.length() == 0 || pwd1.length() == 0 || pwd2.length() == 0)
			throw new EmptyFieldException();
		
		if (!pwd1.equals(pwd2))
			throw new WrongConfirmationPasswordException();
		
		String password = null;
		try {
			password = AESencrp.encrypt(pwd1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!DatabaseConnection.changePassword(name, password))
			 throw new PasswordNotChangedException();
	}
	
	

	public Account login(String name, String pwd) throws AuthenticationError, UndefinedAccount, WrongConfirmationPasswordException {
		try{
			Account acc = this.get_account(name);
			if(acc.isLocked())
				throw new AuthenticationError();
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
		if (username == null || pwdhash == null)
			throw new AuthenticationError();
		String pwd = null;
		try {
			pwd = AESencrp.decrypt(pwdhash);
		} catch(Exception e){
			e.printStackTrace();
		}
		return login(username, pwd);
	}

}
