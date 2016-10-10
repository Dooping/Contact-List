package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import authenticator.Account;
import authenticator.Authenticator;
import authenticator.IAuthenticator;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PasswordNotChangedException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CHANGEPASSWORD = "change_password";

	public ChangePassword() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			authenticator.login(request, response);
			response.sendRedirect("/Authenticator/changepassword.html");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CHANGEPASSWORD);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pwd1 = request.getParameter("password");
		String pwd2 = request.getParameter("password_confirmation");
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(Login.USER);

		IAuthenticator authenticator = new Authenticator();

		try {
			authenticator.login(request, response);
			authenticator.change_pwd(name, pwd1, pwd2);
			Account acc = authenticator.get_account(name);
			session.setAttribute(Login.USER, acc.getUsername());
			session.setAttribute(Login.PWD, acc.getPassword());
			response.sendRedirect("/Authenticator/home.html");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (PasswordNotChangedException e) {
			RedirectError(request, response, "Password not changed");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CHANGEPASSWORD);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		}

	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
