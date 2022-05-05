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

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_Container;

/**
 * The I_Carrier interface represents objects that implement both the I_Element and the I_Container
 * interfaces (that is, elements that can contain other elements).
 * 
 * @author Paul Grogan
 */
public interface I_Carrier extends I_Element, I_Container {

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
   * Gets the maximum crew size.
   * 
   * @return the maximum crew size
   */
  public int getMaxCrewSize();

  /**
   * Sets the maximum crew size.
   * 
   * @param maxCrewSize the maximum crew size
   */
  public void setMaxCrewSize(int maxCrewSize);
}
