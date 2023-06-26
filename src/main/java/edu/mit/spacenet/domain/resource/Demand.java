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

import java.text.DecimalFormat;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * Represents a demanded resource and the demanded amount.
 * 
 * @author Paul Grogan
 */
public class Demand implements Comparable<Demand>, Cloneable {
  private I_Resource resource;
  private double amount;

  /**
   * The default constructor.
   */
  public Demand() {

  }

  /**
   * Gets the mass of the associated demand.
   * 
   * @return the mass of the associated demand (kilograms)
   */
  public double getMass() {
    if (resource == null)
      return 0;
    else
      return GlobalParameters.getSingleton().getRoundedMass(resource.getUnitMass() * amount);
  }

  /**
   * Gets the volume of the associated demand.
   * 
   * @return the volume of the associated demand (cubic meters)
   */
  public double getVolume() {
    if (resource == null)
      return 0;
    else
      return GlobalParameters.getSingleton().getRoundedVolume(resource.getUnitVolume() * amount);
  }

  /**
   * A constructor that sets the resource and amount.
   * 
   * @param resource the resource
   * @param amount the amount
   */
  public Demand(I_Resource resource, double amount) {
    super();
    setResource(resource);
    setAmount(amount);
  }

  /**
   * Gets the demanded resource.
   * 
   * @return the resource
   */
  public I_Resource getResource() {
    return resource;
  }

  /**
   * Sets the demanded resource.
   * 
   * @param resource the resource
   */
  public void setResource(I_Resource resource) {
    this.resource = resource;
  }

  /**
   * Gets the demanded amount.
   * 
   * @return the demanded amount (units of resource)
   */
  public double getAmount() {
    return GlobalParameters.getSingleton().getRoundedDemand(amount);
  }

  /**
   * Sets the demanded amount.
   * 
   * @param amount the demanded amount (units of resource)
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    DecimalFormat format = new DecimalFormat("0.00");
    if (resource == null) {
      return format.format(getAmount()) + " units of unknown";
    } else {
      return format.format(getAmount()) + " " + resource.getUnits() + " of " + resource.getName();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Demand demand) {
    if (getResource() == null) {
      if (demand.getResource() == null)
        return 0;
      else
        return -1;
    } else {
      return getResource().compareTo(demand.getResource());
    }
  }

  @Override
  public Demand clone() throws CloneNotSupportedException {
    Demand d = new Demand();
    d.setAmount(amount);
    d.setResource(resource);
    return d;
  }
}
