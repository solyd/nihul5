package org.nihul5.other;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	public String id;
	public final String owner;
	
	public final double latitude;
	public final double longitude;
	private final Date creationDate;
	
	public final String title;
	public final String content;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Message(String owner, 
	               double latitude, 
	               double longitude,
	               Date creationTime, 
	               String title, 
	               String content) {
		this.owner = owner;
		this.latitude = latitude;
		this.longitude = longitude;
		this.creationDate = creationTime;
		this.title = title;
		this.content = content;
	}
	
	public String creationDate() {
		return sdf.format(this.creationDate);
	}
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Message]\n\towner: " + owner);
		sb.append("\n\tlat: " + latitude);
		sb.append("\n\tlng: " + longitude);
		sb.append("\n\tcreated on: " + creationDate());
		sb.append("\n\ttitle: " + title);
		sb.append("\n\tcontent: " + content);
		
		return sb.toString();
	}
}
