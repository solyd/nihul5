package org.nihul5.other;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.nihul5.other.Message.MessageType;

public class MySQLStorageTest {
	private static final Logger logger = Logger.getLogger(MySQLStorageTest.class);
	
	private MySQLStorage _db;
	
	public MySQLStorageTest(MySQLStorage db) {
		_db = db;
	}
	
	public void run() {
		_db.saveUser(new User("sol", "sol", "sad", "sja", "email"));
		_db.saveUser(new User("blabla", "blabla", "sad", "sja", "email"));
		
		_db.saveMessage(new Message( "sol", 2, 2, 219, "some title", "content" ));
		_db.saveMessage(new Message( "blabla", 2, 2, 2921219, "some titlofiewe", "contenot" ));
		
		Message blaevent = new Message();
		blaevent.username = "blabla";
		blaevent.capacity = 3;
		blaevent.consensusDescList.add("bla cons 1");
		blaevent.consensusDescList.add("bla cons 2");
		blaevent.content = "blaeevent desc";
		blaevent.creationTime = 213192;
		blaevent.eventTime = new Timestamp(3000, 1, 1, 1, 1, 1, 1).getTime();
		blaevent.lat = 2;
		blaevent.lng = 3;
		blaevent.title = "blaevent title";
		blaevent.type = MessageType.EVENT;
		
		Message blaevent2 = new Message();
		blaevent2.username = "blabla";
		blaevent2.capacity = 3;
		blaevent2.consensusDescList.add("bla conIIIIs 1");
		blaevent2.consensusDescList.add("bla IIIcons 2");
		blaevent2.content = "blaifewnofiewII____eevent desc";
		blaevent2.creationTime = 213192;
		blaevent2.eventTime = new Timestamp(3000, 1, 1, 1, 1, 1, 1).getTime();
		blaevent2.lat = 2;
		blaevent2.lng = 3;
		blaevent2.title = "blaeveiIIIIIIIIIIIInt title";
		blaevent2.type = MessageType.EVENT;
		
		_db.saveMessage(blaevent);
		_db.saveMessage(blaevent2);
		
		try {
			_db.saveEventRegistration(3, "sol");
			_db.saveEventRegistration(4, "sol");
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}
}
