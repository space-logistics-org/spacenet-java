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
 * A vehicle that is characterized by having one, either, or both OMS impulsive burns and RCS
 * impulsive burns capabilities.
 * 
 * @author Paul Grogan
 */
public class PropulsiveVehicle extends Carrier {
  private double omsIsp, rcsIsp;
  private ResourceTank omsFuelTank, rcsFuelTank;

  /**
   * The default constructor that initializes the OMS and RCS resource containers.
   */
  public PropulsiveVehicle() {
    super();
    omsFuelTank = new ResourceTank();
    omsFuelTank.setName(getName() + " OMS Fuel Tank");
    omsFuelTank.setContainer(this);
    rcsFuelTank = new ResourceTank();
    rcsFuelTank.setName(getName() + " RCS Fuel Tank");
    rcsFuelTank.setContainer(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    super.setName(name);
    if (omsFuelTank != null)
      omsFuelTank.setName(getName() + " OMS Fuel Tank");
    if (rcsFuelTank != null && rcsFuelTank != omsFuelTank)
      rcsFuelTank.setName(getName() + " RCS Fuel Tank");
  }

  /**
   * Gets the Isp (specific impulse) of the OMS engine.
   * 
   * @return the OMS Isp (seconds)
   */
  public double getOmsIsp() {
    return omsIsp;
  }

  /**
   * Sets the Isp (specific impulse) of the OMS engine.
   * 
   * @param omsIsp the OMS Isp (seconds)
   */
  public void setOmsIsp(double omsIsp) {
    this.omsIsp = omsIsp;
  }

  /**
   * Gets the resource container representing the OMS fuel tank.
   * 
   * @return the OMS fuel tank resource container
   */
  public ResourceTank getOmsFuelTank() {
    return omsFuelTank;
  }

  /**
   * Sets the resource container representing the OMS fuel tank.
   * 
   * @param omsFuelTank the OMS fuel tank resource container
   */
  public void setOmsFuelTank(ResourceTank omsFuelTank) {
    this.omsFuelTank = omsFuelTank;
    if (omsFuelTank != null) {
      omsFuelTank.setName(getName() + " OMS Fuel Tank");
      omsFuelTank.setContainer(this);
    }
  }

  /**
   * Gets the Isp (specific impulse) of the RCS engine.
   * 
   * @return the RCS Isp (seconds)
   */
  public double getRcsIsp() {
    return rcsIsp;
  }

  /**
   * Sets the Isp (specific impulse) of the RCS engine.
   * 
   * @param rcsIsp the RCS Isp (seconds)
   */
  public void setRcsIsp(double rcsIsp) {
    this.rcsIsp = rcsIsp;
  }

  /**
   * Gets the resource container representing the RCS fuel tank.
   * 
   * @return the RCS fuel tank resource container
   */
  public ResourceTank getRcsFuelTank() {
    return rcsFuelTank;
  }

  /**
   * Sets the resource container representing the RCS fuel tank.
   * 
   * @param rcsFuelTank the RCS fuel tank resource container
   */
  public void setRcsFuelTank(ResourceTank rcsFuelTank) {
    this.rcsFuelTank = rcsFuelTank;
    if (rcsFuelTank != null && rcsFuelTank != omsFuelTank) {
      rcsFuelTank.setName(getName() + " RCS Fuel Tank");
      rcsFuelTank.setContainer(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass()
   */
  @Override
  public double getTotalMass() {
    double mass = super.getTotalMass();
    if (omsFuelTank != null)
      mass += omsFuelTank.getTotalMass();
    if (rcsFuelTank != null && rcsFuelTank != omsFuelTank)
      mass += rcsFuelTank.getTotalMass();
    return GlobalParameters.getSingleton().getRoundedMass(mass);
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
    if (getOmsFuelTank() != null)
      amount += getOmsFuelTank().getTotalMass(cos);
    if (getRcsFuelTank() != null && getRcsFuelTank() != getOmsFuelTank())
      amount += getRcsFuelTank().getTotalMass(cos);
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
    if (omsFuelTank != null)
      omsFuelTank.print(tabOrder + 1);
    if (rcsFuelTank != null && rcsFuelTank != omsFuelTank)
      rcsFuelTank.print(tabOrder + 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#satisfyDemands(edu.mit.spacenet.domain.resource.
   * DemandSet, edu.mit.spacenet.simulator.I_Simulator)
   */
  @Override
  public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
    if (omsFuelTank != null)
      omsFuelTank.satisfyDemands(demands, simulator);
    if (rcsFuelTank != null && rcsFuelTank != omsFuelTank)
      rcsFuelTank.satisfyDemands(demands, simulator);
    super.satisfyDemands(demands, simulator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Carrier#getElementType()
   */
  @Override
  public ElementType getElementType() {
    return ElementType.PROPULSIVE_VEHICLE;
  }

  @Override
  public PropulsiveVehicle clone() throws CloneNotSupportedException {
    PropulsiveVehicle e = new PropulsiveVehicle();
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
    e.setRcsIsp(getRcsIsp());
    e.setOmsIsp(getOmsIsp());
    e.setRcsFuelTank(getRcsFuelTank().clone());
    e.setOmsFuelTank(getOmsFuelTank().clone());
    return e;
  }
}
