package org.nihul5.other;

import java.util.List;
import java.util.Map;

public interface Storage {
	void init();
	
	// User operations
	// ++++++++++++++++++++++++++++++++++++++++
	boolean 		saveUser(User user);
	boolean 		deleteUser(String username);
	List<User> 		getUsers();
	List<String>	getUserNames();
	User 			getUser(String username);
	
	
	boolean savePost(Post post);
	boolean saveEvent(Event event);
	boolean deleteMessage(int msgid);
		
	boolean saveEventRegistration(int eventid, String username);
	boolean deleteEventRegistration(int eventid, String username);
	
	List<Consensus> getEventConsensusReqs(int eventid);
	boolean voteOnConsensusReq(String username, int eventid, int reqid, boolean accept);
	
	List<Message> getUserMessages(String username);
}
