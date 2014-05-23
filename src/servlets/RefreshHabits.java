package servlets;

import static classes.Constants.*;

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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class RefreshHabits
 */
@WebServlet("/RefreshHabits")
public class RefreshHabits extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefreshHabits() {
        super();
        // TODO Auto-generated constructor stub
    }

	/*
	 * HTTP Method: GET
	 * ----------------
	 * Gets all of the data from MongoDB and sends it to the jsp,
	 * properly formatted.
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
		QueryBuilder userQuery = new QueryBuilder();
		userQuery.put(idField).is(username);
		DBObject userObj = collection.findOne(userQuery.get());
		if (userObj == null) {
			response.sendRedirect("login.jsp");
			return;
		}
				
		// Get the completion habits collection.
		DBCollection completionHabits = db.getCollection(completionCollection);
		
		// Find all of the user's habits within the completion habits collection.
		BasicBSONList habits = new BasicBSONList();
		DBCursor cursor = completionHabits.find(userQuery.get());
		 while (cursor.hasNext()) {
		     DBObject obj = cursor.next();
		     habits.add(obj);
		 }

		// ArrayLists that we will return.
		ArrayList<String> completionArrayList = new ArrayList<String>();
		ArrayList<Integer> currentStreaks = new ArrayList<Integer>();
		ArrayList<Integer> bestStreaks = new ArrayList<Integer>();
		ArrayList<Integer> maxStreaks = new ArrayList<Integer>();
		
		// Put information from the BSONList into the ArrayList.
		for (int i = 0; i < habits.size(); i++) {
			
			BasicDBObject object = (BasicDBObject) habits.get(i);
			String habitTitle = object.get(habitField).toString();
			// This is a bit of a hack I used to get around the way Mongo stores numbers.
			int currentStreak = Math.round(Float.parseFloat(object.get("current").toString()));
			int bestStreak = Integer.parseInt(object.get("best").toString());
			int maxStreak = Integer.parseInt(object.get(lengthField).toString());
			completionArrayList.add(habitTitle);
			currentStreaks.add(currentStreak);
			bestStreaks.add(bestStreak);
			maxStreaks.add(maxStreak);
			
		}
		
		request.setAttribute("habitTitles", completionArrayList);
		request.setAttribute("habitSize", completionArrayList.size());
		request.setAttribute("currentStreaks", currentStreaks);
		request.setAttribute("bestStreaks", bestStreaks);
		request.setAttribute("maxStreaks", maxStreaks);
		request.setAttribute("isUpdated", "");
		request.getRequestDispatcher("habits.jsp").forward(request, response);

		
	}

	/*
	 * HTTP Method: POST
	 * -----------------
	 * The reset method for COMPLETION habits.
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
		
		// Get the information we want from the form.
		String habit = request.getParameter("habit");
		int current = Integer.parseInt(request.getParameter("current").toString());
		int best = Integer.parseInt(request.getParameter("best").toString());

		// Find that habit within the database.
		QueryBuilder habitQuery = new QueryBuilder();
		habitQuery.put(idField).is(username);
		habitQuery.put(habitField).is(habit);

		habits.update(habitQuery.get(), new BasicDBObject("$set", new BasicDBObject("current", current)));
		habits.update(habitQuery.get(), new BasicDBObject("$set", new BasicDBObject("best", best)));

		request.getRequestDispatcher("habits.jsp").forward(request, response);
		
	}

}
