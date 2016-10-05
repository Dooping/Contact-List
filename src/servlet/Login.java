package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Exceptions.AuthenticationError;
import authenticator.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  private static final String USER = "username";
	  private static final String PWD = "password";
	  
	  public Login() {
	        super();
	    }

	public void doGet(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException  {
		try {
			String aname = request.getParameter("username");
			String apwd = request.getParameter("password");
			HttpSession session = request.getSession(true);
			Authenticator authenticator = new Authenticator();
			IAccount authUser = authenticator.login(aname, apwd);
			session.setAttribute(USER, authUser.getUsername());
			session.setAttribute(PWD, authUser.getPassword());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.html");
	        requestDispatcher.forward(request, response);
		} catch (AuthenticationError e) {
			// handle authentication error
		}
	}
	public void doPost(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
