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
import authenticator.IAuthenticator;
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PermissionNotExistsException;
import exceptions.UndefinedAccount;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotCreatedException;
import exceptions.WrongConfirmationPasswordException;
import authenticator.Account;
import authenticator.Authenticator;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CREATEUSER = "create_user";
	private static final String OWNER = "root";
	private static final String RESOURCE = "user";
	private static final String OPERATION = "create";

	public CreateUser() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		IAuthenticator authenticator = new Authenticator();
		AccessControl acm = new AccessControl();
		try {
			Account acc = authenticator.login(request, response);
			try{
				List<Capability> capabilities = acm.getCapabilities(request);
				if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE, OPERATION)){
					Capability c = acm.makeCapability(OWNER, acc.getUsername(), RESOURCE, 
							OPERATION, System.currentTimeMillis()+3600000);
					capabilities.add(c);
				}
				HttpSession session = request.getSession(true);
				session.setAttribute("capabilities", capabilities);
				response.sendRedirect("/Authenticator/createuser.html");
			} catch (PermissionNotExistsException e) {
				RedirectError(request, response, "Username " + acc.getUsername() + " can't create users!");
			}
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CREATEUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
			e.printStackTrace();
		}  
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String aname = request.getParameter("username");
		String apwd1 = request.getParameter("password");
		String apwd2 = request.getParameter("password_confirmation");

		IAuthenticator authenticator = new Authenticator();
		AccessControl acm = new AccessControl();

		try {
			Account acc = authenticator.login(request, response);
			try{
				List<Capability> capabilities = acm.getCapabilities(request);
				if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE, OPERATION)){
					Capability c = acm.makeCapability(OWNER, acc.getUsername(), RESOURCE, 
							OPERATION, System.currentTimeMillis()+3600000);
					capabilities.add(c);
				}
				HttpSession session = request.getSession(true);
				session.setAttribute("capabilities", capabilities);
				authenticator.create_account(aname, apwd1, apwd2);
				RedirectSuccess(request, response, "User Created Successfully!");
			} catch (PermissionNotExistsException e) {
				RedirectError(request, response, "Username " + acc.getUsername() + " can't create users!");
			}
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (UserAlreadyExistsException e) {
			RedirectError(request, response, "User already exists");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (UserNotCreatedException e) {
			RedirectError(request, response, "User not created");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", CREATEUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		} 
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
