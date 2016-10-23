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

import authenticator.*;
import exceptions.AuthenticationError;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  public static final String LOGOUT = "logout";

	public Logout() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			IAuthenticator authenticator = new Authenticator();
			Account authUser = authenticator.login(request,response);
			authenticator.logout(authUser);
			HttpSession session = request.getSession(false);
			if(session != null) session.invalidate();
			RedirectSuccess(request, response, "Logout complete!");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", LOGOUT);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Wrong Password");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}
	
	private void RedirectSuccess(HttpServletRequest request, HttpServletResponse response, String successMessage) throws ServletException, IOException{
		request.setAttribute("successMessage", successMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/successmessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
