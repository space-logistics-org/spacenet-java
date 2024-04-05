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


import java.text.DecimalFormat;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element that can hold at most one type of continuous resource up to a capacity constraint.
 * <p>
 * A resource tank is assigned a resource (which itself has a unit mass which could be non-zero
 * depending on the units for which consumption is measured by), a current amount, and a maximum
 * amount.
 * 
 * @author Paul Grogan
 */
public class ResourceTank extends Element implements I_ResourceContainer {
  private I_Resource resource;
  private double amount;
  private double maxAmount;

  /**
   * The default constructor.
   */
  public ResourceTank() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_ResourceContainer#canAdd(edu.mit.spacenet.domain.resource.
   * I_Resource, double)
   */
  public boolean canAdd(I_Resource resource, double amount) {
    if (this.resource != null && !this.resource.equals(resource))
      return false;
    else if (this.amount + amount - maxAmount > GlobalParameters.getSingleton().getDemandPrecision()
        / 2d)
      return false;
    else
      return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#add(edu.mit.spacenet.domain.resource.
   * I_Resource, double)
   */
  public boolean add(I_Resource resource, double amount) {
    if (canAdd(resource, amount)) {
      if (this.resource == null) {
        this.resource = resource;
      }
      this.amount += amount;
      return true;
    } else
      return false;
  }

  /**
   * Adds an amount of resource to the tank.
   * 
   * @param amount the amount
   * 
   * @return true, if successful
   */
  public boolean add(double amount) {
    return add(resource, amount);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_ResourceContainer#remove(edu.mit.spacenet.domain.resource.
   * I_Resource, double)
   */
  public boolean remove(I_Resource resource, double amount) {
    if (this.resource == null || !this.resource.equals(resource)) {
      return false;
    } else if (this.amount - amount >= 0) {
      this.amount = this.amount - amount;
      return true;
    } else
      return false;
  }

  /**
   * Removes an amount of a resource from the tank.
   * 
   * @param amount the amount
   * 
   * @return true, if successful
   */
  public boolean remove(double amount) {
    return remove(resource, amount);
  }

  /**
   * Gets the contained resource.
   * 
   * @return the contained resource
   */
  public I_Resource getResource() {
    return resource;
  }

  /**
   * Sets the contained resource.
   * 
   * @param resource the contained resource
   */
  public void setResource(I_Resource resource) {
    this.resource = resource;
  }

  /**
   * Gets the amount of the contained resource.
   * 
   * @return the amount of resource (units of resource consumption)
   */
  public double getAmount() {
    return GlobalParameters.getSingleton().getRoundedDemand(amount);
  }

  /**
   * Sets the amount of the contained resource.
   * 
   * @param amount the amount of resource (units of resource consumption)
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Gets the maximum amount of the contained resource.
   * 
   * @return the maximum amount of resource (units of resource consumption)
   */
  public double getMaxAmount() {
    return GlobalParameters.getSingleton().getRoundedDemand(maxAmount);
  }

  /**
   * Sets the maximum amount of the contained resource.
   * 
   * @param maxAmount the maximum amount of resource (units of resource consumption)
   */
  public void setMaxAmount(double maxAmount) {
    this.maxAmount = maxAmount;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getTotalMass()
   */
  @Override
  public double getTotalMass() {
    return GlobalParameters.getSingleton().getRoundedMass(super.getTotalMass() + getCargoMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.Element#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
   */
  public double getTotalMass(ClassOfSupply cos) {
    double mass = super.getTotalMass(cos);
    if (resource != null && getResource().getClassOfSupply().isInstanceOf(cos)) {
      mass += getResource().getUnitMass() * getAmount();
    }
    return GlobalParameters.getSingleton().getRoundedMass(mass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#toString()
   */
  public String toString() {
    DecimalFormat format = new DecimalFormat("0.0");
    if (resource == null)
      return super.toString() + " (" + format.format(getAmount()) + " / "
          + format.format(getMaxAmount()) + ")";
    else
      return super.toString() + " (" + format.format(getAmount()) + " / "
          + format.format(getMaxAmount()) + " " + resource.getUnits() + " " + resource + ")";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#print(int)
   */
  @Override
  public void print(int tabOrder) {
    super.print(tabOrder);
    resource.print(tabOrder + 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#satisfyDemands(edu.mit.spacenet.domain.resource.
   * DemandSet, edu.mit.spacenet.simulator.I_Simulator)
   */
  @Override
  public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
    super.satisfyDemands(demands, simulator);
    for (Demand demand : demands) {
      if (demand.getResource().isSubstitutableFor(resource)) {
        if (demand.getAmount() > 0) { // consumption
          if (demand.getAmount() > amount) {
            demand.setAmount(demand.getAmount() - amount);
            amount = 0;
          } else {
            amount -= demand.getAmount();
            demand.setAmount(0);
          }
        } else { // production
          if (-demand.getAmount() > maxAmount - amount) {
            demand.setAmount(demand.getAmount() + (maxAmount - amount));
            amount = maxAmount;
          } else {
            amount += -demand.getAmount();
            demand.setAmount(0);
          }
        }
      }
    }
    demands.clean();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoEnvironment()
   */
  public Environment getCargoEnvironment() {
    return Environment.UNPRESSURIZED;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoMass()
   */
  public double getCargoMass() {
    return resource == null ? 0
        : GlobalParameters.getSingleton().getRoundedMass(amount * resource.getUnitMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoVolume()
   */
  public double getCargoVolume() {
    return resource == null ? 0
        : GlobalParameters.getSingleton().getRoundedVolume(amount * resource.getUnitVolume());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getMaxCargoMass()
   */
  public double getMaxCargoMass() {
    return resource == null ? 0
        : GlobalParameters.getSingleton().getRoundedMass(maxAmount * resource.getUnitMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getMaxCargoVolume()
   */
  public double getMaxCargoVolume() {
    return resource == null ? 0
        : GlobalParameters.getSingleton().getRoundedVolume(maxAmount * resource.getUnitVolume());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_ResourceContainer#setCargoEnvironment(edu.mit.spacenet.domain
   * .Environment)
   */
  public void setCargoEnvironment(Environment cargoEnvironment) {}

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#setMaxCargoMass(double)
   */
  public void setMaxCargoMass(double maxCargoMass) {
    if (resource != null) {
      maxAmount = maxCargoMass / resource.getUnitMass();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#setMaxCargoVolume(double)
   */
  public void setMaxCargoVolume(double maxCargoVolume) {
    if (resource != null) {
      maxAmount = maxCargoVolume / resource.getUnitVolume();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getContents()
   */
  public SortedMap<I_Resource, Double> getContents() {
    TreeMap<I_Resource, Double> contents = new TreeMap<I_Resource, Double>();
    contents.put(resource, amount);
    return contents;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getElementType()
   */
  @Override
  public ElementType getElementType() {
    return ElementType.RESOURCE_TANK;
  }

  @Override
  public ResourceTank clone() throws CloneNotSupportedException {
    ResourceTank e = new ResourceTank();
    e.setTid(getTid());
    e.setName(getName());
    e.setDescription(getDescription());
    e.setClassOfSupply(getClassOfSupply());
    e.setEnvironment(getEnvironment());
    e.setAccommodationMass(getAccommodationMass());
    e.setMass(getMass());
    e.setVolume(getVolume());
    for (PartApplication part : getParts()) {
      e.getParts().add(part.clone());
    }
    for (I_State state : getStates()) {
      I_State s = state.clone();
      e.getStates().add(s);
      if (state.equals(getCurrentState())) {
        e.setCurrentState(s);
      }
    }
    e.setContainer(getContainer());
    e.setIconType(getIconType());
    e.setResource(getResource());
    e.setMaxAmount(getMaxAmount());
    e.setAmount(getAmount());
    return e;
  }
}
