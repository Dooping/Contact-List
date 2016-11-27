package servlet;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.TimeUtil;

import contact_list.ContactDetailed;
import contact_list.ContactList;
import exceptions.UndefinedAccount;


@WebServlet(urlPatterns = {"", "/user/*"})
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Home() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 String sessionUsername = (String)request.getSession().getAttribute("username");
		 String welcomeMessage = "";
		 if(sessionUsername != null && request.getRequestURI().equals("/Authenticator/"))
			 welcomeMessage = "Hello, " + sessionUsername;
		 request.setAttribute("welcomeMessage", welcomeMessage);
		 
		 
		 String[] pathInfo = request.getPathInfo().split("/");
	     String id = "";
	     
	     if(pathInfo.length != 0){
	    	 id = pathInfo[1];
	     }
		
	     // Check permissions ... 
	    
	    if(sessionUsername!= null){
		    ContactList clist = new ContactList();
		    ContactDetailed cd;
			try {
				cd = clist.getContactDetails(sessionUsername);
				request.setAttribute("name",sessionUsername);
				request.setAttribute("age",clist.getAge(cd.getBirthdate().getTime()));
				request.setAttribute("sex",cd.getSex());
				request.setAttribute("work",cd.getWork());
				request.setAttribute("birth",cd.getBirthdate());
				request.setAttribute("lives",cd.getLocation());
				request.setAttribute("from",cd.getOrigin());
				request.setAttribute("email",cd.getEmail());
				request.setAttribute("phonenumber",cd.getPhone());
			} catch (UndefinedAccount e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.jsp");
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
