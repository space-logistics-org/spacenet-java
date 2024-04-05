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

import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element with the capability to contain crew and cargo up to capacity constraints.
 * 
 * @author Paul Grogan
 */
public class Carrier extends Element implements I_Carrier {
  private SortedSet<I_Element> contents;
  private double maxCargoMass;
  private double maxCargoVolume;
  private Environment cargoEnvironment;
  private int maxCrewSize;

  /**
   * The default constructor that initializes the contents, crew, and cargo structures.
   */
  public Carrier() {
    super();
    setCargoEnvironment(Environment.UNPRESSURIZED);
    contents = new TreeSet<I_Element>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#getCargoEnvironment()
   */
  public Environment getCargoEnvironment() {
    return cargoEnvironment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#setCargoEnvironment(edu.mit.spacenet.domain.
   * Environment)
   */
  public void setCargoEnvironment(Environment cargoEnvironment) {
    this.cargoEnvironment = cargoEnvironment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#getMaxCargoMass()
   */
  public double getMaxCargoMass() {
    return GlobalParameters.getSingleton().getRoundedMass(maxCargoMass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#setMaxCargoMass(double)
   */
  public void setMaxCargoMass(double maxCargoMass) {
    this.maxCargoMass = maxCargoMass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getCargoMass()
   */
  public double getCargoMass() {
    double mass = 0;
    for (I_Element i : contents) {
      if (!(i instanceof CrewMember)) {
        mass += i.getTotalMass();
      }
    }
    return mass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#getMaxCargoVolume()
   */
  public double getMaxCargoVolume() {
    return GlobalParameters.getSingleton().getRoundedVolume(maxCargoVolume);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#setMaxCargoVolume(double)
   */
  public void setMaxCargoVolume(double maxCargoVolume) {
    this.maxCargoVolume = maxCargoVolume;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getCargoVolume()
   */
  public double getCargoVolume() {
    double volume = 0;
    for (I_Element e : contents) {
      if (!(e instanceof CrewMember)) {
        volume += e.getVolume();
      }
    }
    return GlobalParameters.getSingleton().getRoundedVolume(volume);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getCrewSize()
   */
  public int getCrewSize() {
    int crew = 0;
    for (I_Element e : contents) {
      if (e instanceof CrewMember) {
        crew++;
      }
    }
    return crew;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getTotalCrewSize()
   */
  public int getTotalCrewSize() {
    int crew = 0;
    for (I_Element e : contents) {
      if (e instanceof CrewMember) {
        crew++;
      } else if (e instanceof I_Carrier) {
        crew += ((I_Carrier) e).getTotalCrewSize();
      }
    }
    return crew;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#getMaxCrewSize()
   */
  public int getMaxCrewSize() {
    return maxCrewSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Carrier#setMaxCrewSize(int)
   */
  public void setMaxCrewSize(int maxCrewSize) {
    this.maxCrewSize = maxCrewSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#canAdd(edu.mit.spacenet.domain.element.I_Element)
   */
  public boolean canAdd(I_Element element) {
    if (element instanceof CrewMember) {
      if (contents.contains(element))
        return true;
      else if (getCrewSize() < maxCrewSize)
        return true;
      else
        return false;
    } else {
      if (contents.contains(element)) {
        return true;
      } else if (getCargoMass() + element.getTotalMass()
          - getMaxCargoMass() > GlobalParameters.getSingleton().getMassPrecision() / 2d) {
        return false; // mass constrained
      } else if (GlobalParameters.getSingleton().isVolumeConstrained()
          && getCargoVolume() + element.getVolume()
              - getMaxCargoVolume() > GlobalParameters.getSingleton().getVolumePrecision() / 2d) {
        return false; // volume constrained
      } else if (GlobalParameters.getSingleton().isEnvironmentConstrained()
          && (element.getEnvironment().equals(Environment.PRESSURIZED)
              && getCargoEnvironment().equals(Environment.UNPRESSURIZED))) {
        return false; // environment constrained
      } else
        return getContainer() == null ? true : getContainer().canAdd(element.getTotalMass());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#canAdd(double)
   */
  public boolean canAdd(double addedMass) {
    if (getCargoMass() + addedMass
        - getMaxCargoMass() > GlobalParameters.getSingleton().getMassPrecision() / 2d)
      return false;
    else
      return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#add(edu.mit.spacenet.domain.element.I_Element)
   */
  public boolean add(I_Element element) {
    if (contents.contains(element))
      return true;
    else if (canAdd(element)) {
      if (element.getContainer() != null)
        element.getContainer().remove(element);
      element.setContainer(this);
      contents.add(element);
      return true;
    } else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#remove(edu.mit.spacenet.domain.element.I_Element)
   */
  public boolean remove(I_Element element) {
    if (contents.contains(element)) {
      element.setContainer(null);
      contents.remove(element);
      return true;
    } else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getContents()
   */
  public SortedSet<I_Element> getContents() {
    return contents;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_Container#getCompleteContents()
   */
  public SortedSet<I_Element> getCompleteContents() {
    SortedSet<I_Element> elements = new TreeSet<I_Element>();
    for (I_Element element : contents)
      recursiveAdd(elements, element);
    return elements;
  }

  private void recursiveAdd(SortedSet<I_Element> elements, I_Element element) {
    elements.add(element);
    if (element instanceof I_Container) {
      for (I_Element child : ((I_Container) element).getContents()) {
        recursiveAdd(elements, child);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getTotalMass()
   */
  @Override
  public double getTotalMass() {
    double mass = 0;
    for (I_Element e : contents) {
      mass += e.getTotalMass();
    }
    return GlobalParameters.getSingleton().getRoundedMass(super.getTotalMass() + mass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.Element#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
   */
  @Override
  public double getTotalMass(ClassOfSupply cos) {
    double amount = super.getTotalMass(cos);
    for (I_Element e : getContents()) {
      amount += e.getTotalMass(cos);
    }
    return GlobalParameters.getSingleton().getRoundedMass(amount);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#print(int)
   */
  @Override
  public void print(int tabOrder) {
    super.print(tabOrder);
    for (I_Element e : contents) {
      e.print(tabOrder + 1);
    }
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
    for (I_Element i : contents)
      i.satisfyDemands(demands, simulator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getElementType()
   */
  @Override
  public ElementType getElementType() {
    return ElementType.CARRIER;
  }

  @Override
  public Carrier clone() throws CloneNotSupportedException {
    Carrier e = new Carrier();
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
    return e;
  }
}
