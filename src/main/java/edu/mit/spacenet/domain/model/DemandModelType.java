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
package edu.mit.spacenet.domain.model;

import javax.swing.ImageIcon;

/**
 * An enumeration for the demand model types.
 * 
 * @author Paul Grogan
 */
public enum DemandModelType {

  /** The crew consumables model type. */
  CREW_CONSUMABLES("Crew Consumables Demand Model", "icons/comment.png"),

  /** The timed impulse model type. */
  TIMED_IMPULSE("Timed Impulse Demand Model", "icons/comment.png"),

  /** The rated model type. */
  RATED("Rated Demand Model", "icons/comment.png"),

  /** The sparing by mass model type. */
  SPARING_BY_MASS("Sparing by Mass Demand Model", "icons/comment.png");

  private String name;
  private ImageIcon icon;

  private DemandModelType(String name, String iconUrl) {
    this.name = name;
    this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
  }

  /**
   * Gets the name of the demand model type.
   * 
   * @return the demand model type name
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
   * Gets the icon for the demand model type.
   * 
   * @return the demand model type icon
   */
  public ImageIcon getIcon() {
    return icon;
  }

  /**
   * Gets the enumeration value based on a passed name.
   * 
   * @param name the name to match
   * 
   * @return the matching demand model type (null if no match found)
   */
  public static DemandModelType getInstance(String name) {
    for (DemandModelType t : DemandModelType.values()) {
      if (t.getName().toLowerCase().equals(name.toLowerCase())) {
        return t;
      }
    }
    return null;
  }
}
