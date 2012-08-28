package org.nihul5.other;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLStorageTest {
	private static final Logger logger = Logger.getLogger(MySQLStorageTest.class);
	
	private MySQLStorage _db;
	
	public MySQLStorageTest(MySQLStorage db) {
		_db = db;
	}
	
	
	public void run() {
		logger.info("DB test is running");
		
		List<User> userlist = new ArrayList<User>();
		List<Post> postlist = new ArrayList<Post>();
		List<Event> eventlist = new ArrayList<Event>();
		
		// ++++++++++++++++++++++++++++++++++++++++
		userlist.add(new User("sol", "sol", "alex", "y", "sol@alex.com"));
		userlist.add(new User("wat", "wat", "iii", "feww", "soiewol@alex.com"));
		userlist.add(new User("lewl", "lewl", "is", "this", "is@alex.com"));
		
		
		
		// ++++++++++++++++++++++++++++++++++++++++
		postlist.add(new Post("sol", 2, 2, 
		                      new GregorianCalendar().getTime(), 
		                      "title", 
		                      "content"));
		postlist.add(new Post("non exist", 2, 2, 
		                      new GregorianCalendar().getTime(), 
		                      "title", 
		                      "content"));
		postlist.add(new Post("sol", 2, 2, 
		                      new GregorianCalendar().getTime(), 
		                      "title2", 
		                      "content2"));
		postlist.add(new Post("lewl", 2, 2, 
		                      new GregorianCalendar().getTime(), 
		                      "lewl_title2", 
		                      "lewl_content2"));
		
		// ++++++++++++++++++++++++++++++++++++++++
		Event event = new Event("sol", 2, 2, 
		                        new GregorianCalendar().getTime(),
		                        "eventtitle", 
		                        "eventcontent", 
		                        new GregorianCalendar().getTime(), 
		                        23);
		event.consensusDescList.add("consensus 1");
		event.consensusDescList.add("consensus 2");
		event.consensusDescList.add("consensus 3");
		eventlist.add(event);
		
		event = new Event("non exist", 2, 2, 
		                  new GregorianCalendar().getTime(),
		                  "eventtitle", 
		                  "eventcontent", 
		                  new GregorianCalendar().getTime(), 
		                  23);
		eventlist.add(event);
		

		event = new Event("wat", 2, 2, 
		                  new GregorianCalendar().getTime(),
		                  "eventtitleeeee", 
		                  "eventcontentwwaattt", 
		                  new GregorianCalendar().getTime(), 
		                  23);
		event.consensusDescList.add("watreq 2");
		event.consensusDescList.add("watreq 1");
		eventlist.add(event);
		
		for (User u : userlist)
			_db.saveUser(u);
		
		for (Event e : eventlist)
			_db.saveEvent(e);
		
		for (Post p : postlist)
			_db.savePost(p);
		
		_db.saveEventRegistration(1, "sol");
		_db.saveEventRegistration(1, "lewl");
		
		_db.deleteEventRegistration(1, "lewl");
		_db.deleteEventRegistration(1, "sol");
		
		_db.saveEventRegistration(1, "sol");
		_db.saveEventRegistration(1, "lewl");
		
		_db.voteOnConsensusReq("sol", 1, 2, true);
		_db.voteOnConsensusReq("sol", 1, 2, true);
		_db.voteOnConsensusReq("lewl", 1, 2, true);
		
		_db.deleteEventRegistration(1, "lewl");
		_db.deleteEventRegistration(1, "sol");
		
		_db.saveEventRegistration(1, "sol");
		_db.saveEventRegistration(1, "lewl");
		
		_db.voteOnConsensusReq("sol", 1, 2, true);
		_db.deleteEventRegistration(1, "lewl");
	}
}
