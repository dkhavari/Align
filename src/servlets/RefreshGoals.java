package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.idField;
import static classes.Constants.mongoClient;
import static classes.Constants.userCollection;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.types.BasicBSONList;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class RefreshGoals
 */
@WebServlet("/RefreshGoals")
public class RefreshGoals extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RefreshGoals() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Gets all of the data from MongoDB for the goals page.
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
		QueryBuilder userQuery = new QueryBuilder();
		userQuery.put(idField).is(username);
		DBObject userObj = collection.findOne(userQuery.get());
		if (userObj == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		// Get the list of goals.
		BasicBSONList list = (BasicBSONList) userObj.get("goals");
		if (list == null) {
			response.sendRedirect("goals.jsp");
			return;
		}

		// Return the goals list.
		ArrayList<String> goals = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			// Add to the lists.
			String goalTitle = list.get(i).toString();
			System.out.println("GOAL: " + goalTitle);
			goals.add(goalTitle);
		}
				
		// Finish things up.
		request.setAttribute("goals", goals);
		request.setAttribute("goalcount", goals.size());
		request.setAttribute("isUpdated", "");
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
