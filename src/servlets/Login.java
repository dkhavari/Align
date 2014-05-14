package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.idField;
import static classes.Constants.mongoClient;
import static classes.Constants.passwordField;
import static classes.Constants.userCollection;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /*
     * Constructor: Login
     * ------------------
     * Invokes the parent constructor.
     */
    public Login() { super(); }

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Logs the user in and stores info in the session.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the ServletContext, MongoClient and database.
		ServletContext context = request.getSession().getServletContext();
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection collection = db.getCollection(userCollection);

		// Get the user's information.
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Check with the database to see if the login is valid.
		QueryBuilder userQuery = new QueryBuilder();
		userQuery.put(idField).is(username);
		DBObject check = collection.findOne(userQuery.get());
		if (check == null) {
			request.setAttribute("message", "Failed login.");
			request.getRequestDispatcher("login.jsp")
					.forward(request, response);
			return;
		} else {
			if (!(check.get(passwordField).equals(password))) {
				request.setAttribute("message", "Failed login.");
				request.getRequestDispatcher("login.jsp").forward(request,
						response);
				return;
			}
		}

		// If the user account exists and the password is right, log them in.
		HttpSession session = request.getSession(true);
		session.setAttribute(idField, username);
		response.sendRedirect("home.jsp");
	}
	
	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Logs the user out.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
	}

}
