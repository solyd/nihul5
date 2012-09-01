package org.nihul5.other;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class User {
	private static final Logger logger = Logger.getLogger(User.class);
	
	public String username;
	public String password;
	public String firstName;
	public String lastName;
	public String email;
	
	public User() {} 
	
	public User(String username, String password, String firstName, String lastName, String email) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public User(String username, String firstName, String lastName, String email) {
		this.username = username;
		this.password = "";
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return String.format("(%s, %s, %s, %s, %s)", username, password, firstName, lastName, email);
	}
	
	public String md5password() {
		try {
			return Utility.MD5(password);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Can't compute MD5 of password", e);
			return null;
		}
	}
}
