package authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAuthenticator {
	
	void create_account(String name, String pwd1, String pwd2);
	void delete_account(String name);
	Account get_account(String name);
	void change_pwd(String name, String pwd1, String pwd2);
	Account login(String name, String pwd);
	void logout(Account acc);
	Account login(HttpServletRequest req, HttpServletResponse resp);
}
