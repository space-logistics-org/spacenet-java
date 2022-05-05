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

import java.util.SortedMap;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;

/**
 * The I_ResourceContainer represents elements that can contain resources.
 * 
 * @author Paul Grogan
 */
public interface I_ResourceContainer extends I_Element {

  /**
   * Determines if the resource can be added.
   * 
   * @param resource the resource to add
   * @param amount the amount to add
   * 
   * @return true if it can be added, false otherwise
   */
  public boolean canAdd(I_Resource resource, double amount);

  /**
   * Attempts to add an amount of a resource to the container.
   * 
   * @param resource the resource to add
   * @param amount the amount to add
   * 
   * @return true if successful, false otherwise
   */
  public boolean add(I_Resource resource, double amount);

  /**
   * Attempts to remove an amount of a resource from the container.
   * 
   * @param resource the resource to remove
   * @param amount the amount to remove
   * 
   * @return true if successful, false otherwise
   */
  public boolean remove(I_Resource resource, double amount);

  /**
   * Gets the cargo environment.
   * 
   * @return the cargo environment
   */
  public Environment getCargoEnvironment();

  /**
   * Sets the cargo environment.
   * 
   * @param cargoEnvironment the cargo environment
   */
  public void setCargoEnvironment(Environment cargoEnvironment);

  /**
   * Gets the maximum cargo mass.
   * 
   * @return the maximum cargo mass (kilograms)
   */
  public double getMaxCargoMass();

  /**
   * Sets the maximum cargo mass.
   * 
   * @param maxCargoMass the maximum cargo mass (kilograms)
   */
  public void setMaxCargoMass(double maxCargoMass);

  /**
   * Gets the maximum cargo volume.
   * 
   * @return the maximum volume (cubic meters)
   */
  public double getMaxCargoVolume();

  /**
   * Sets the maximum cargo volume.
   * 
   * @param maxCargoVolume the maximum volume (cubic meters)
   */
  public void setMaxCargoVolume(double maxCargoVolume);

  /**
   * Gets the total volume of cargo items.
   * 
   * @return the cargo volume (cubic meters)
   */
  public double getCargoVolume();

  /**
   * Gets the total mass of cargo items.
   * 
   * @return the cargo mass (kilograms)
   */
  public double getCargoMass();

  /**
   * Gets the contents of the container.
   * 
   * @return the contents
   */
  public SortedMap<I_Resource, Double> getContents();
}
