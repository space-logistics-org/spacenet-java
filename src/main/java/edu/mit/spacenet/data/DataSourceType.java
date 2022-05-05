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
  EXCEL_2_5("SpaceNet 2.5 (Excel)", "icons/page_white_excel.png"),
  // EXCEL_1_3("SpaceNet 1.3 (Excel)", Spreadsheet_1_3.class, "icons/page_white_excel.png");
  // , ONLINE("SpaceNet Online", SpaceNetOnline.class, "icons/database.png");

  /** An in-memory database */
  IN_MEMORY("In-memory", "icons/database.png");

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

  /*
   * (non-Javadoc)
   * 
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
