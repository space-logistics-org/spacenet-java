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
package edu.mit.spacenet.domain;

import java.util.SortedSet;

import edu.mit.spacenet.domain.element.I_Element;

/**
 * The interface for any object that can contain items.
 * 
 * @author Paul Grogan
 */
public interface I_Container extends I_DomainType {

  /**
   * Returns the contents of this container.
   * 
   * @return the set of items
   */
  public SortedSet<I_Element> getContents();

  /**
   * Gets the complete contents of a location, including any nested elements at any level.
   * 
   * @return the set of elements
   */
  public SortedSet<I_Element> getCompleteContents();

  /**
   * Gets whether an element can be added.
   * 
   * @param element the element
   * 
   * @return true, if successful
   */
  public boolean canAdd(I_Element element);

  /**
   * Gets whether mass can be added to a nested element.
   * 
   * @param addedMass the added mass
   * 
   * @return true, if successful
   */
  public boolean canAdd(double addedMass);

  /**
   * Adds the item to the container.
   * 
   * @param element the element to add
   * 
   * @return true, if successful
   */
  public boolean add(I_Element element);

  /**
   * Removes the element from the container.
   * 
   * @param element the element to add
   * 
   * @return true, if successful
   */
  public boolean remove(I_Element element);

  /**
   * Gets the current cargo mass.
   * 
   * @return the cargo mass (kilograms)
   */
  public double getCargoMass();

  /**
   * Gets the total cargo mass including any nested elements and resources.
   * 
   * @return the total cargo mass (kilograms)
   */
  public double getTotalMass();

  /**
   * Gets the current number of crew members.
   * 
   * @return the number of crew members
   */
  public int getCrewSize();

  /**
   * Gets the total crew size including any nested elements.
   * 
   * @return the total number of crew members
   */
  public int getTotalCrewSize();

  /**
   * Gets the total volume of cargo elements.
   * 
   * @return the cargo volume (cubic meters)
   */
  public double getCargoVolume();
}
