package servlets;

import static classes.Constants.*;

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
 * Servlet implementation class DailyCompletion
 */
@WebServlet("/DailyCompletion")
public class DailyCompletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DailyCompletion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/*
	 * HTTP Method: GET
	 * ----------------
	 * Creates a new daily completion habit for the user,
	 * and stamps it with their unique username.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		
		// Get the habits collection.
		DBCollection habitsCollection = db.getCollection(completionCollection);
		
		// Get the name of the completion habit the user wants to add.
		String habit = request.getParameter("habit").toString();

		// Get the number of days the user wants for this habit.
		int days = Integer.parseInt(request.getParameter("days"));
		
		/* Create the new habit object. */
		DBObject newHabit = new BasicDBObject(idField, username);
		newHabit.put(habitField, habit);
		newHabit.put("current", 0);
		newHabit.put("best", 0);
		newHabit.put(lengthField, days);
		
		// Push the new habit object to the collection.
		habitsCollection.insert(newHabit);
		
		request.getRequestDispatcher("habits.jsp").forward(request, response);
		
	}

	/*
	 * HTTP Method: POST
	 * -----------------
	 * Updates the habit with an increment or a current streak reset.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

		// Get the habit collection.
		DBCollection habits = db.getCollection(completionCollection);
		
		// Check to see if the habit was completed or not.
		String completion = request.getParameter("completion");

		// Get the name of the habit.
		String habit = request.getParameter("habit");
		
		// Find that habit within the database.
		QueryBuilder habitQuery = new QueryBuilder();
		habitQuery.put(idField).is(username);
		habitQuery.put(habitField).is(habit);


		// If null, zero it. If non-null, increment!
		if (completion == null) {
			
			// Straight up just set the current habit to zero days.
			habits.update(habitQuery.get(), new BasicDBObject("$set", new BasicDBObject("current", 0)));
			
		} else {
			
			// Get the current habit object to extract the numbers.
			DBObject obj = habits.findOne(habitQuery.get());
			int current =  Math.round(Float.parseFloat(obj.get("current").toString()));
			int best = Math.round(Float.parseFloat(obj.get("best").toString()));
			
			// Increment the current streak.
			current++;
			habits.update(habitQuery.get(), new BasicDBObject("$set", new BasicDBObject("current", current)));
			
			// Increment the best streak if currentStreak > bestStreak.
			if (current > best) {
				habits.update(habitQuery.get(), new BasicDBObject("$set", new BasicDBObject("best", current)));
			}
			
		}
		
		request.getRequestDispatcher("habits.jsp").forward(request, response);

	}

}
