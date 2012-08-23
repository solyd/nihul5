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
	
	List<User> getUsers(Predicate<User> userFilter);
}
