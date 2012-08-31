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
	Message getMessage(int msgid);
	boolean deleteMessage(int msgid);
	
	List<Message> searchMessages(String[] keywords);
	
	/**
	 * @param lat
	 *            is the angular distance, in degrees, minutes, and seconds of a
	 *            point north or south of the Equator. Lines of latitude are
	 *            often referred to as parallels.
	 * @param lng
	 *            is the angular distance, in degrees, minutes, and seconds, of
	 *            a point east or west of the Prime (Greenwich) Meridian. Lines
	 *            of longitude are often referred to as meridians.
	 * @param distance
	 * @return
	 */
	List<Message> searchMessages(double lat, double lng, double distance);
	
	boolean saveEventRegistration(int eventid, String username);
	boolean deleteEventRegistration(int eventid, String username);
	boolean isUserRegisteredToEvent(String username, int eventid);
	
	List<Consensus> getEventConsensusReqs(int eventid);
	boolean voteOnConsensusReq(String username, int eventid, int reqid, boolean accept);
	
	List<Message> getUserCreatedMessages(String username);
	List<Message> getUserRegisteredEvents(String username);
}
