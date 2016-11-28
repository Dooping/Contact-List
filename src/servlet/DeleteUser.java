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
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PermissionNotExistsException;
import exceptions.UndefinedAccount;
import exceptions.UserIsLoggedInException;
import exceptions.UserNotLockedException;
import exceptions.WrongConfirmationPasswordException;
import exceptions.UserNotExistsException;


@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String DELETEUSER = "delete_user";
	private static final String OWNER = "root";
	private static final String RESOURCE = "user";
	private static final String OPERATION = "delete";
       
    public DeleteUser() {
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
				response.sendRedirect("/Authenticator/deleteuser.html");
			} catch (PermissionNotExistsException e) {
				RedirectError(request, response, "Username " + acc.getUsername() + " can't delete users!");
			}
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", DELETEUSER);
			response.sendRedirect("/Authenticator/login.html");
		}catch (Exception e) {
			RedirectError(request, response, "Exception error");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String aname = request.getParameter("username");
		
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
				if(aname.equals(acc.getUsername())){
					RedirectError(request, response, "Cant remove yourself");
				} else {
					authenticator.delete_account(aname);
					RedirectSuccess(request, response, "User Deleted Successfully!");
				}
			} catch (PermissionNotExistsException e) {
				RedirectError(request, response, "Username " + acc.getUsername() + " can't delete users!");
			}
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (UserNotExistsException e) {
			RedirectError(request, response, "User not exists");
		} catch(UserNotLockedException e) {
			RedirectError(request, response, "User is not locked");
		} catch(UserIsLoggedInException e){
			RedirectError(request, response, "User is logged in");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", DELETEUSER);
			response.sendRedirect("/Authenticator/login.html");
		} catch (UndefinedAccount e) {
			RedirectError(request, response, "Undefined Account");
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Wrong Password");
		} catch (Exception e) {
			RedirectError(request, response, "Exception error");
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
