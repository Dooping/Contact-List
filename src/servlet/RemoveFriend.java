package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.Account;
import authenticator.Authenticator;
import authenticator.IAuthenticator;
import contact_list.ContactList;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.InvalidRequestException;
import exceptions.RequestSelfException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/RemoveFriend")
public class RemoveFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;
	  public static final String REMOVE_FRIEND = "remove_friend";

	public RemoveFriend() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			IAuthenticator authenticator = new Authenticator();
			Account authUser = authenticator.login(request,response);
			String friend = request.getParameter("friendName");
			ContactList contactList = new ContactList();
			contactList.deleteFriend(authUser.getUsername(), friend);
			RedirectSuccess(request, response, "You and "+friend+" are no longer friends!");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", REMOVE_FRIEND);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Wrong Password");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "Invalid Parameters");
		} catch (InvalidRequestException e) {
			RedirectError(request, response, "Invalid Request");
		} catch (RequestSelfException e) {
			RedirectError(request, response, "Can't delete yourself as friends");
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
