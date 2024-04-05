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

import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Defines a relationship between an element and a a part object that includes several modifying
 * quantities.
 * 
 * @author Paul Grogan
 */
public class PartApplication extends DomainType implements Comparable<PartApplication>, Cloneable {
  private Item partType;
  private double meanTimeToFailure;
  private double meanTimeToRepair;
  private double massToRepair;
  private double quantity;
  private double dutyCycle;

  /**
   * Default constructor sets the mean time to failure to 0, the mean time to repair to 0, the
   * quantity to 1, and the duty cycle to 1.
   */
  public PartApplication() {
    super();
    partType = new Item();
    setMeanTimeToFailure(0);
    setMeanTimeToRepair(0);
    setQuantity(1);
    setDutyCycle(1);
  }

  /**
   * Gets the part.
   * 
   * @return the part
   */
  public Item getPart() {
    return partType;
  }

  /**
   * Sets the part.
   * 
   * @param partType the part type
   */
  public void setPart(Item partType) {
    this.partType = partType;
  }

  /**
   * Gets the unit mean time to repair.
   * 
   * @return the unit mean time to repair (hours per kilogram)
   */
  public double getUnitMeanTimeToRepair() {
    return getMeanTimeToRepair() / partType.getUnitMass();
  }

  /**
   * Gets the mean time to failure.
   * 
   * @return the mean time to failure (hours), does not fail if = 0
   */
  public double getMeanTimeToFailure() {
    return meanTimeToFailure;
  }

  /**
   * Sets the mean time to failure.
   * 
   * @param meanTimeToFailure the mean time to failure (hours), does not fail if < 0
   */
  public void setMeanTimeToFailure(double meanTimeToFailure) {
    this.meanTimeToFailure = meanTimeToFailure;
  }

  /**
   * Gets the mean time to repair the part.
   * 
   * @return the mean time to repair (hours), not repairable if = 0
   */
  public double getMeanTimeToRepair() {
    return meanTimeToRepair;
  }

  /**
   * Sets the mean time required to repair the part.
   * 
   * @param meanTimeToRepair the mean time to repair(hours), not repairable if < 0
   */
  public void setMeanTimeToRepair(double meanTimeToRepair) {
    this.meanTimeToRepair = meanTimeToRepair;
  }

  /**
   * Gets the mass of generic COS 4 required to perform a repair.
   * 
   * @return the mass required to repair (kilograms)
   */
  public double getMassToRepair() {
    return massToRepair;
  }

  /**
   * Sets the mass of generic COS 4 required to perform a repair.
   * 
   * @param massToRepair the mass required to repair (kilograms)
   */
  public void setMassToRepair(double massToRepair) {
    this.massToRepair = massToRepair;
  }

  /**
   * Gets the duty cycle.
   * 
   * @return the duty cycle (percent)
   */
  public double getDutyCycle() {
    return dutyCycle;
  }

  /**
   * Sets the duty cycle.
   * 
   * @param dutyCycle the duty cycle (percent)
   */
  public void setDutyCycle(double dutyCycle) {
    this.dutyCycle = dutyCycle;
  }

  /**
   * Gets the quantity.
   * 
   * @return the quantity of parts
   */
  public double getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity.
   * 
   * @param quantity the quantity of parts
   */
  public void setQuantity(double quantity) {
    this.quantity = GlobalParameters.getSingleton().getRoundedDemand(quantity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(PartApplication partApplication) {
    if (getPart() == null) {
      if (partApplication.getPart() == null)
        return 0;
      else
        return -1;
    } else {
      return getPart().compareTo(partApplication.getPart());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return partType + " (" + ((int) getQuantity()) + ")";
  }

  @Override
  public PartApplication clone() throws CloneNotSupportedException {
    PartApplication p = new PartApplication();
    p.setTid(getTid());
    p.setName(getName());
    p.setDescription(getDescription());
    p.setPart(partType);
    p.setMeanTimeToFailure(meanTimeToFailure);
    p.setMeanTimeToRepair(meanTimeToRepair);
    p.setMassToRepair(massToRepair);
    p.setQuantity(quantity);
    p.setDutyCycle(dutyCycle);
    return p;
  }
}
