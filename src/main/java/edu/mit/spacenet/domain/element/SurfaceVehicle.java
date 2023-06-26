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


import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A vehicle with an assigned maximum speed and a resource container to represent a fuel tank. Note
 * that unlike a propulsive vehicle, consumption of fuel should be handled by a demand model rather
 * than through impulsive burns.
 * 
 * @author Paul Grogan
 */
public class SurfaceVehicle extends Carrier {
  private double maxSpeed;
  private ResourceTank fuelTank;

  /**
   * The default constructor that initializes the fuel tank.
   */
  public SurfaceVehicle() {
    super();
    fuelTank = new ResourceTank();
    fuelTank.setName(getName() + " Fuel Tank");
    fuelTank.setContainer(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    super.setName(name);
    if (fuelTank != null)
      fuelTank.setName(getName() + " Fuel Tank");
  }

  /**
   * Gets the maximum speed.
   * 
   * @return the maximum speed (kilometers per hour)
   */
  public double getMaxSpeed() {
    return maxSpeed;
  }

  /**
   * Sets the maximum speed.
   * 
   * @param maxSpeed the maximum speed (kilometers per hour)
   */
  public void setMaxSpeed(double maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  /**
   * Gets the fuel tank resource container.
   * 
   * @return the fuel tank resource container
   */
  public ResourceTank getFuelTank() {
    return fuelTank;
  }

  /**
   * Sets the fuel tank resource container.
   * 
   * @param fuelTank the fuel tank resource container
   */
  public void setFuelTank(ResourceTank fuelTank) {
    this.fuelTank = fuelTank;
    if (fuelTank != null) {
      fuelTank.setName(getName() + " Fuel Tank");
      fuelTank.setContainer(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass()
   */
  @Override
  public double getTotalMass() {
    return GlobalParameters.getSingleton()
        .getRoundedMass(super.getTotalMass() + fuelTank.getTotalMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.Carrier#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
   */
  @Override
  public double getTotalMass(ClassOfSupply cos) {
    double amount = super.getTotalMass(cos);
    if (getFuelTank() != null)
      amount += getFuelTank().getTotalMass(cos);
    return GlobalParameters.getSingleton().getRoundedMass(amount);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#print(int)
   */
  @Override
  public void print(int tabOrder) {
    super.print(tabOrder);
    fuelTank.print(tabOrder + 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#satisfyDemands(edu.mit.spacenet.domain.resource.
   * DemandSet, edu.mit.spacenet.simulator.I_Simulator)
   */
  @Override
  public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
    fuelTank.satisfyDemands(demands, simulator);
    super.satisfyDemands(demands, simulator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#getElementType()
   */
  @Override
  public ElementType getElementType() {
    return ElementType.SURFACE_VEHICLE;
  }

  @Override
  public SurfaceVehicle clone() throws CloneNotSupportedException {
    SurfaceVehicle e = new SurfaceVehicle();
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
    e.setMaxCargoMass(getMaxCargoMass());
    e.setMaxCargoVolume(getMaxCargoVolume());
    e.setCargoEnvironment(getCargoEnvironment());
    e.setMaxCrewSize(getMaxCrewSize());
    for (I_Element element : getContents()) {
      e.getContents().add(element.clone());
    }
    e.setMaxSpeed(getMaxSpeed());
    e.setFuelTank(getFuelTank().clone());
    return e;
  }
}
