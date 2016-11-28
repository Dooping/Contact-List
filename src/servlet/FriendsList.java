package servlet;

import java.io.IOException;
import java.util.List;

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
import contact_list.ContactList;
import database.DatabaseConnection;
import exceptions.AuthenticationError;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/FriendsList")
public class FriendsList extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String FRIEND_LIST = "friend_list";

	public FriendsList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			ContactList contactList = new ContactList();
			List<String> friendList = contactList.listFriends(acc.getUsername());
			int listSize = friendList.size();
			request.setAttribute("listSize", listSize);
			request.setAttribute("friends",friendList);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/friendslist.jsp");
			requestDispatcher.forward(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", FRIEND_LIST);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String sessionUsername = (String) request.getSession().getAttribute("username");
		try {
			String path="/Authenticator";
			if(!name.equals(sessionUsername)){
				int id = DatabaseConnection.getAccountId(name);
				HttpSession session = request.getSession(true);
				session.setAttribute("pageUsername",name);
				
				path += "/user/"+id; 
			}
			response.sendRedirect(path);
		} catch (UndefinedAccount e) {
			e.printStackTrace();
		}
	}
	
	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
