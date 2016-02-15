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

import edu.mit.spacenet.data.I_DataSource;

/**
 * A data source panel to display when no data source is present (blank).
 * 
 * @author Paul Grogan
 */
public class NoDataSourcePanel extends AbstractDataSourcePanel {
	private static final long serialVersionUID = 2234336773732183925L;
	
	/**
	 * Instantiates a new no data source panel.
	 * 
	 * @param dialog the dialog
	 * @param dataSource the data source
	 */
	protected NoDataSourcePanel(DataSourceDialog dialog, I_DataSource dataSource) {
		super(dialog, dataSource);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#saveData()
	 */
	@Override
	public void saveData() {}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#getDataSource()
	 */
	@Override
	public I_DataSource getDataSource() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#canLoad()
	 */
	@Override
	public boolean canLoad() {
		return false;
	}
}
