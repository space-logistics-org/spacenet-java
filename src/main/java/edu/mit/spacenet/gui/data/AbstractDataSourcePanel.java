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

import javax.swing.JPanel;

import edu.mit.spacenet.data.I_DataSource;

/**
 * Abstract panel to serve as an interface to the data source panels.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractDataSourcePanel extends JPanel {
	private static final long serialVersionUID = 1391703151715305906L;
	
	private DataSourceDialog dataSourceDialog;
	
	/**
	 * The constructor.
	 * 
	 * @param dataSourceDialog the data source dialog component
	 * @param dataSource the data source
	 */
	public AbstractDataSourcePanel(DataSourceDialog dataSourceDialog, I_DataSource dataSource) {
		this.dataSourceDialog = dataSourceDialog;
	}
	
	/**
	 * Requests the panel to save any inputs for the data source.
	 */
	public abstract void saveData();
	
	/**
	 * Load and update view.
	 */
	public void loadAndUpdateView() {
		dataSourceDialog.loadDataSource();
		dataSourceDialog.updateTables();
	}
	
	/**
	 * Gets the data source dialog.
	 * 
	 * @return the data source dialog
	 */
	public DataSourceDialog getDialog() {
		return dataSourceDialog;
	}
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public abstract I_DataSource getDataSource();
	
	/**
	 * Gets whether the data source can be loaded.
	 * 
	 * @return whether the data source can be loaded
	 */
	public abstract boolean canLoad();
}