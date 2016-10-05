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

import authenticator.*;
import exceptions.AuthenticationError;


@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Logout() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			IAuthenticator authenticator = new Authenticator();
			Account authUser = authenticator.login(request,response);
			authenticator.logout(authUser);
			HttpSession session = request.getSession(false);
			if(session != null) session.invalidate();
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.html");
			requestDispatcher.forward(request, response);
		} catch (AuthenticationError e) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.html");
			requestDispatcher.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
