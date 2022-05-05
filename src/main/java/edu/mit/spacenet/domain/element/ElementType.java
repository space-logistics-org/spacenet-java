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


/**
 * The element type enumeration lists the different types of elements, typically one per element
 * subclass. The element type provides a default name and a default icon.
 * 
 * @author Paul Grogan
 */
public enum ElementType {

  /** Used for elements of the Element class. */
  ELEMENT("Element", ElementIcon.BRICK),

  /** Used for elements of the CrewMember class. */
  CREW_MEMBER("Crew Member", ElementIcon.USER),

  /** Used for elements of the ResourceContainer class. */
  RESOURCE_CONTAINER("Resource Container", ElementIcon.PACKAGE),

  /** Used for elements of the ResourceTank class. */
  RESOURCE_TANK("Resource Tank", ElementIcon.PACKAGE),

  /** Used for elements of the Carrier class. */
  CARRIER("Carrier", ElementIcon.LORRY),

  /** Used for elements of the PropulsiveVehicle class. */
  PROPULSIVE_VEHICLE("Propulsive Vehicle", ElementIcon.BRICK),

  /** Used for elements of the SurfaceVehicle class. */
  SURFACE_VEHICLE("Surface Vehicle", ElementIcon.CAR);

  private String name;
  private ElementIcon icon;

  private ElementType(String name, ElementIcon icon) {
    this.name = name;
    this.icon = icon;
  }

  /**
   * Gets the text representation of the element type.
   * 
   * @return the element type name
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
   * Gets the icon used as the default for elements of this type.
   * 
   * @return the default icon
   */
  public ElementIcon getIconType() {
    return icon;
  }

  /**
   * Gets an element type instance based on a give name.
   * 
   * @param name the element type name to match
   * 
   * @return the element type, null if no match was found.
   */
  public static ElementType getInstance(String name) {
    for (ElementType t : ElementType.values()) {
      if (t.getName().toLowerCase().equals(name.toLowerCase())) {
        return t;
      }
    }
    return null;
  }
}
