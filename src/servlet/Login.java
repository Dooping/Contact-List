package servlet;

import javax.servlet.http.*;

import Exceptions.AuthenticationError;
import authenticator.*;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  private static final String USER = "username";
	  private static final String PWD = "password";

	public void doGet(
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String aname = request.getParameter("username");
			String apwd = request.getParameter("password");
			HttpSession session = request.getSession(true);
			Authenticator authenticator = new Authenticator();
			IAccount authUser = authenticator.login(aname, apwd);
			session.setAttribute(USER, authUser.getUsername());
			session.setAttribute(PWD, authUser.getPassword());
			// continue with authenticated user (redirect?)
		} catch (AuthenticationError e) {
			// handle authentication error
		}
	}

}
