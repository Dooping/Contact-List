package servlet;

import java.io.IOException;
import java.util.List;

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
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/FriendRequests")
public class FriendRequests extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String FRIEND_REQUESTS = "friends_requests";

	public FriendRequests() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			ContactList contactList = new ContactList();
			List<String> list = contactList.listFriendRequests(acc.getUsername());
			int listSize = list.size();
			System.out.println("Friends Requests Size: " + listSize);
			request.setAttribute("listSize", listSize);
			request.setAttribute("list",list);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/friendrequests.jsp");
			requestDispatcher.forward(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", FRIEND_REQUESTS);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
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
