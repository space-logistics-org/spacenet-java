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
package edu.mit.spacenet.domain.network.edge;

import java.io.Serializable;

/**
 * Enumeration of the two types of propulsive burns (OMS for Orbit Maneuvering System, RCS for
 * Reaction Control System).
 * 
 * @author Paul Grogan
 */
public enum BurnType implements Serializable {

  /** Abbreviation for Orbit Maneuvering System. */
  OMS("OMS"),

  /** Abbreviation for Reaction Control System. */
  RCS("RCS");

  private String name;

  /**
   * The default constructor.
   * 
   * @param name the name of the burn type to display
   */
  private BurnType(String name) {
    this.name = name;
  }

  /**
   * Gets the name of the burn type.
   * 
   * @return the burn type name
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
   * Method to get a particular instance of a state type based on a case-insensitive string.
   * 
   * @param name case-insensitive string of name
   * 
   * @return the burn type, returns RCS if unknown name
   */
  public static BurnType getInstance(String name) {
    if (name.toLowerCase().equals(OMS.getName().toLowerCase()))
      return OMS;
    else
      return RCS;
  }
}
