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
package edu.mit.spacenet.domain.element;

import javax.swing.ImageIcon;

/**
 * Enumeration that defines the four operational states for elements.
 * 
 * @author Paul Grogan
 */
public enum StateType {

  /**
   * The active state type is used for states where the element is in a typical or nominal active
   * condition.
   */
  ACTIVE("Active", "icons/clock_play.png"),

  /**
   * The special state type is used for states where the element is in a special active condition.
   */
  SPECIAL("Special", "icons/clock_add.png"),

  /**
   * The quiescent state type is used for states where the element is only operational part of the
   * time (duty cycle < 1).
   */
  QUIESCENT("Quiescent", "icons/clock_pause.png"),

  /**
   * The dormant state type is used for states where the element is operational for a small
   * percentage of the time (duty cycle << 1).
   */
  DORMANT("Dormant", "icons/clock_stop.png"),

  /**
   * The decommissioned state type is used for states where the element is not operational and will
   * not become operational in the future. This state type is reserved for cannibalization of spare
   * parts.
   */
  DECOMMISSIONED("Decommissioned", "icons/clock_red.png");

  private String name;
  private ImageIcon icon;

  private StateType(String name, String iconUrl) {
    this.name = name;
    this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
  }

  /**
   * Gets the textual representation of the state type.
   * 
   * @return the state type name
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Gets the icon representation of the state type.
   * 
   * @return the state type icon
   */
  public ImageIcon getIcon() {
    return icon;
  }

  /**
   * Method to get a particular instance of a state type based on a case-insensitive string.
   * 
   * @param name case-insensitive string of name
   * 
   * @return the state type, returns null if unmatched
   */
  public static StateType getInstance(String name) {
    for (StateType t : values()) {
      if (t.getName().toLowerCase().equals(name.toLowerCase()))
        return t;
    }
    return null;
  }
}
