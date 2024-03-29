package servlet;

import java.io.IOException;

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
import exceptions.RequestSelfException;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.InvalidRequestException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/AddFriend")
public class AddFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;
	  public static final String ADD_FRIEND = "add_friend";

	public AddFriend() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			IAuthenticator authenticator = new Authenticator();
			Account authUser = authenticator.login(request,response);
			String friend = request.getParameter("friendName");
			ContactList contactList = new ContactList();
			contactList.newFriendRequest(authUser.getUsername(), friend);
			RedirectSuccess(request, response, "Friend Request Sent!");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", ADD_FRIEND);
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
			RedirectError(request, response, "Can't request friendship from yourself!");
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
