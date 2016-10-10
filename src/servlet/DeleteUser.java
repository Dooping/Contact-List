package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.Authenticator;
import authenticator.IAuthenticator;
import exceptions.EmptyFieldException;
import exceptions.UserNotLockedException;
import exceptions.UserNotExistsException;


@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeleteUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String aname = request.getParameter("username");
		
		IAuthenticator authenticator = new Authenticator();
		
		try {
			authenticator.delete_account(aname);
			response.sendRedirect("/Authenticator/home.html");
		} catch (EmptyFieldException e) {
			RedirectError(request, response, "You need to fill all the fields");
		} catch (UserNotExistsException e) {
			RedirectError(request, response, "User not exists");
		} catch(UserNotLockedException e) {
			RedirectError(request, response, "User is not locked");
		} catch (Exception e) {
			RedirectError(request, response, "Exception error");
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
