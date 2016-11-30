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
import exceptions.AccessControlError;
import exceptions.AuthenticationError;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet("/ContactsList")
public class ContactsList extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final String CONTACT_LIST = "contact_list";
	private static final String OWNER = "root";
	private static final String RESOURCE = "user";
	private static final String OPERATION = "lock";

	public ContactsList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			ContactList contactList = new ContactList();
			AccessControl acm = new AccessControl();
			boolean seeLocked = false;
			try{
				List<Capability> capabilities = acm.getCapabilities(request);
				if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE, OPERATION)){
					Capability c = acm.makeCapability(OWNER, acc.getUsername(), RESOURCE, 
							OPERATION, System.currentTimeMillis()+3600000);
					capabilities.add(c);
				}
				HttpSession session = request.getSession(true);
				session.setAttribute("capabilities", capabilities);
				seeLocked = true;
			} catch (AccessControlError e) {
			}
			List<Account> list = contactList.listContacts(seeLocked);
			int listSize = list.size();
			request.setAttribute("listSize", listSize);
			request.setAttribute("list",list);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/contactslist.jsp");
			requestDispatcher.forward(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CONTACT_LIST);
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
				path += "/user/"+id; 
				HttpSession session = request.getSession(true);
				session.setAttribute("pageUsername",name);
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
