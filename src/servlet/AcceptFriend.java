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
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.InvalidRequestException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;

@WebServlet("/AcceptFriend")
public class AcceptFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ACCEPT_FRIEND = "accept_friend";

	public AcceptFriend() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			IAuthenticator authenticator = new Authenticator();
			Account authUser = authenticator.login(request,response);
			String requester = (String)request.getAttribute("requesterName");
			ContactList contactList = new ContactList();
			contactList.acceptFriend(requester, authUser.getUsername());
			response.sendRedirect("/Authenticator/FriendRequests");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", ACCEPT_FRIEND);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Wrong Password");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "Invalid Parameters");
		} catch (InvalidRequestException e) {
			RedirectError(request, response, "Invalid Request");
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

}
