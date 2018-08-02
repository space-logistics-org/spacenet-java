/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mit.spacenet.data;


/**
 * Saves a database connection for later use.
 */
public class SavedOnlineDataSource implements I_SavedDataSource {
	private String host, user, password, database;
	private Integer port;
	private boolean passwordSaved;
	
	/**
	 * Instantiates a new saved database connection.
	 */
	public SavedOnlineDataSource() { }
	
	/**
	 * Instantiates a new saved database connection.
	 * 
	 * @param host the host
	 * @param port the port
	 * @param database the database
	 * @param user the user
	 * @param password the password
	 */
	public SavedOnlineDataSource(String host, Integer port, String database, 
			String user, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		if(passwordSaved) this.password = password;
	}
	
	/**
	 * Instantiates a new saved database connection.
	 * 
	 * @param host the host
	 * @param database the database
	 * @param user the user
	 * @param password the password
	 */
	public SavedOnlineDataSource(String host, String database, String user, 
			String password) {
		this(host, null, database, user, password);
	}
	
	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Sets the host.
	 * 
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 * 
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the database.
	 * 
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}
	
	/**
	 * Sets the database.
	 * 
	 * @param database the new database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	
	/**
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 * 
	 * @param port the new port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Checks if is password saved.
	 * 
	 * @return true, if is password saved
	 */
	public boolean isPasswordSaved() {
		return passwordSaved;
	}

	/**
	 * Sets the password saved. If password is changed from saved to not saved,
	 * any existing password is removed.
	 * 
	 * @param passwordSaved the new password saved
	 */
	public void setPasswordSaved(boolean passwordSaved) {
		this.passwordSaved = passwordSaved;
		if(!passwordSaved) setPassword(null);
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.I_SavedDataSource#getDataSourceType()
	 */
	public DataSourceType getDataSourceType() {
		return DataSourceType.SQL_DB;
	}
}
