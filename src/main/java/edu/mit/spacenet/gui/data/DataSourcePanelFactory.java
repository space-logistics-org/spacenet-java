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
package edu.mit.spacenet.gui.data;

import edu.mit.spacenet.data.Database;
import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.data.Spreadsheet_2_5;

/**
 * An abstract class to contain the factory method to create data source panels.
 * 
 * @author Paul Grogan
 */
public abstract class DataSourcePanelFactory {
	
	/**
	 * Factory method to create a data source panel based on the data source
	 * type.
	 * 
	 * @param dialog the data source dialog
	 * @param dataSource the data source
	 * 
	 * @return the data source panel
	 */
	public static AbstractDataSourcePanel createDataSourcePanel(DataSourceDialog dialog, I_DataSource dataSource) {
		if(dataSource==null) return new NoDataSourcePanel(dialog, dataSource);
		
		switch(dataSource.getDataSourceType()) {
		case EXCEL_2_5:
			return new ExcelDataSourcePanel(dialog, (Spreadsheet_2_5)dataSource);
		case SQL_DB:
			return new MySqlDataSourcePanel(dialog, (Database)dataSource);
		default: throw new RuntimeException("Unsupported Data Source");
		}
	}
}
