package org.nihul5.other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.nihul5.other.Message.MessageType;


public class MySQLStorage implements Storage {
	private static final Logger logger = Logger.getLogger(MySQLStorage.class);

	private final DataSource _dbcPool;

	/**
	 * @param dbcPool Database Connection Pool. This object will be used 
	 * for retrieving connections to the DB.
	 */
	public MySQLStorage(DataSource dbcPool) {
		assert(dbcPool != null);

		_dbcPool = dbcPool;
	}

	public void init() {
		logger.info("initializing MySQL DB");

		assert(_dbcPool != null);
		List<String> initStatements = new ArrayList<String>();
		StringBuilder sb;

		if (CONST.RESET_DB) {
			initStatements.add("DROP TABLE IF EXISTS consensus_votes;");
			initStatements.add("DROP TABLE IF EXISTS consensus;");
			initStatements.add("DROP TABLE IF EXISTS event_reg;");
			initStatements.add("DROP TABLE IF EXISTS events;");
			initStatements.add("DROP TABLE IF EXISTS messages;");
			initStatements.add("DROP TABLE IF EXISTS message_types;");
			initStatements.add("DROP TABLE IF EXISTS user_roles;");
			initStatements.add("DROP TABLE IF EXISTS roles;");
			initStatements.add("DROP TABLE IF EXISTS users;");
		}

		// Create the basic tables - users, roles and user_roles. 
		// This way any user can have multiple roles

		// roles TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append(String.format
		          ("CREATE TABLE IF NOT EXISTS roles (role VARCHAR(%d) NOT NULL PRIMARY KEY) ENGINE = InnoDB;", 
		           CONST.MAX_ROLENAME_LEN));
		initStatements.add(sb.toString());

		// There has to be at least one default role for every user
		sb = new StringBuilder();
		sb.append(String.format
		          ("INSERT IGNORE INTO roles (role) VALUES ('%s');",
		           CONST.DEFAULT_ROLE));
		initStatements.add(sb.toString());

