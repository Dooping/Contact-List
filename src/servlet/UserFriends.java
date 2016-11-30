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

import accesscontrol.AccessControl;
import accesscontrol.Capability;
import authenticator.Account;
import authenticator.Authenticator;
import authenticator.IAuthenticator;
import contact_list.ContactList;
import database.DatabaseConnection;
import exceptions.AuthenticationError;
import exceptions.LockedAccount;
import exceptions.AccessControlError;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/UserFriends")
public class UserFriends extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String USER_FRIENDS = "user_friends";
	private static final String RESOURCE = "friends";
	private static final String OPERATION = "read";

	public UserFriends() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			ContactList contactList = new ContactList();
			
			String userPage="";
			HttpSession session = request.getSession(true);
			String pathInfo = (String)session.getAttribute("path");
			
			if(pathInfo.length() > 1){
				userPage = (String)session.getAttribute("pageUsername");
			} else 
				userPage = acc.getUsername();
			
			if(contactList.isLocked(userPage))
				throw new LockedAccount();
			
			AccessControl acm = new AccessControl();
			try{
				List<Capability> capabilities = acm.getCapabilities(request);
				if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE, OPERATION)){
					Capability c = acm.makeCapability(userPage, acc.getUsername(), RESOURCE, 
							OPERATION, System.currentTimeMillis()+3600000);
					capabilities.add(c);
				}
				session.setAttribute("capabilities", capabilities);
				
				List<String> friendList = contactList.listFriends(userPage);
				int listSize = friendList.size();
				request.setAttribute("listSize", listSize);
				request.setAttribute("friends",friendList);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/userfriends.jsp");
				requestDispatcher.forward(request, response);
			} catch (AccessControlError e) {
				RedirectError(request, response, "You do not have permission to see this user's friend list!");
			}
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", USER_FRIENDS);
			response.sendRedirect("/Authenticator/login.html");
		} catch (LockedAccount e) {
			RedirectError(request, response, "This user does not exist");
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
