package servlets;

import static classes.Constants.*;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor: CreateUser
	 * -----------------------
	 * Simply invokes the parent constructor.
	 */
	public CreateUser() {
		super();
	}

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Does nothing currently.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {}

	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Creates a new user or returns a failed status.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("We're in the POST of CreateUser.java.");

		// Grab the ServletContext, MongoClient and database.
		ServletContext context = request.getSession().getServletContext();
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection collection = db.getCollection(userCollection);
		
		// Get the user's information from the creation request.
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String password = request.getParameter("password");
		
		// Check to see if the username is already taken.
		QueryBuilder userQuery = new QueryBuilder();
		userQuery.put(idField).is(username);
		DBObject check = collection.findOne(userQuery.get());
		if(check != null) {
			request.setAttribute("message", "This username is already taken. Please choose another.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Create a new user account.
		BasicDBObject newUser = new BasicDBObject();
		newUser.put(idField, username);
		newUser.put(nameField, realname);
		newUser.put(passwordField, password);
		collection.insert(newUser);
		
		// Send back a message notifying the user that creation was successful.
		request.setAttribute("message", "Success! Please use your new account to log in.");
		request.getRequestDispatcher("login.jsp").forward(request, response);		

	}

}
