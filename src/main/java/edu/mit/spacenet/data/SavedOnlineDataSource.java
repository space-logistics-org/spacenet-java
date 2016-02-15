/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
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
