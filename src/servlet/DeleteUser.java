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
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccount;
import exceptions.UserIsLoggedInException;
import exceptions.UserNotLockedException;
import exceptions.WrongConfirmationPasswordException;
import exceptions.UserNotExistsException;


@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  public static final String DELETEUSER = "delete_user";
       
    public DeleteUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		IAuthenticator authenticator = new Authenticator();
		
		try {
			Account acc = authenticator.login(request, response);
			if (acc.getUsername().equals("root"))
				response.sendRedirect("/Authenticator/deleteuser.html");
			else
				RedirectError(request, response, "Username " + acc.getUsername() + " can't delete users!");
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
		
		try {
			Account acc = authenticator.login(request, response);
			if (acc.getUsername().equals("root")){
				if(aname.equals("root")){
					RedirectError(request, response, "Cant remove root");
				} else {
				authenticator.delete_account(aname);
				RedirectSuccess(request, response, "User Deleted Successfully!");
				}
			}
			else
				RedirectError(request, response, "Username " + acc.getUsername() + " can't delete users!");
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
