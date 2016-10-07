package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSeparatorUI;

import authenticator.IAuthenticator;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotCreatedException;
import exceptions.WrongConfirmationPasswordException;
import authenticator.Authenticator;
import authenticator.IAccount;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateUser() {
		super(); 
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String aname = request.getParameter("username");
		String apwd1 = request.getParameter("password");
		String apwd2 = request.getParameter("password_confirmation");

		IAuthenticator authenticator = new Authenticator();

		try {
			authenticator.create_account(aname, apwd1, apwd2);
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (UserAlreadyExistsException e) {
			RedirectError(request, response, "User already exists");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (UserNotCreatedException e) {
			RedirectError(request, response, "User not created");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		} 
		
		// Não sei se é o correcto, mas talvez não 
		response.sendRedirect("/Authenticator/home.html");
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/createusererrormessage.jsp");
		requestDispatcher.forward(request, response);
	}


}
