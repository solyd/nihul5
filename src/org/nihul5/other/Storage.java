package org.nihul5.other;

import java.util.List;
import java.util.Map;

public interface Storage {
	void init();

	boolean 		saveUser(User user);
	boolean 		deleteUser(String username);
	List<String>	getUserNames();
	User 			getUser(String username);

	boolean saveMessage(Message msg);
	boolean getMessage(int msgid);
	boolean deleteMessage(int msgid);
		
	boolean saveEventRegistration(int eventid, String username);
	boolean deleteEventRegistration(int eventid, String username);
	
	List<Consensus> getEventConsensusReqs(int eventid);
	boolean voteOnConsensusReq(String username, int eventid, int reqid, boolean accept);
	
	List<Message> getUserCreatedMessages(String username);
	List<Message> getUserRegisteredEvents(String username);
}
