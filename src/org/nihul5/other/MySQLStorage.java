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
			initStatements.add("DROP TABLE IF EXISTS user_roles;");
			initStatements.add("DROP TABLE IF EXISTS users;");
			initStatements.add("DROP TABLE IF EXISTS roles;");
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
		
		// TODO remove
//		if (CONST.DEBUG_MODE) {
//			sb = new StringBuilder();
//			sb.append("INSERT IGNORE INTO users (username, password, first_name, last_name, email) ");
//			sb.append("VALUES ('sol', 'sol', null, null, null);");
//			initStatements.add(sb.toString());
//		}
		
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
		
		// TODO remove
//		if (CONST.DEBUG_MODE) {
//			sb = new StringBuilder();
//			sb.append("INSERT IGNORE INTO user_roles (username, role) VALUES ('sol', 'user');");
//			initStatements.add(sb.toString());
//		}
		
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
	public StorageResponse addUser(User user) {
		if (user == null)
			throw new IllegalArgumentException("user is null");
		
		logger.info("Adding new user to DB:\n\t" + user.toString());
		
		Connection conn = null;
		PreparedStatement s1 = null;
		PreparedStatement s2 = null;
		PreparedStatement s3 = null;
		String sql;
		try {
			conn = _dbcPool.getConnection();
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			sql = "SELECT * FROM users WHERE username = ?;";
			s1 = (PreparedStatement) conn.prepareStatement(sql);
			s1.setString(1, user.username);
			
			ResultSet rs = s1.executeQuery();
			if (rs.next()) {
				logger.info("User " + user.toString() + " already exists");
				return StorageResponse.ADDUSER_EXISTS;
			}
			
			// ++++++++++++++++++++++++++++++++++++++++
			
			sql = "INSERT INTO users (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?);";
			s2 = (PreparedStatement) conn.prepareStatement(sql);
			String md5password = user.md5password();
			if (md5password == null)
				return StorageResponse.ADDUSER_FAILED;
			
			s2.setString(1, user.username);
			s2.setString(2, md5password);
			s2.setString(3, user.firstName);
			s2.setString(4, user.lastName);
			s2.setString(5, user.email);
			
			if (s2.executeUpdate() == 0) {
				logger.error("Error inserting new user to users table. user: " + user.toString());
				return StorageResponse.ADDUSER_FAILED;
			}
			
			// ++++++++++++++++++++++++++++++++++++++++
			
			sql = "INSERT INTO user_roles (username, role) VALUES (?, 'user');";
			s3 = (PreparedStatement) conn.prepareStatement(sql);
			s3.setString(1, user.username);
			
			if (s3.executeUpdate() == 0) {
				logger.error("Error inserting new user to user_roles table. user: " + user.toString());
				return StorageResponse.ADDUSER_FAILED;
			}
			
			conn.commit();
		} 
		catch (SQLException e) {
			logger.error("Exception while adding user: " + user.toString(), e);
			if (conn != null)
				try { conn.rollback(); } catch (SQLException e1) { logger.error("Can't roll back", e1); }
			
			return StorageResponse.ADDUSER_FAILED;
		}
		finally {
			if (s1 != null)
				try { s1.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (s2 != null)
				try { s2.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (s3 != null)
				try { s3.close(); } catch (SQLException e) { logger.error("Can't close statement", e); }
			if (conn != null)
				try { conn.close(); } catch (SQLException e) { logger.error("Can't close DB connection", e); }
		}

		return StorageResponse.ADDUSER_OK;
	}

	@Override
	public StorageResponse removeUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers(Predicate<User> userFilter) {
		// TODO Auto-generated method stub
		return null;
	}

}
