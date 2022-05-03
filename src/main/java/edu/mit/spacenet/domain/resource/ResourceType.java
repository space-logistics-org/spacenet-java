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
package edu.mit.spacenet.domain.resource;

import javax.swing.ImageIcon;

/**
 * An enumeration for the different subclasses of resources.
 * 
 * @author Paul Grogan
 */
public enum ResourceType {

  /** The generic resource type. */
  GENERIC("Generic", "icons/bullet_pink.png"),

  /** The continuous resource type. */
  RESOURCE("Continuous", "icons/bullet_blue.png"),

  /** The discrete resource type. */
  ITEM("Discrete", "icons/bullet_wrench.png");

  private String name;
  private ImageIcon icon;

  private ResourceType(String name, String iconUrl) {
    this.name = name;
    this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
  }

  /**
   * Gets the name of the resource type.
   * 
   * @return the resource type name
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
   * Gets the icon of the resource type.
   * 
   * @return the resource type icon
   */
  public ImageIcon getIcon() {
    return icon;
  }

  /**
   * Gets the resource type instance based on a passed name.
   * 
   * @param name the resource type name
   * 
   * @return the resource type, null if not found
   */
  public static ResourceType getInstance(String name) {
    for (ResourceType t : ResourceType.values()) {
      if (t.getName().toLowerCase().equals(name.toLowerCase())) {
        return t;
      }
    }
    return null;
  }
}
