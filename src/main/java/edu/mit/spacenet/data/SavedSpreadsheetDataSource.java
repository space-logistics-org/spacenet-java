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
