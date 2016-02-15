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

import javax.swing.ImageIcon;

/**
 * An enumeration which lists the different types of data sources.
 * 
 * @author Paul Grogan
 */
public enum DataSourceType {
	/** No data source. */
	NONE("None", "icons/database.png"),
	
	/** A SQL-based database. */
	SQL_DB("SpaceNet 2.5 (SQL Database)", "icons/database_yellow.png"),
	
	/** An excel-based spreadsheet database */
	EXCEL_2_5("SpaceNet 2.5 (Excel)", "icons/page_white_excel.png");
	//EXCEL_1_3("SpaceNet 1.3 (Excel)", Spreadsheet_1_3.class, "icons/page_white_excel.png");
	//, ONLINE("SpaceNet Online", SpaceNetOnline.class, "icons/database.png"); 
	
	private String name;
	private ImageIcon icon;
	
	private DataSourceType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the data source type.
	 * 
	 * @return the data source type name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() { 
		return name;
	}
	
	/**
	 * Gets the icon of the data source type.
	 * 
	 * @return the data source type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
}
