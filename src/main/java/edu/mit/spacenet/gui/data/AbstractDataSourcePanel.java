/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
