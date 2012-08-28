package org.nihul5.other;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Message {
	public final int id;
	public final String username;
	public final double lat;
	public final double lng;
	public final Date creationDate;	
	public final String title;
	public final String content;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Message(String username, 
	               double lat, 
	               double lng,
	               Date creationTime, 
	               String title, 
	               String content) {
		this.username = username;
		this.lat = lat;
		this.lng = lng;
		this.creationDate = creationTime;
		this.title = title;
		this.content = content;
		
		this.id = -1;
	}
	
	public Message(int id,
	               String username, 
	               double lat, 
	               double lng,
	               Date creationTime, 
	               String title, 
	               String content) {
		this.id = id;
		this.username = username;
		this.lat = lat;
		this.lng = lng;
		this.creationDate = creationTime;
		this.title = title;
		this.content = content;
	}
	
	public String creationDateStr() {
		return sdf.format(this.creationDate);
	}
}
