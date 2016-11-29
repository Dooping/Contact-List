package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import exceptions.InvalidRequestException;
import exceptions.WrongConfirmationPasswordException;

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
			
			String profilePermission = contactList.checkInformationPermission(acc.getUsername(), "profile"+acc.getUsername());
			String contactPermission = contactList.checkInformationPermission(acc.getUsername(), "contacts"+acc.getUsername());
			request.setAttribute("selectedProfilePermission", profilePermission);
			request.setAttribute("selectedContactPermission", contactPermission);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/settings.jsp");
			requestDispatcher.forward(request, response);
		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", SETTINGS);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = (String) request.getSession().getAttribute("username");
		
		ContactList contactList = new ContactList();
		
		int sexSelectedItem=-1;
		if(request.getParameter("sexSelectedOption")!=null)
			sexSelectedItem= Integer.parseInt(request.getParameter("sexSelectedOption"));
		
		char sex = contactList.getSelectedDropdown(sexSelectedItem, "-", "F", "M").charAt(0);
		
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
		
		String internal_statement = request.getParameter("internal_statement_text");
		String external_statement = request.getParameter("external_statement_text");
		
		ContactDetailed cd = new ContactDetailed(name, sex, work, sqlBirthdate, location, origin, email, phone, internal_statement, external_statement);
	
		contactList.setContactDetails(cd);
		
		int selectedProfilePermission=-1;
		if(request.getParameter("profile")!=null)
			selectedProfilePermission= Integer.parseInt(request.getParameter("profile"));
		
		String profilePermission = contactList.getSelectedDropdown(selectedProfilePermission,"private", "internal", "public");
		
		int selectedContactsPermission=-1;
		if(request.getParameter("contacts")!=null)
			selectedContactsPermission= Integer.parseInt(request.getParameter("contacts"));
		
		String contactPermission = contactList.getSelectedDropdown(selectedContactsPermission,"private", "internal", "public");
		
		
		try {
			contactList.setInformationPermission(profilePermission, name, "profile"+name);
			contactList.setInformationPermission(contactPermission, name, "contacts"+name);
			contactList.resetPermissions(name);
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("/Authenticator");
	}
	
	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
