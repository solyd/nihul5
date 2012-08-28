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
		sb.append("creation_date DATETIME NOT NULL, ");
		sb.append(String.format("title VARCHAR(%d) NOT NULL, ", CONST.MSG_MAX_TITLE_LEN));
		sb.append(String.format("content VARCHAR(%d), ", CONST.MSG_MAX_CONTENT_LEN));
		sb.append("type VARCHAR(20) NOT NULL, ");
		sb.append("FOREIGN KEY (username) REFERENCES users(username) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE, ");
		sb.append("FOREIGN KEY (type) REFERENCES message_types(type) ");
		sb.append("ON UPDATE CASCADE ON DELETE CASCADE");
		sb.append(") ENGINE = InnoDB;");
		initStatements.add(sb.toString());

		// events TABLE
		// ++++++++++++++++++++++++++++++++++++++++
		sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS events (");
		sb.append("msgid BIGINT NOT NULL PRIMARY KEY, ");
		sb.append("event_date DATETIME NOT NULL, ");
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

			if (prep_s.executeUpdate() == 0) {
				logger.error("Error inserting new user to users table. user: " + user.toString());
				throw new SQLException();
			}

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

			String sql = "DELETE FROM users WHERE username = ?;";
			prep_s = conn.prepareStatement(sql);
			prep_s.setString(1, username);

			if (prep_s.executeUpdate() == 0) {
				logger.error("Error removing user: " + username);
				throw new SQLException();
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
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
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

			String sql = "SELECT * FROM users;";
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
	public boolean savePost(Post post) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		
		logger.info("Saving " + post.toString());
		
		try {
			conn = _dbcPool.getConnection();

			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO messages (username, lat, lng, creation_date, title, content, type) ");
			sqlsb.append("VALUES (?, ?, ?, ?, ?, ?, 'POST');");
			
			prep_s = conn.prepareStatement(sqlsb.toString());
			prep_s.setString(1, post.username);
			prep_s.setDouble(2, post.lat);
			prep_s.setDouble(3, post.lng);
			prep_s.setString(4, post.creationDateStr());
			prep_s.setString(5, post.title);
			prep_s.setString(6, post.content);

			if (prep_s.executeUpdate() == 0) {
				logger.error("Error saving post: " + post.toString());
				throw new SQLException();
			}
		} 
		catch (SQLException e) {
			logger.error("Exception during getUser", e);
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
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
	public boolean saveEvent(Event event) {
		Connection conn = null;
		PreparedStatement prepMsg = null;
		PreparedStatement prepEvent = null;
		PreparedStatement prepCons = null;
		
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
			prepMsg.setString(4, event.creationDateStr());
			prepMsg.setString(5, event.title);
			prepMsg.setString(6, event.content);

			if (prepMsg.executeUpdate() == 0) {
				logger.error("Error saving (inesrting into messages): " + event.toString());
				throw new SQLException();
			}
			
			// the id of the new message
			int last_insert_id = getLastInsertId(conn);
			if (last_insert_id < 0)
				throw new SQLException();
			
			// insert into events table
			// ++++++++++++++++++++++++++++++++++++++++
			sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO events (msgid, event_date, capacity) ");
			sqlsb.append("VALUES (?, ?, ?);");
			
			prepEvent = conn.prepareStatement(sqlsb.toString());
			prepEvent.setInt(1, last_insert_id);
			prepEvent.setString(2, event.eventDateStr());
			prepEvent.setInt(3, event.capacity);
			
			if (prepEvent.executeUpdate() == 0) {
				logger.error("Error saving (inserting into events): " + event.toString());
				throw new SQLException();
			}
			
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
			
			int[] res = prepCons.executeBatch();
			for (int i = 0, len = res.length; i < len; ++i) {
				if (res[i] == 0){
					logger.error("Error saving (consensus req): " + event.toString());
					throw new SQLException();
				}
			}
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception during getUser", e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return false;
		}
		finally {
			if (prepMsg != null)
				try { prepMsg.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepCons != null)
				try { prepCons.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (prepEvent != null)
				try { prepEvent.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return true;
	}

	@Override
	public boolean deleteMessage(int msgid) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		
		logger.info("Deleting msg: " + msgid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			String sql = "DELETE FROM messages WHERE msgid = ?;";
			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, msgid);

			if (prep_s.executeUpdate() == 0) {
				logger.error("Error removing message with id: " + msgid);
				throw new SQLException();
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
	public boolean saveEventRegistration(int eventid, String username) {
		Connection conn = null;
		PreparedStatement prep_s = null;
		
		logger.info("Saving event registration for event " + eventid + " by " + username);

		try {
			conn = _dbcPool.getConnection();

			String sql = "INSERT INTO event_reg (msgid, username) VALUES (?, ?);";
			prep_s = conn.prepareStatement(sql);
			prep_s.setInt(1, eventid);
			prep_s.setString(2, username);

			if (prep_s.executeUpdate() == 0) {
				logger.error(String.format("Error saving registration for event : %d by %s", eventid, username));
				throw new SQLException();
			}
		} 
		catch (SQLException e) {
			logger.error(String.format("Exception when saving registration for event : %d by %s", eventid, username));
//			if (conn != null)
//				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
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
	public boolean deleteEventRegistration(int eventid, String username) {
		Connection conn = null;
		PreparedStatement prepEventReg = null;
		PreparedStatement prepConsVotes = null;
		PreparedStatement prepCons = null;
		ResultSet rs = null;

		logger.info("deleting event registration by " + username + "to event " + eventid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

			// delete from registration table
			// ++++++++++++++++++++++++++++++++++++++++
			String sql = "DELETE FROM event_reg WHERE msgid = ? AND username = ?;";
			prepEventReg = conn.prepareStatement(sql);
			prepEventReg.setInt(1, eventid);
			prepEventReg.setString(2, username);

			prepEventReg.executeUpdate();
//			if (prepEventReg.executeUpdate() == 0) {
//				logger.error(String.format("Error canceling registration for event : %d by %s", eventid, username));
//				throw new SQLException();
//			}
			
			// delete all casted votes for this event by the user
			// ++++++++++++++++++++++++++++++++++++++++
			sql = "DELETE FROM consensus_votes WHERE msgid = ? AND username = ?;";
			prepConsVotes = conn.prepareStatement(sql);
			prepConsVotes.setInt(1, eventid);
			prepConsVotes.setString(2, username);
			
			prepConsVotes.executeUpdate();
//			if (prepConsVotes.executeUpdate() == 0) {
//				logger.error(String.format("Error canceling registration for event : %d by %s", eventid, username));
//				throw new SQLException();
//			}
			
			// now check if the status should be toggled for any of the consensus reqs of the event
			// ++++++++++++++++++++++++++++++++++++++++
			sql = "SELECT consid FROM consensus WHERE msgid = ?;";
			prepCons = conn.prepareStatement(sql);
			prepCons.setInt(1, eventid);
			
			rs = prepCons.executeQuery();
			while (rs.next()) {
				int consid = rs.getInt("consid");
				int votesCount = getVoteCount(conn, consid, eventid);
				int nregistered = getRegisteredCount(conn, eventid);
				
				// TODO  
				if (votesCount < 0 || nregistered < 0) {
					continue;
				}
				
				if (votesCount == nregistered && nregistered > 0) {
					toggleStatus(conn, consid);
					deleteCurrentVotes(conn, consid);
				}
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
		
		logger.info(username + "votes on consensus req " + reqid + " of event " + eventid);

		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			// The user isn't registered to the event, can't vote on this consensus req
			if (!isUserRegistered(conn, username, eventid))
				return false;

			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("INSERT INTO consensus_votes (consid, msgid, username) ");
			sqlsb.append("VALUES (?, ?, ?);");
			
			prep_s = conn.prepareStatement(sqlsb.toString());
			prep_s.setInt(1, reqid);
			prep_s.setInt(2, eventid);
			prep_s.setString(3, username);
			
			if (prep_s.executeUpdate() == 0)
				throw new SQLException();

			int votesCount = getVoteCount(conn, reqid, eventid);
			int nregistered = getRegisteredCount(conn, eventid);

			// TODO  
			if (votesCount < 0 || nregistered < 0) {
				throw new SQLException();
			}

			if (votesCount == nregistered && nregistered > 0) {
				toggleStatus(conn, reqid);
				deleteCurrentVotes(conn, reqid);
			}
			
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
	public List<Message> getUserMessages(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private int getVoteCount(Connection conn, int consid, int eventid) {
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT COUNT(*) FROM consensus_votes WHERE msgid = ? AND consid = ?;"; 
			s = conn.prepareStatement(sql);
			s.setInt(1, eventid);
			s.setInt(2, consid);
			
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
}
