package org.nihul5.other;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
	public int id;
	public String username;
	public double lat;
	public double lng;
	public Timestamp creationTime;	
	public String title;
	public String content;
	
	public Timestamp eventTime;
	public int capacity;
	public List<String> consensusDescList = new ArrayList<String>();
	
	public MessageType type = MessageType.POST;
	
	public enum MessageType {
		POST,
		EVENT
	}
	
	public static MessageType stringToType(String type) {
		if (type.equals(MessageType.EVENT.toString()))
			return MessageType.EVENT;
		return MessageType.POST;
	}
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Message() {}
	
	// ctor for basic info retrieval from db
	public Message(int id,
	               String title,
	               String type) {
		this.id = id;
		this.title = title;
		if (type.equals("EVENT"))
			this.type = MessageType.EVENT;
		else
			this.type = MessageType.POST;
	}
	
	// ctor for passing a POST message to db
	public Message(String username, 
	               double lat, 
	               double lng,
	               Timestamp creationTime, 
	               String title, 
	               String content) {
		this.username = username;
		this.lat = lat;
		this.lng = lng;
		this.creationTime = creationTime;
		this.title = title;
		this.content = content;
		
		this.id = -1;
	}
	
	public Message(int id,
	               String username, 
	               double lat, 
	               double lng,
	               Timestamp creationTime, 
	               String title, 
	               String content) {
		this.id = id;
		this.username = username;
		this.lat = lat;
		this.lng = lng;
		this.creationTime = creationTime;
		this.title = title;
		this.content = content;
	}
	
	public String creationDateStr() {
		return sdf.format(this.creationTime);
	}
	
	public String eventDateStr() {
		return sdf.format(eventTime);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case EVENT:
			sb.append("\n\t[Event]\n\towner: " + username);
			sb.append("\n\tlat: " + lat);
			sb.append("\n\tlng: " + lng);
			sb.append("\n\tcreated on: " + creationDateStr());
			sb.append("\n\ttitle: " + title);
			sb.append("\n\tcontent: " + content);
			sb.append("\n\tevent date: " + eventDateStr());
			sb.append("\n\tcapacity: " + capacity);
			sb.append("\n\t# of consensus req: " + consensusDescList.size());
			break;
		case POST:
			sb.append("\n\t[Post]\n\towner: " + username);
			sb.append("\n\tlat: " + lat);
			sb.append("\n\tlng: " + lng);
			sb.append("\n\tcreated on: " + creationDateStr());
			sb.append("\n\ttitle: " + title);
			sb.append("\n\tcontent: " + content);
			break;
		}
		
		return sb.toString();
	}
}
