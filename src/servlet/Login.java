package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import authenticator.*;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  public static final String USER = "username";
	  public static final String PWD = "password";
	  
	  public Login() {
	        super();
	    }

	public void doGet(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException  {
		try {
			String aname = request.getParameter("username");
			String apwd = request.getParameter("password");
			if (aname.equals("") || apwd.equals(""))
				throw new EmptyFieldException();
			HttpSession session = request.getSession(true);
			Authenticator authenticator = new Authenticator();
			IAccount authUser = authenticator.login(aname, apwd);
			session.setAttribute(USER, authUser.getUsername());
			session.setAttribute(PWD, authUser.getPassword());
			String origin = (String)request.getSession().getAttribute("origin");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.jsp");
			if (origin != null)
				switch (origin){
				case Logout.LOGOUT: response.sendRedirect("/Authenticator/Logout");
				break;
				case CreateUser.CREATEUSER: response.sendRedirect("/Authenticator/CreateUser");
				break;
				case DeleteUser.DELETEUSER: response.sendRedirect("/Authenticator/DeleteUser");
				break;
				case ChangePassword.CHANGEPASSWORD: response.sendRedirect("/Authenticator/ChangePassword");
				break;
				case LockUser.LOCKUSER: response.sendRedirect("/Authenticator/LockUser");
				break;
				case ContactsList.CONTACT_LIST: response.sendRedirect("/Authenticator/ContactsList");
				break;
				case FriendsList.FRIEND_LIST: response.sendRedirect("/Authenticator/FriendList");
				break;
				}
			else
				System.out.println("no origin");
	        //requestDispatcher.forward(request, response);
		} catch (AuthenticationError e) {
			RedirectError(request, response, "Authentication Error");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Account does not exist");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Wrong Password");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		}
	}
	public void doPost(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
