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
 * Saves a spreadsheet location for future use.
 */
public class SavedSpreadsheetDataSource implements I_SavedDataSource {
	private String filePath;
	
	/**
	 * Instantiates a new saved spreadsheet data source.
	 */
	public SavedSpreadsheetDataSource() { }
	
	/**
	 * Instantiates a new saved spreadsheet data source.
	 * 
	 * @param filePath the file path
	 */
	public SavedSpreadsheetDataSource(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Gets the file path.
	 * 
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Sets the file path.
	 * 
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.I_SavedDataSource#getDataSourceType()
	 */
	public DataSourceType getDataSourceType() {
		return DataSourceType.EXCEL_2_5;
	}
}
