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
import exceptions.AuthenticationError;
import exceptions.EmptyFieldException;
import exceptions.PasswordNotChangedException;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet(urlPatterns = {"", "/user/*"})
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Home() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String[] pathInfo = request.getPathInfo().split("/");
	     String id = "";
	     
	     if(pathInfo.length != 0){
	    	 id = pathInfo[1];
	     }
		
	     // Check permissions ... 
	     
		request.setAttribute("name","Andre");
		request.setAttribute("age","20");
		request.setAttribute("sex","Andre");
		request.setAttribute("work","Andre");
		request.setAttribute("birth","Andre");
		request.setAttribute("lives","Andre");
		request.setAttribute("from","Andre");
		request.setAttribute("email","Andre");
		request.setAttribute("phonenumber","Andre");
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
