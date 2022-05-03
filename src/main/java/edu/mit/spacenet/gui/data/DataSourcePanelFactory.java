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

import edu.mit.spacenet.data.Database;
import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.data.InMemoryDataSource;
import edu.mit.spacenet.data.Spreadsheet_2_5;

/**
 * An abstract class to contain the factory method to create data source panels.
 * 
 * @author Paul Grogan
 */
public abstract class DataSourcePanelFactory {

  /**
   * Factory method to create a data source panel based on the data source type.
   * 
   * @param dialog the data source dialog
   * @param dataSource the data source
   * 
   * @return the data source panel
   */
  public static AbstractDataSourcePanel createDataSourcePanel(DataSourceDialog dialog,
      I_DataSource dataSource) {
    if (dataSource == null)
      return new NoDataSourcePanel(dialog, dataSource);

    switch (dataSource.getDataSourceType()) {
      case EXCEL_2_5:
        return new ExcelDataSourcePanel(dialog, (Spreadsheet_2_5) dataSource);
      case SQL_DB:
        return new MySqlDataSourcePanel(dialog, (Database) dataSource);
      case IN_MEMORY:
        return new InMemoryDataSourcePanel(dialog, (InMemoryDataSource) dataSource);
      default:
        throw new RuntimeException("Unsupported Data Source");
    }
  }
}
