package org.nihul5.other;

import java.util.Date;

public class Post extends Message {
	public Post(int id, 
	            String username, 
	            double lat, 
	            double lng,
				Date creationTime, 
				String title, 
				String content) {
		super(id, username, lat, lng, creationTime, title, content);
	}
	
	public Post(String username, 
	            double lat, 
	            double lng,
				Date creationTime, 
				String title, 
				String content) {
		super(username, lat, lng, creationTime, title, content);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t[Post]\n\towner: " + username);
		sb.append("\n\tlat: " + lat);
		sb.append("\n\tlng: " + lng);
		sb.append("\n\tcreated on: " + creationDateStr());
		sb.append("\n\ttitle: " + title);
		sb.append("\n\tcontent: " + content);

		return sb.toString();
	}
}
