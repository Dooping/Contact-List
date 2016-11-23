package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.Account;


@WebServlet("/ContactsList")
public class ContactsList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ContactsList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Account> list = new ArrayList<Account>();
		Account a1 = new Account("alberto", "a",false,false);
		Account a2 = new Account("albertina", "a",false,false);
		list.add(a1);
		list.add(a2);
		
		request.setAttribute("list",list);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/contactslist.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 doGet(request, response);
	}

}
