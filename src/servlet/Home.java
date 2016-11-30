package servlet;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.TimeUtil;

import accesscontrol.AccessControl;
import accesscontrol.Capability;
import authenticator.Account;
import authenticator.Authenticator;
import authenticator.IAuthenticator;
import contact_list.ContactDetailed;
import contact_list.ContactList;
import exceptions.AuthenticationError;
import exceptions.AccessControlError;
import exceptions.UndefinedAccount;
import exceptions.WrongConfirmationPasswordException;


@WebServlet(urlPatterns = {"", "/user/*"})
public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String HOME = "home";
	private static final String RESOURCE1 = "profile";
	private static final String RESOURCE2 = "contacts";
	private static final String RESOURCE3 = "internal";
	private static final String OPERATION = "read";

	public Home() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		IAuthenticator authenticator = new Authenticator();
		try {
			Account acc = authenticator.login(request, response);
			String welcomeMessage = "";
			if(acc.getUsername() != null && request.getRequestURI().equals("/Authenticator/"))
				welcomeMessage = "Hello, " + acc.getUsername();
			request.setAttribute("welcomeMessage", welcomeMessage);

			String[] pathInfo = request.getPathInfo().split("/");
			String user="";
			
			String displayUserFriendButton = "";
			if(pathInfo.length != 0){
				HttpSession session = request.getSession(true);
				user = (String)session.getAttribute("pageUsername");
				session.setAttribute("pageUsername", user);
			} else {
				user = acc.getUsername();
				displayUserFriendButton = "none";
			}
			
			request.setAttribute("displayUserFriendButton", displayUserFriendButton);

			ContactList clist = new ContactList();
			ContactDetailed cd;
			try {
				cd = clist.getContactDetails(user);
				request.setAttribute("name",user);
				boolean profile = false;
				AccessControl acm = new AccessControl();

				String permission = clist.checkInformationPermission(user, RESOURCE1+user);
				if(!permission.equals("public"))
					try{
						List<Capability> capabilities = acm.getCapabilities(request);
						if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE1+user, OPERATION)){
							Capability c = acm.makeCapability(user, acc.getUsername(), RESOURCE1+user, 
									OPERATION, System.currentTimeMillis()+3600000);
							capabilities.add(c);
						}
						HttpSession session = request.getSession(true);
						session.setAttribute("capabilities", capabilities);
						profile = true;
					} catch (AccessControlError e) {
					}
				else
					profile = true;
	
				String displayProfile="";
				if(profile){
					if(cd.getBirthdate()!=null)
						request.setAttribute("age",clist.getAge(cd.getBirthdate().getTime()));
					request.setAttribute("sex",cd.getSex());
					request.setAttribute("work",cd.getWork());
					request.setAttribute("birth",cd.getBirthdate());
					request.setAttribute("lives",cd.getLocation());
					request.setAttribute("from",cd.getOrigin());
				} else 
					displayProfile = "none";
				
				request.setAttribute("displayProfile", displayProfile);
				
				clist.checkInformationPermission(user, RESOURCE2+user);
				boolean contacts = false;
				
				permission = clist.checkInformationPermission(user, RESOURCE2+user);
				if(!permission.equals("public"))
					try{
						List<Capability> capabilities = acm.getCapabilities(request);
						if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE2+user, OPERATION)){
							Capability c = acm.makeCapability(user, acc.getUsername(), RESOURCE2+user, 
									OPERATION, System.currentTimeMillis()+3600000);
							capabilities.add(c);
						}
						HttpSession session = request.getSession(true);
						session.setAttribute("capabilities", capabilities);
						contacts = true;
					} catch (AccessControlError e) {
					}
				else
					contacts = true;
				
				String displayContacts="";
				if(contacts){
					request.setAttribute("email",cd.getEmail());
					request.setAttribute("phonenumber",cd.getPhone());
				} else 
					displayContacts = "none";
				
				request.setAttribute("displayContacts", displayContacts);
				
				boolean internal = false;
				
				permission = clist.checkInformationPermission(user, RESOURCE3+user);
				if(!permission.equals("public"))
					try{
						List<Capability> capabilities = acm.getCapabilities(request);
						if(!acm.checkPermission(acc.getUsername(), capabilities, RESOURCE3+user, OPERATION)){
							Capability c = acm.makeCapability(user, acc.getUsername(), RESOURCE3+user, 
									OPERATION, System.currentTimeMillis()+3600000);
							capabilities.add(c);
						}
						HttpSession session = request.getSession(true);
						session.setAttribute("capabilities", capabilities);
						internal = true;
					} catch (AccessControlError e) {
					}
				else
					internal = true;
				if(internal)
					request.setAttribute("internal_statement", cd.getInternal_statement());
				
				request.setAttribute("external_statement", cd.getExternal_statement());
				String path = request.getRequestURI().substring(request.getContextPath().length());
				
				HttpSession session = request.getSession(true);
				session.setAttribute("path",path);
				
			} catch (UndefinedAccount e) {
				e.printStackTrace();
			}


			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.jsp");
			requestDispatcher.forward(request, response);


		} catch (WrongConfirmationPasswordException e) {
			RedirectError(request, response, "Password confirmation did not match with the password");
		} catch (AuthenticationError e) {
			request.getSession().setAttribute("origin", HOME);
			response.sendRedirect("/Authenticator/login.html");
		} catch (Exception e) {
			RedirectError(request, response, "Exception Error");
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request,response);
	}

	private void RedirectError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException{
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errormessage.jsp");
		requestDispatcher.forward(request, response);
	}

}
