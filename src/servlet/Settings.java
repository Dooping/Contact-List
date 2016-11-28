package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import authenticator.Account;
import authenticator.Authenticator;
import authenticator.IAuthenticator;
import contact_list.ContactDetailed;
import contact_list.ContactList;
import exceptions.AuthenticationError;
import exceptions.WrongConfirmationPasswordException;

import java.text.DateFormat;
import java.text.ParseException;


@WebServlet("/Settings")
public class Settings extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String SETTINGS = "settings";

	public Settings() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			ContactList contactList = new ContactList();
			ContactDetailed contactDetails = contactList.getContactDetails(acc.getUsername());
			request.setAttribute("contactDetails",contactDetails);
			request.setAttribute("selectedSex", contactDetails.getSex());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/settings.jsp");
			requestDispatcher.forward(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", SETTINGS);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = (String) request.getSession().getAttribute("username");
		
		String selectedItem="";
		if(request.getParameter("sexSelectedOption")!=null)
		{
		   selectedItem= request.getParameter("sexSelectedOption");
		}
		
		char sex;
		switch(selectedItem){
		case "male":
			sex = 'M';
			break;
		case "female":
			sex = 'F';
			break;
		default:
			sex ='-';
			break;
		}
		
		
		String work = request.getParameter("work");
		
		Calendar mydate = new GregorianCalendar();
		String birth = request.getParameter("birth");
		java.util.Date birthDate = new java.util.Date();
		try {
			birthDate = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH).parse(birth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mydate.setTime(birthDate);
	    java.sql.Date sqlBirthdate = new java.sql.Date(birthDate.getTime());  
		
		String location = request.getParameter("livesin");
		String origin = request.getParameter("from");
		String email = request.getParameter("email");
		String phone = request.getParameter("phonenumber");
		
		ContactDetailed cd = new ContactDetailed(name, sex, work, sqlBirthdate, location, origin, email, phone, "", "");
	
		ContactList contactList = new ContactList();
		contactList.setContactDetails(cd);
		response.sendRedirect("/Authenticator");
	}
	
	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