		// users TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS users (");
		sb.append(String.format("username VARCHAR(%d) NOT NULL PRIMARY KEY, ", CONST.MAX_USERNAME_LEN));
		sb.append(String.format("password VARCHAR(%d) NOT NULL, ", CONST.MAX_PASSWORD_LEN));
		sb.append(String.format("first_name VARCHAR(%d), ", CONST.MAX_NAME_LEN));
		sb.append(String.format("last_name VARCHAR(%d), ", CONST.MAX_NAME_LEN));
		sb.append(String.format("email VARCHAR(%d), ", CONST.MAX_NAME_LEN));
		sb.append("photo LONGBLOB");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());

		// user_roles TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS user_roles (");
		sb.append(String.format("username VARCHAR(%d) NOT NULL, ", CONST.MAX_USERNAME_LEN)); 
		sb.append(String.format("role VARCHAR(%d) NOT NULL, ", CONST.MAX_ROLENAME_LEN));
		sb.append("FOREIGN KEY (username) REFERENCES users(username) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE,");
		sb.append("FOREIGN KEY (role) REFERENCES roles(role) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE,");
		sb.append("PRIMARY KEY (username, role)");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());

		// message_types TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS message_types (type VARCHAR(20) NOT NULL PRIMARY KEY) ENGINE = InnoDB;"); 
		initStatements.add(sb.toString());

		sb = new StringBuilder();
		sb.append("INSERT IGNORE INTO message_types (type) VALUES ('POST'), ('EVENT');");
		initStatements.add(sb.toString());                        

		// messages TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS messages (");
		sb.append("msgid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, ");
		sb.append(String.format("username VARCHAR(%d) NOT NULL, ", CONST.MAX_USERNAME_LEN));
		sb.append("lat DOUBLE NOT NULL, ");
		sb.append("lng DOUBLE NOT NULL, ");
		sb.append("creation_date BIGINT NOT NULL, ");
		sb.append(String.format("title VARCHAR(%d) NOT NULL, ", CONST.MSG_MAX_TITLE_LEN));
		sb.append(String.format("content VARCHAR(%d), ", CONST.MSG_MAX_CONTENT_LEN));
		sb.append("type VARCHAR(20) NOT NULL, ");
		sb.append("FOREIGN KEY (username) REFERENCES users(username) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("FOREIGN KEY (type) REFERENCES message_types(type) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("KEY(lat, lng) ");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());

		// events TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS events (");
		sb.append("msgid BIGINT NOT NULL PRIMARY KEY, ");
		sb.append("event_date BIGINT NOT NULL, ");
		sb.append("capacity INT NOT NULL, ");
		sb.append("FOREIGN KEY (msgid) REFERENCES messages(msgid) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE");
		sb.append(") ENGINE = InnoDB");
		initStatements.add(sb.toString());

		// event_reg TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS event_reg (");
		sb.append("msgid BIGINT NOT NULL, ");
		sb.append(String.format("username VARCHAR(%d) NOT NULL, ", CONST.MAX_USERNAME_LEN));
		sb.append("FOREIGN KEY (msgid) REFERENCES messages(msgid) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("FOREIGN KEY (username) REFERENCES users(username) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("PRIMARY KEY (msgid, username)");
		sb.append(") ENGINE = InnoDB");
		initStatements.add(sb.toString());

		// consensus TABLE 
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS consensus (");
		sb.append("consid BIGINT NOT NULL AUTO_INCREMENT, ");
		sb.append("msgid BIGINT NOT NULL, ");
		sb.append(String.format("description VARCHAR(%d) NOT NULL, ", CONST.MAX_DESC_LEN));
		sb.append("status VARCHAR(20) NOT NULL, ");
		sb.append("FOREIGN KEY (msgid) REFERENCES messages(msgid) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("PRIMARY KEY (consid, msgid)");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());

		// consensus_votes TABLE 
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS consensus_votes (");
		sb.append("consid BIGINT NOT NULL, ");
		sb.append("msgid BIGINT NOT NULL, ");
		sb.append(String.format("username VARCHAR(%d) NOT NULL, ", CONST.MAX_USERNAME_LEN));
		sb.append("FOREIGN KEY (msgid) REFERENCES messages(msgid) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("FOREIGN KEY (consid) REFERENCES consensus(consid) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("FOREIGN KEY (username) REFERENCES users(username) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("PRIMARY KEY (consid, msgid, username)");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());


		// execute all init statements
		// ++++++++++++++++++++++++++++++++++++++++
		Connection conn = null;
		Statement s = null;
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			// This is initialization, no other transaction is running
			// on our tables so it safe to use READ UNCOMMITTED
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			s = conn.createStatement();
			for (String statement : initStatements) {
				logger.debug("Executing sql statement:\n\t" + statement);
				s.execute(statement);
			}

			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Problem during db tables initializaion", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
		}
		finally {
			if (s != null)
				try { s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}

	@Override
	public boolean saveUser(User user) {
		if (user == null)
			throw new IllegalArgumentException("User is null");

		logger.info("Saving user:\n\t" + user.toString());

		Connection conn = null;
		PreparedStatement prep_s = null;
		PreparedStatement prep_s2 = null;
		String sql;
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			sql = "INSERT INTO users (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?);";
			prep_s = (PreparedStatement) conn.prepareStatement(sql);

			String md5password = user.md5password();
			assert(md5password != null);

			prep_s.setString(1, user.username);
			prep_s.setString(2, md5password);
			prep_s.setString(3, user.firstName);
			prep_s.setString(4, user.lastName);
			prep_s.setString(5, user.email);

			prep_s.executeUpdate();

			// ++++++++++++++++++++++++++++++++++++++++

			sql = "INSERT INTO user_roles (username, role) VALUES (?, 'user');";
			prep_s2 = (PreparedStatement) conn.prepareStatement(sql);
			prep_s2.setString(1, user.username);

			if (prep_s2.executeUpdate() == 0) {
				logger.error("Error inserting new user to user_roles table. user: " + user.toString());
				throw new SQLException();
			}

			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("SQLException while adding user: " + user.toString(), e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prep_s2 != null)
				try { prep_s2.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public boolean deleteUser(String username) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		
		logger.info("Deleting user:\n\t" + username);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			// Before we can delete the user we need to get all of the events he 
			// is registered to, and then for each event all of the consensus
			// requirements - to check if they can be toggled because of users
			// deletion.
			List<Message> userEvents = getUserRegisteredEvents(conn, username);			

			String sql = "DELETE FROM users WHERE username = ?;";
			prep_s = conn.prepareStatement(sql);
			prep_s.setString(1, username);

			prep_s.executeUpdate();
			
			for (Message event : userEvents) {
				for (Integer consid : getEventConsensusIds(conn, event.id)) {
					toggleConsensusReqIfNeeded(conn, consid, event.id);
				}
			}

			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception in during user deletion", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public List<String> getUserNames() {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			String sql = "SELECT username FROM users;";
			prep_s = conn.prepareStatement(sql);

			rs = prep_s.executeQuery();

			List<String> res = new ArrayList<String>();
			while (rs.next())
				res.add(rs.getString("username"));

			return res;
		} 
		catch (SQLException e) {
			logger.error("Exception during getUserName", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
		}
		finally {
			if (rs != null)
				try { rs.close(); } catch (SQLException e1) { logger.error("Can't close result set", e1); }
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return null;
	}

	@Override
	public User getUser(String username) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);

			String sql = "SELECT * FROM users WHERE username = ?";
			prep_s = conn.prepareStatement(sql);
			prep_s.setString(1, username);

			rs = prep_s.executeQuery();
			if (!rs.next())
				return null;

			User res = new User(username, 
			                    "", 
			                    rs.getString("first_name"), 
			                    rs.getString("last_name"), 
			                    rs.getString("email"));

			return res;
		} 
		catch (SQLException e) {
			logger.error("Exception during getUser", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
		}
		finally {
			if (rs != null)
				try { rs.close(); } catch (SQLException e1) { logger.error("Can't close result set", e1); }
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return null;
	}
	
	@Override
	public int saveMessage(Message msg) {
		switch (msg.type) {
		case EVENT:
			return saveEvent(msg);
		case POST:
			return savePost(msg);
		}
		
		return -1;
	}

	private int savePost(Message post) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		int newmsgid = -1;
		
		logger.info("Saving " + post.toString());
		
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO messages (username, lat, lng, creation_date, title, content, type) ");
			sqlsb.append("VALUES (?, ?, ?, ?, ?, ?, 'POST');");
			
			prep_s = conn.prepareStatement(sqlsb.toString());
			prep_s.setString(1, post.username);
			prep_s.setDouble(2, post.lat);
			prep_s.setDouble(3, post.lng);
			prep_s.setLong(4, post.creationTime);
			prep_s.setString(5, post.title);
			prep_s.setString(6, post.content);

			prep_s.executeUpdate();
			
			newmsgid = getLastInsertId(conn);
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception during getUser", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			return -1;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return newmsgid;
	}

	private int saveEvent(Message event) {
		Connection conn = null;
		PreparedStatement prepMsg = null;
		PreparedStatement prepEvent = null;
		PreparedStatement prepCons = null;
		PreparedStatement prepReg = null;
		int neweventid = -1;
		
		logger.info("Saving " + event.toString());
		
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			// insert into messages table
			// ++++++++++++++++++++++++++++++++++++++++
			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO messages (username, lat, lng, creation_date, title, content, type) ");
			sqlsb.append("VALUES (?, ?, ?, ?, ?, ?, 'EVENT');");
			
			prepMsg = conn.prepareStatement(sqlsb.toString());
			prepMsg.setString(1, event.username);
			prepMsg.setDouble(2, event.lat);
			prepMsg.setDouble(3, event.lng);
			prepMsg.setLong(4, event.creationTime);
			prepMsg.setString(5, event.title);
			prepMsg.setString(6, event.content);

			prepMsg.executeUpdate();
			
			// the id of the new message
			int last_insert_id = getLastInsertId(conn);
			neweventid = last_insert_id;
			if (last_insert_id < 0)
				throw new SQLException();
			
			// insert into events table
			// ++++++++++++++++++++++++++++++++++++++++
			sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO events (msgid, event_date, capacity) ");
			sqlsb.append("VALUES (?, ?, ?);");
			
			prepEvent = conn.prepareStatement(sqlsb.toString());
			prepEvent.setInt(1, last_insert_id);
			prepEvent.setLong(2, event.eventTime);
			prepEvent.setInt(3, event.capacity);
			
			prepEvent.executeUpdate();
			
			// register the creating user to the event
			// ++++++++++++++++++++++++++++++++++++++++
			sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO event_reg (msgid, username) ");
			sqlsb.append("VALUES (?, ?);");
			
			prepReg = conn.prepareStatement(sqlsb.toString());
			prepReg.setInt(1, last_insert_id);
			prepReg.setString(2, event.username);
			
			prepReg.executeUpdate();
			
			// insert consensus requirements to consensus table
			// ++++++++++++++++++++++++++++++++++++++++
			sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO consensus (msgid, description, status) ");
			sqlsb.append(String.format("VALUES (?, ?, '%s');", Consensus.Status.NOT_ACCEPTED.toString()));
			
			prepCons = conn.prepareStatement(sqlsb.toString());
			for (String desc : event.consensusDescList) {
				prepCons.setInt(1, last_insert_id);
				prepCons.setString(2, desc);
				prepCons.addBatch();
			}
			prepCons.executeBatch();
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception during getUser", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return -1;
		}
		finally {
			if (prepMsg != null)
				try { prepMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepCons != null)
				try { prepCons.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEvent != null)
				try { prepEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepReg != null)
				try { prepReg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return neweventid;
	}

	@Override
	public boolean deleteMessage(int msgid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		PreparedStatement prepEventCheck = null;
		ResultSet eventrs = null;
		
		logger.info("Deleting msg: " + msgid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			// Check if this is an event - if so, don't allow deletion of past events
			String sql = "SELECT * FROM events WHERE msgid = ?;";
			prepEventCheck = conn.prepareStatement(sql);
			prepEventCheck.setInt(1, msgid);

			eventrs = prepEventCheck.executeQuery();
			if (eventrs.next()) {
				logger.info("Attempting to delete an EVENT " + msgid);
				long eventDate = eventrs.getLong("event_date");
				long currDate = Utility.getCurrentTime();
				
				if (eventDate < currDate) {
					logger.error("Can't delete past event " + msgid);
					return false;
				}	
			}
			
			sql = "DELETE FROM messages WHERE msgid = ?;";
			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, msgid);

			prep_s.executeUpdate();

			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception in during user deletion", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEventCheck != null)
				try { prepEventCheck.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (eventrs != null)
				try { eventrs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}
	
	@Override
	public Message getMessage(int msgid) {
		Connection conn = null;
		PreparedStatement prepMsg = null;
		PreparedStatement prepEvent = null;
		ResultSet rsMsg = null;
		ResultSet rsEvent = null;
		Message msg = new Message();
		
		logger.info("Retreiving msgid " + msgid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			String sql = "SELECT * FROM messages WHERE msgid = ?;";
			prepMsg = conn.prepareStatement(sql);
			prepMsg.setInt(1, msgid);

			rsMsg = prepMsg.executeQuery();
			if (!rsMsg.next()) {
				logger.error("There is no such msgid " + msgid);
				return null;
			}
			
			msg.id = msgid;
			msg.username = rsMsg.getString("username");
			msg.lat = rsMsg.getDouble("lat");
			msg.lng = rsMsg.getDouble("lng");
			msg.creationTime = rsMsg.getLong("creation_date");
			msg.title = rsMsg.getString("title");
			msg.content = rsMsg.getString("content");
			msg.type = Message.stringToType(rsMsg.getString("type"));
			
			if (msg.type == MessageType.EVENT) {
				sql = "SELECT * FROM events WHERE msgid = ?;";
				prepEvent = conn.prepareStatement(sql);
				prepEvent.setInt(1, msgid);

				rsEvent = prepEvent.executeQuery();
				if (!rsEvent.next()) {
					logger.error("Though it was an event but couldn't find the record in events: " + msgid);
					return null;
				}

				msg.eventTime = rsEvent.getLong("event_date");
				msg.capacity = rsEvent.getInt("capacity");
				
				msg.consReqList = getEventConsensusList(conn, msgid);
				msg.nSubs = getRegisteredCount(conn, msgid);
			}
			
						
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return null;
		}
		finally {
			if (prepMsg != null)
				try { prepMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEvent != null)
				try { prepEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsMsg != null)
				try { rsMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsEvent != null)
				try { rsEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
		
		return msg;
	}

	@Override
	public boolean saveEventRegistration(int eventid, String username) throws Exception {
		Connection conn = null;
		PreparedStatement prep_s = null;
		PreparedStatement prepMsgCheck = null;
		ResultSet rsMsg = null;
		
		logger.info("Saving event registration for event " + eventid + " by " + username);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			String sql = "SELECT * FROM events WHERE msgid = ?;";
			prepMsgCheck = conn.prepareStatement(sql);
			prepMsgCheck.setInt(1, eventid);

			rsMsg = prepMsgCheck.executeQuery();
			if (!rsMsg.next()) {
				throw new Exception(CONST.EVENT_DELETED);
//				logger.error("Can't register to eventid " + eventid + " - event doesn't exist");
//				return false;
			}
			
			long eventDate = rsMsg.getLong("event_date");
			long currDate = Utility.getCurrentTime();
			int capacity = rsMsg.getInt("capacity");
			int registeredCount = getRegisteredCount(conn, eventid);
			
			if (registeredCount == capacity) {
				throw new Exception(CONST.EVENT_FULL);
//				logger.error("Can't register to event " + eventid + " - there is no room left");
//				return false;
			}
			
			if (eventDate < currDate) {
				throw new Exception(CONST.EVENT_PASSED);
//				logger.error("Can't register to event " + eventid + " - deadline has passed");
//				return false;
			}

			sql = "INSERT INTO event_reg (msgid, username) VALUES (?, ?);";
			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, eventid);
			prep_s.setString(2, username);

			prep_s.executeUpdate();
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error(String.format("Exception when saving registration for event : %d by %s", eventid, username));
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepMsgCheck != null)
				try { prepMsgCheck.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsMsg != null)
				try { rsMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public boolean deleteEventRegistration(int eventid, String username) {
		Connection conn = null;
		PreparedStatement prepEventReg = null;
		PreparedStatement prepConsVotes = null;
		PreparedStatement prepCons = null;
		PreparedStatement prepEventCheck = null;
		ResultSet rs = null;
		ResultSet eventrs = null;

		logger.info("deleting event registration by " + username + "to event " + eventid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			// Check that the event exists and hasn't occured yet
			String sql = "SELECT * FROM events WHERE msgid = ?;";
			prepEventCheck = conn.prepareStatement(sql);
			prepEventCheck.setInt(1, eventid);

			eventrs = prepEventCheck.executeQuery();
			if (!eventrs.next()) {
				logger.error("Can't delete registeration to eventid " + eventid + " - event doesn't exist");
				return false;
			}

			long eventDate = eventrs.getLong("event_date");
			long currDate = Utility.getCurrentTime();

			if (eventDate < currDate) {
				logger.error("Can't delete registeration to eventid " + eventid + " - deadline has passed");
				return false;
			}


			// delete from registration table
			// ++++++++++++++++++++++++++++++++++++++++
			sql = "DELETE FROM event_reg WHERE msgid = ? AND username = ?;";
			prepEventReg = conn.prepareStatement(sql);
			prepEventReg.setInt(1, eventid);
			prepEventReg.setString(2, username);

			prepEventReg.executeUpdate();
			
			// delete all casted votes for this event by the user
			// ++++++++++++++++++++++++++++++++++++++++
			sql = "DELETE FROM consensus_votes WHERE msgid = ? AND username = ?;";
			prepConsVotes = conn.prepareStatement(sql);
			prepConsVotes.setInt(1, eventid);
			prepConsVotes.setString(2, username);
			
			prepConsVotes.executeUpdate();
			
			// now check if the status should be toggled for any of the consensus reqs of the event
			// ++++++++++++++++++++++++++++++++++++++++
			sql = "SELECT consid FROM consensus WHERE msgid = ?;";
			prepCons = conn.prepareStatement(sql);
			prepCons.setInt(1, eventid);
			
			rs = prepCons.executeQuery();
			while (rs.next()) {
				int consid = rs.getInt("consid");
				toggleConsensusReqIfNeeded(conn, consid, eventid);
			}
			
			conn.commit();
		}
		catch (SQLException e) {
			logger.error(String.format("Exception when deleting registration for event : %d by %s", eventid, username));
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return false;
		}
		finally {
			if (prepEventReg != null)
				try { prepEventReg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepCons != null)
				try { prepCons.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepConsVotes != null)
				try { prepConsVotes.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rs != null)
				try { rs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (eventrs != null)
				try { eventrs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEventCheck != null)
				try { prepEventCheck.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public List<Consensus> getEventConsensusReqs(int eventid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();

			String sql = "SELECT * FROM consensus WHERE msgid = ?;";
			
			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, eventid);
			
			rs = prep_s.executeQuery();
			List<Consensus> res = new ArrayList<Consensus>();
			while (rs.next()) {
				Consensus.Status status = Consensus.getConsensusStatus(rs.getString("status"));
				String desc = rs.getString("description");
				int id = rs.getInt("consid");
				
				res.add(new Consensus(id, desc, status));
			}

			return res;
		} 
		catch (SQLException e) {
			logger.error("Error getting event consensus reqs of eventdi " + eventid);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return null;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}

	@Override
	public boolean voteOnConsensusReq(String username, int eventid, int reqid, boolean accept) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		
		logger.info(username + "votes on consensus req " + reqid + " of event " + eventid + " for change? " + accept);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			// The user isn't registered to the event, can't vote on this consensus req
			if (!isUserRegistered(conn, username, eventid))
				return false;

			StringBuilder sqlsb = new StringBuilder();
			if (accept) {
				sqlsb.append("INSERT INTO consensus_votes (consid, msgid, username) ");
				sqlsb.append("VALUES (?, ?, ?);");

				prep_s = conn.prepareStatement(sqlsb.toString());
				prep_s.setInt(1, reqid);
				prep_s.setInt(2, eventid);
				prep_s.setString(3, username);
			} else  {
				sqlsb.append("DELETE FROM consensus_votes WHERE consid = ? AND msgid = ? AND username = ?;");
				
				prep_s = conn.prepareStatement(sqlsb.toString());
				prep_s.setInt(1, reqid);
				prep_s.setInt(2, eventid);
				prep_s.setString(3, username);
			}

			prep_s.executeUpdate();
			toggleConsensusReqIfNeeded(conn, reqid, eventid);
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Error getting event consensus reqs of eventdi " + eventid);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public List<Message> getUserCreatedMessages(String username) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;
		List<Message> res = new ArrayList<Message>();

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);

			String sql = "SELECT msgid, type, title FROM messages WHERE username = ?;";
			
			prep_s = conn.prepareStatement(sql);
			prep_s.setString(1, username);
			
			rs = prep_s.executeQuery();
			while (rs.next()) {
				int msgid = rs.getInt("msgid");
				String type = rs.getString("type");
				String title = rs.getString("title");
				
				res.add(new Message(msgid, title, type));
			}

			return res;
		} 
		catch (SQLException e) {
			logger.error("", e);
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return null;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	public List<Message> getUserRegisteredEvents(String username) {
		Connection conn = null;
		PreparedStatement prep_s = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);
			return getUserRegisteredEvents(conn, username);
		} 
		catch (SQLException e) {
			logger.error("", e);
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return null;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	@Override
	public List<Message> searchMessages(String[] keywords) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Message> searchMessages(double lat, double lng, double distance) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;
		List<Message> res = new ArrayList<Message>();
		GeoLocation location = null;
		
		try {
			location = GeoLocation.fromDegrees(lat, lng);
		} 
		catch (Exception e) {
			logger.error("", e);
			return res;
		}
		
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);

			GeoLocation[] boundingCoordinates = location.boundingCoordinates(distance, CONST.EARTH_RADIUS);
			boolean meridian180WithinDistance =
					boundingCoordinates[0].getLongitudeInDegrees() > 
					boundingCoordinates[1].getLongitudeInDegrees();

			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("SELECT * FROM messages WHERE (lat >= ? AND lat <= ?) AND (lng >= ? ");
			sqlsb.append((meridian180WithinDistance ? "OR" : "AND") + " lng <= ?) AND ");
			sqlsb.append("acos(sin(radians(?)) * sin(radians(lat)) + cos(radians(?)) * cos(radians(lat)) * cos(radians(lng - ?))) <= ?;");
			
			prep_s = conn.prepareStatement(sqlsb.toString());
			prep_s.setDouble(1, boundingCoordinates[0].getLatitudeInDegrees());
			prep_s.setDouble(2, boundingCoordinates[1].getLatitudeInDegrees());
			prep_s.setDouble(3, boundingCoordinates[0].getLongitudeInDegrees());
			prep_s.setDouble(4, boundingCoordinates[1].getLongitudeInDegrees());
			prep_s.setDouble(5, location.getLatitudeInDegrees());
			prep_s.setDouble(6, location.getLatitudeInDegrees());
			prep_s.setDouble(7, location.getLongitudeInDegrees());
			prep_s.setDouble(8, distance / CONST.EARTH_RADIUS);
			
			rs = prep_s.executeQuery();
			
			while (rs.next()) {
				Message msg = new Message();
				msg.id = rs.getInt("msgid");
				msg.lat = rs.getDouble("lat");
				msg.lng = rs.getDouble("lng");
				msg.content = rs.getString("content");
				msg.title = rs.getString("title");
				msg.type = Message.stringToType(rs.getString("type"));
				
				res.add(msg);
			}
			
			return res;
		}
		catch (SQLException e) {
			logger.error("", e);
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
		
		return res;
	}	

	@Override
	public boolean isUserRegisteredToEvent(String username, int eventid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);
			
			String sql = "SELECT COUNT(*) FROM event_reg WHERE username = ? AND msgid = ?;";
			
			prep_s = conn.prepareStatement(sql);
			prep_s.setString(1, username);
			prep_s.setInt(2, eventid);

			rs = prep_s.executeQuery();

			if (!rs.next())
				throw new SQLException();

			return rs.getInt(1) > 0;
		} 
		catch (SQLException e) {
			logger.error("", e);
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rs != null)
				try { rs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	@Override
	public List<User> getEventRegisteredUsers(int eventid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);

			String sql = "SELECT username FROM event_reg WHERE msgid = ?;";

			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, eventid);

			rs = prep_s.executeQuery();
			
			List<User> res = new ArrayList<User>();
			while (rs.next()) {
				User u = new User();
				u.username = rs.getString("username");
				res.add(u);
			}

			return res;
		} 
		catch (SQLException e) {
			logger.error("", e);
			//			if (conn != null)
			//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return null;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rs != null)
				try { rs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	@Override
	public boolean didUserVote(String username, int consid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		ResultSet rs = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(true);

			String sql = "SELECT username FROM consensus_votes WHERE consid = ?;";

			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, consid);

			rs = prep_s.executeQuery();
			
			while (rs.next()) {
				String name = rs.getString("username");
				if (name.equals(username))
					return true;
			}

			return false;
		} 
		catch (SQLException e) {
			logger.error("", e);
			//			if (conn != null)
			//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return false;
		}
		finally {
			if (prep_s != null)
				try { prep_s.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rs != null)
				try { rs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	@Override
	public Consensus getConsensus(int consid) {
		Connection conn = null;
		PreparedStatement prepCons = null;
		PreparedStatement prepVotes = null;		
		ResultSet rs = null;
		ResultSet rsVotes = null;

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			String sql = "SELECT status FROM consensus WHERE consid = ?;";

			prepCons = conn.prepareStatement(sql);
			prepCons.setInt(1, consid);

			rs = prepCons.executeQuery();
			
			if (!rs.next())
				return null;
			
			Consensus cons = new Consensus();
			cons.status = Consensus.getConsensusStatus(rs.getString("status"));
			
			sql = "SELECT COUNT(*) FROM consensus_votes WHERE consid = ?;";
			
			prepVotes = conn.prepareStatement(sql);
			prepVotes.setInt(1, consid);
			
			rsVotes = prepVotes.executeQuery();
			rsVotes.next();
			
			cons.nvotesForChange = rsVotes.getInt(1);

			conn.commit();
			
			return cons;
		} 
		catch (SQLException e) {
			logger.error("", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return null;
		}
		finally {
			if (prepCons != null)
				try { prepCons.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepVotes != null)
				try { prepVotes.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsVotes != null)
				try { rsVotes.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rs != null)
				try { rs.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}	
	}
	
	@Override
	public List<Message> getAllMessages() {
		Connection conn = null;
		PreparedStatement prepMsg = null;
		PreparedStatement prepEvent = null;		
		ResultSet rsMsg = null;
		ResultSet rsEvent = null;
		List<Message> res = new ArrayList<Message>();

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			String sql = "SELECT * FROM messages;";
			prepMsg = conn.prepareStatement(sql);
			rsMsg = prepMsg.executeQuery();
			
			while(rsMsg.next()) {
				Message m = new Message();
				m.id = rsMsg.getInt("msgid");
				m.username = rsMsg.getString("username");
				m.lat = rsMsg.getDouble("lat");
				m.lng = rsMsg.getDouble("lng");
				m.creationTime = rsMsg.getLong("creation_date");
				m.title = rsMsg.getString("title");
				m.content = rsMsg.getString("content");
				m.type = Message.stringToType(rsMsg.getString("type"));
				
				if (m.type == MessageType.EVENT) {
					sql = "SELECT * FROM events WHERE msgid = ?";
					prepEvent = conn.prepareStatement(sql);
					prepEvent.setInt(1, m.id);
					rsEvent = prepEvent.executeQuery();
					
					if (!rsEvent.next())
						continue;
					
					m.eventTime = rsEvent.getLong("event_date");
					m.capacity = rsEvent.getInt("capacity");
					m.registeredUsers = getEventRegistered(conn, m.id);
					m.consReqList = getEventConsensusList(conn, m.id);
				}
				
				res.add(m);
			}

			conn.commit();
			
			return res;
		} 
		catch (SQLException e) {
			logger.error("", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }

			return null;
		}
		finally {
			if (prepMsg != null)
				try { prepMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEvent != null)
				try { prepEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsEvent != null)
				try { rsEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (rsMsg != null)
				try { rsMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}
	}
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private int getVoteCount(Connection conn, int consid) {
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT COUNT(*) FROM consensus_votes WHERE consid = ?;"; 
			s = conn.prepareStatement(sql);
			s.setInt(1, consid);
			
			rs = s.executeQuery();

			if (!rs.next())
				throw new SQLException();

			int votesCount = rs.getInt(1);

			return votesCount;
		}
		catch (SQLException e) {
			logger.error("", e);
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}
			
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}
		
		return -1;
	}
	
	private int getCapacity(Connection conn, int eventid) {
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT capacity FROM events WHERE msgid = ?;";
			s = conn.prepareStatement(sql);
			s.setInt(1, eventid);


			rs = s.executeQuery();

			if (!rs.next())
				throw new SQLException();

			int capacity = rs.getInt(1);

			return capacity;
		}
		catch (SQLException e) {
			logger.error("", e);
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}

		return -1;
	}
	
	private int getRegisteredCount(Connection conn, int eventid) {
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT(*) FROM event_reg WHERE msgid = ?;";
			s = conn.prepareStatement(sql);
			s.setInt(1, eventid);
			
			rs = s.executeQuery();

			if (!rs.next())
				throw new SQLException();

			int nreg = rs.getInt(1);

			return nreg;
		}
		catch (SQLException e) {
			logger.error("", e);
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}

		return -1;		
	}
	
	private String getStatus(Connection conn, int consid) {
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT status FROM consensus WHERE consid = ?;";
			s = conn.prepareStatement(sql);
			s.setInt(1, consid);

			rs = s.executeQuery();

			if (!rs.next())
				throw new SQLException();

			String status = rs.getString(1);

			return status;
		}
		catch (SQLException e) {
			logger.error("", e);
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}

		return null;
	}
	
	private boolean toggleStatus(Connection conn, int consid) {
		String currentStatus = getStatus(conn, consid);
		String nextStatus = "";
		if (currentStatus.equals(Consensus.Status.ACCEPTED.toString()))
			nextStatus = Consensus.Status.NOT_ACCEPTED.toString();
		else
			nextStatus = Consensus.Status.ACCEPTED.toString();

		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			String sql = "UPDATE consensus SET status = ? WHERE consid = ?;";
			s = conn.prepareStatement(sql);
			s.setString(1, nextStatus);
			s.setInt(2, consid);

			if (s.executeUpdate() == 0)
				throw new SQLException();
		}
		catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}

		return true;
	}
	
	private boolean deleteCurrentVotes(Connection conn, int consid) {
		PreparedStatement s = null;

		try {
			String sql = "DELETE FROM consensus_votes WHERE consid = ?;";
			s = conn.prepareStatement(sql);
			s.setInt(1, consid);

			s.executeUpdate();
//			if (s.executeUpdate() == 0)
//				throw new SQLException();
		}
		catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}
		
		return true;
	}
	
	private int getLastInsertId(Connection conn) {
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT LAST_INSERT_ID();";
			s = conn.prepareStatement(sql);

			rs = s.executeQuery(); 
			if (!rs.next())
				throw new SQLException();
			
			return rs.getInt(1);
		}
		catch (SQLException e) {
			logger.error("", e);
			return -1;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}
	}
	
	private boolean isUserRegistered(Connection conn, String username, int eventid) {
		PreparedStatement s = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT(*) FROM event_reg WHERE username = ? AND msgid = ?;";
			s = conn.prepareStatement(sql);
			s.setString(1, username);
			s.setInt(2, eventid);

			rs = s.executeQuery(); 
			if (!rs.next())
				throw new SQLException();
						
			int reg = rs.getInt(1);
			if (reg > 0)
				return true;
		}
		catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}
		
		return false;
	}
	
	private boolean toggleConsensusReqIfNeeded(Connection conn, int consid, int eventid) throws SQLException {
		int votesCount = getVoteCount(conn, consid);
		int nregistered = getRegisteredCount(conn, eventid);

		if (votesCount < 0 || nregistered < 0) {
			throw new SQLException();
		}

		if (votesCount == nregistered && nregistered > 0) {
			toggleStatus(conn, consid);
			deleteCurrentVotes(conn, consid);
			return true;
		}
		
		return false;
	}
	
	private List<Message> getUserRegisteredEvents(Connection conn, String username) {
		PreparedStatement s = null;
		ResultSet rs = null;
		List<Message> res = new ArrayList<Message>();

		try {
			
			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("SELECT messages.msgid, messages.title FROM event_reg JOIN messages ON ");
			sqlsb.append("event_reg.msgid = messages.msgid ");
			sqlsb.append("WHERE event_reg.username = ?;");
			
			s = conn.prepareStatement(sqlsb.toString());
			s.setString(1, username);

			rs = s.executeQuery();
			while (rs.next()) {
				int msgid = rs.getInt("msgid");
				String title = rs.getString("title");
								
				res.add(new Message(msgid, title, "EVENT"));
			}
			
			return res;
		}
		catch (SQLException e) {
			logger.error("", e);
			return null;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}
	}
	
	private List<Integer> getEventConsensusIds(Connection conn, int eventid) {
		PreparedStatement s = null;
		ResultSet rs = null;
		List<Integer> res = new ArrayList<Integer>();

		try {
			String sql = "SELECT consid FROM consensus WHERE msgid = ?;";
			s = conn.prepareStatement(sql);
			s.setInt(1, eventid);

			rs = s.executeQuery();
			while (rs.next()) {
				res.add(rs.getInt(1));
			}
			
			return res;
		}
		catch (SQLException e) {
			logger.error("", e);
			return null;
		}
		finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { logger.error("", e); }
			}

			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}		
	}
	
	private List<Consensus> getEventConsensusList(Connection conn, int eventid) {
		PreparedStatement prepCons = null;
		PreparedStatement prepConsVotes = null;
		ResultSet consRs = null;
		ResultSet consVotesRs = null;
		List<Consensus> res = new ArrayList<Consensus>();

		try {
			String sql = "SELECT * FROM consensus WHERE msgid = ?;";
			prepCons = conn.prepareStatement(sql);
			prepCons.setInt(1, eventid);

			consRs = prepCons.executeQuery();
			while (consRs.next()) {
				Consensus cons = new Consensus();
				cons.id = consRs.getInt("consid");
				cons.eventid = eventid;
				cons.desc = consRs.getString("description");
				cons.status = Consensus.getConsensusStatus(consRs.getString("status"));
				
				sql = "SELECT COUNT(*) FROM consensus_votes WHERE consid = ? AND msgid = ?;";
				prepConsVotes = conn.prepareStatement(sql);
				prepConsVotes.setInt(1, cons.id);
				prepConsVotes.setInt(2, eventid);
				
				consVotesRs = prepConsVotes.executeQuery();
				if (!consVotesRs.next())
					return null;
				
				cons.nvotesForChange = consVotesRs.getInt(1);
				
				res.add(cons);
			}

			return res;
		}
		catch (SQLException e) {
			logger.error("", e);
			return null;
		}
		finally {
			if (prepCons != null) {
				try { prepCons.close(); } catch (SQLException e) { logger.error("", e); }
			}
			if (prepConsVotes != null) {
				try { prepCons.close(); } catch (SQLException e) { logger.error("", e); }
			}
			if (consRs != null) {
				try { consRs.close(); } catch (SQLException e) { logger.error("", e); }
			}
			if (consVotesRs != null) {
				try { consRs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}		
	}
	
	private List<String> getEventRegistered(Connection conn, int eventid) {
		PreparedStatement prep = null;
		ResultSet rs = null;
		List<String> res = new ArrayList<String>();

		try {
			String sql = "SELECT * FROM event_reg WHERE msgid = ?;";
			prep = conn.prepareStatement(sql);
			prep.setInt(1, eventid);

			rs = prep.executeQuery();
			while (rs.next()) {
				res.add(rs.getString("username"));
			}

			return res;
		}
		catch (SQLException e) {
			logger.error("", e);
			return null;
		}
		finally {
			if (prep != null) {
				try { prep.close(); } catch (SQLException e) { logger.error("", e); }
			}
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { logger.error("", e); }
			}
		}		
	}
}
