package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.IAuthenticator;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotCreatedException;
import exceptions.WrongConfirmationPasswordException;
import authenticator.Authenticator;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  public static final String CREATEUSER = "create_user";

	public CreateUser() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		IAuthenticator authenticator = new Authenticator();
		try {
			authenticator.login(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CREATEUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		} 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String aname = request.getParameter("username");
		String apwd1 = request.getParameter("password");
		String apwd2 = request.getParameter("password_confirmation");

		IAuthenticator authenticator = new Authenticator();

		try {
			authenticator.login(request, response);
			authenticator.create_account(aname, apwd1, apwd2);
			response.sendRedirect("/Authenticator/home.html");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (UserAlreadyExistsException e) {
			RedirectError(request, response, "User already exists");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (UserNotCreatedException e) {
			RedirectError(request, response, "User not created");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CREATEUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		} 
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}


}
