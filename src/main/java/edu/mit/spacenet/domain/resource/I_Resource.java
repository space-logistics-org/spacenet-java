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

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_DomainType;

/**
 * Interface that represents any supply item (discrete or continuous).
 * 
 * @author Paul Grogan
 */
public interface I_Resource extends I_DomainType, Comparable<I_Resource> {

  /**
   * Gets the class of supply.
   * 
   * @return the class of supply
   */
  public ClassOfSupply getClassOfSupply();

  /**
   * Sets the class of supply.
   * 
   * @param classOfSupply the class of supply
   */
  public void setClassOfSupply(ClassOfSupply classOfSupply);

  /**
   * Gets the resource environment.
   * 
   * @return the environment
   */
  public Environment getEnvironment();

  /**
   * Sets the resource environment.
   * 
   * @param environment the environment
   */
  public void setEnvironment(Environment environment);

  /**
   * Gets the packing factor, which is the factor of additional COS 5 demands that is added to
   * unpacked demand figures.
   * 
   * @return the packing factor
   */
  public double getPackingFactor();

  /**
   * Sets the packing factor, which is the factor of additional COS 5 demands that is added to
   * unpacked demand figures.
   * 
   * @param factor the packing factor
   */
  public void setPackingFactor(double factor);

  /**
   * Gets the units of measure.
   * 
   * @return the units of measure
   */
  public String getUnits();

  /**
   * Sets the units of measure.
   * 
   * @param units the units of measure
   */
  public void setUnits(String units);

  /**
   * Gets the unit mass.
   * 
   * @return the unit mass (kilograms per unit of consumption)
   */
  public double getUnitMass();

  /**
   * Sets the unit mass.
   * 
   * @param unitMass the unit mass
   */
  public void setUnitMass(double unitMass);

  /**
   * Gets the unit volume.
   * 
   * @return the unit volume (cubic meters per unit)
   */
  public double getUnitVolume();

  /**
   * Sets the unit volume.
   * 
   * @param unitVolume the unit volume (cubic meters per unit)
   */
  public void setUnitVolume(double unitVolume);

  /**
   * Checks whether this resource is substitutable (same or higher level of fidelity) for a given
   * resource during demand consumption.
   * 
   * @param resource the resource to check for substitutability
   * 
   * @return true if this can be substituted for resource, false otherwise
   */
  public boolean isSubstitutableFor(I_Resource resource);

  /**
   * Gets the resource type.
   * 
   * @return the resource type
   */
  public ResourceType getResourceType();
}
