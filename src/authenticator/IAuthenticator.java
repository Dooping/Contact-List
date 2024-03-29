package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.UserAlreadyExistsException;
import exceptions.UserNotLockedException;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PasswordNotChangedException;
import exceptions.UndefinedAccount;
import exceptions.UserNotCreatedException;
import exceptions.UserNotExistsException;
import exceptions.WrongConfirmationPasswordException;

public interface IAuthenticator {
	
	void create_account(String name, String pwd1, String pwd2) throws EmptyFieldException, UserAlreadyExistsException, WrongConfirmationPasswordException,UserNotCreatedException, Exception;
	void delete_account(String name) throws EmptyFieldException, UserNotExistsException, UserNotLockedException, Exception;
	Account get_account(String name) throws UndefinedAccount;
	void change_pwd(String name, String pwd1, String pwd2) throws EmptyFieldException, WrongConfirmationPasswordException, PasswordNotChangedException;
	void lock_user(String name) throws EmptyFieldException, UserNotExistsException;
	Account login(String name, String pwd, boolean hashed) throws AuthenticationError, UndefinedAccount, WrongConfirmationPasswordException;
	void logout(Account acc);
	Account login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, UndefinedAccount, WrongConfirmationPasswordException;
}
