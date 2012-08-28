package org.nihul5.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event extends Message {
	public final Date eventDate;
	public final int capacity;
	public final List<String> consensusDescList = new ArrayList<String>();
	
	public Event(int id, 
	             String username, 
	             double lat, 
	             double lng,
	             Date creationTime, 
	             String title, 
	             String content,
	             Date eventDate,
	             int capacity) {
		super(id, username, lat, lng, creationTime, title, content);
		this.eventDate = eventDate;
		this.capacity = capacity;
	}
	
	public Event(String username, 
	             double lat, 
	             double lng,
	             Date creationTime, 
	             String title, 
	             String content,
	             Date eventDate,
	             int capacity) {
		super(username, lat, lng, creationTime, title, content);
		this.eventDate = eventDate;
		this.capacity = capacity;
	}
	
	public String eventDateStr() {
		return sdf.format(eventDate);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t[Event]\n\towner: " + username);
		sb.append("\n\tlat: " + lat);
		sb.append("\n\tlng: " + lng);
		sb.append("\n\tcreated on: " + creationDateStr());
		sb.append("\n\ttitle: " + title);
		sb.append("\n\tcontent: " + content);
		sb.append("\n\tevent date: " + eventDateStr());
		sb.append("\n\tcapacity: " + capacity);
		sb.append("\n\t# of consensus req: " + consensusDescList.size());

		return sb.toString();
	}
}
