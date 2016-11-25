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
import contact_list.ContactList;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.InvalidRequestException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/FriendsRequests")
public class FriendsRequests extends HttpServlet {

	private static final long serialVersionUID = 1L;
	 

	public FriendsRequests() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("message", "Friends Requests");
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/friendsrequests.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 doGet(request, response);
	}
	
	

}
