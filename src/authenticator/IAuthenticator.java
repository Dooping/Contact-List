package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;

public interface IAuthenticator {
	
	void create_account(String name, String pwd1, String pwd2) throws EmptyFieldException;
	void delete_account(String name);
	Account get_account(String name);
	void change_pwd(String name, String pwd1, String pwd2);
	Account login(String name, String pwd) throws AuthenticationError;
	void logout(Account acc);
	Account login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError;
}
