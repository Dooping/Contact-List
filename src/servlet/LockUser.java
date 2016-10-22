package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.Authenticator;
import authenticator.IAuthenticator;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.UserNotExistsException;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/LockUser")
public class LockUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String LOCKUSER = "lockuser";

	public LockUser() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			authenticator.login(request, response);
			response.sendRedirect("/Authenticator/lockuser.html");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", LOCKUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
		IAuthenticator authenticator = new Authenticator();
		
			try {
				authenticator.login(request,response);
				authenticator.lock_user(username);
				response.sendRedirect("/Authenticator/home.html");
			} catch (AuthenticationError e) {
				request.getSession().setAttribute("origin", LOCKUSER);
				response.sendRedirect("/Authenticator/login.html");
			} catch (UndefinedAccount e) {
				RedirectError(request, response, "Undefined Account");
			} catch (WrongConfirmationPasswordException e) {
				RedirectError(request, response, "Wrong Password");
			}catch (EmptyFieldException e) {
				RedirectError(request, response, "You need to fill all the fields");
			} catch (UserNotExistsException e) {
				RedirectError(request, response, "User not exists");
			} 
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
