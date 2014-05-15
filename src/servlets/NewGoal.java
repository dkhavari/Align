package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.idField;
import static classes.Constants.mongoClient;
import static classes.Constants.userCollection;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class NewGoal
 */
@WebServlet("/NewGoal")
public class NewGoal extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewGoal() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Redirect if there's no valid session.
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		// Check to make sure there's a valid username.
		if (null == session.getAttribute(idField)) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		// Get the username and start working with the database.
		String username = session.getAttribute(idField).toString();

		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create or fetch a database.
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection collection = db.getCollection(userCollection);

		// Check with the database to see if the login is valid.
		QueryBuilder userQuery = null;
		try {
			userQuery = new QueryBuilder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userQuery.put(idField).is(username);
		DBObject userObj = collection.findOne(userQuery.get());
		if (userObj == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		// Get the goal the user wants to add.
		String goal = request.getParameter("goal").toString();
		
		// Create the search query.
		DBObject searchQuery = new BasicDBObject(idField, username);
		
		// For now, naively add any goal to the user. Set limits / add checks later.
		
		// Add the goal to the user document.
		DBObject pushQuery = new BasicDBObject("$push", new BasicDBObject("goals", goal));
		
		// Call the update command.
		collection.update(searchQuery, pushQuery);
				
		request.setAttribute("message", "New Goal: " + goal);
		request.getRequestDispatcher("goals.jsp").forward(request, response);


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
