package org.nihul5.other;

import java.util.List;

public interface Storage {
	
	public enum StorageResponse {
		ADDUSER_OK,
		ADDUSER_EXISTS,
		ADDUSER_FAILED,
	}
	
	void init();
	
	/**
	 * Stores the user detail in the DB
	 * @param user User details
	 * @return true on success
	 */
	StorageResponse addUser(User user);
	
	StorageResponse removeUser(String username);
	
	List<User> getUsers();
	
	User getUser(String username);
	
	/**
	 * Returns a list of users that is at most 'limit' and starts with
	 * offset of 'offset' from the start of the users table. 
	 * @param offset 
	 * @param limit
	 * @return
	 */
	List<String> getUserNames(int offset, int limit);
}
