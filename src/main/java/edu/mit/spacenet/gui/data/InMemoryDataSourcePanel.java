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
package edu.mit.spacenet.gui.data;

import edu.mit.spacenet.data.InMemoryDataSource;

/**
 * A data source panel for in-memory data sources.
 * 
 * @author Paul Grogan
 */
public class InMemoryDataSourcePanel extends AbstractDataSourcePanel {
	private static final long serialVersionUID = 2234336773732183925L;
	
	private InMemoryDataSource dataSource;
	
	/**
	 * Instantiates a new in-memory data source panel.
	 * 
	 * @param dialog the dialog
	 * @param dataSource the data source
	 */
	protected InMemoryDataSourcePanel(DataSourceDialog dialog, InMemoryDataSource dataSource) {
		super(dialog, dataSource);
		this.dataSource = dataSource;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#getDataSource()
	 */
	@Override
	public InMemoryDataSource getDataSource() {
		return dataSource;
	}
	

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#canLoad()
	 */
	@Override
	public boolean canLoad() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#saveData()
	 */
	@Override
	public void saveData() {
		return;
	}
}
