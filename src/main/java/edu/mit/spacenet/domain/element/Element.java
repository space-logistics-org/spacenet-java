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
import javax.swing.ImageIcon;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.DomainObject;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimScavenge;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Represents the main objects in simulation, each may have a set of parts (through the intermediate
 * part application class), and a set of states, with one current state.
 * 
 * @author Paul Grogan
 */
public class Element extends DomainObject implements I_Element {
  private ClassOfSupply classOfSupply;
  private Environment environment;
  private double accommodationMass;
  private double mass;
  private double volume;
  private SortedSet<PartApplication> parts;
  private SortedSet<I_State> states;
  private I_State currentState;
  private I_Container container;
  private transient ImageIcon icon;
  private ElementIcon iconType;

  /**
   * The default constructor sets the default class of supply to COS0, sets the environment to
   * unpressurized, and initializes the set of part applications and the set of states. Also creates
   * a default state, adds it to the set of states, and sets it as the current state.
   */
  public Element() {
    super();
    setClassOfSupply(ClassOfSupply.COS0);
    setEnvironment(Environment.UNPRESSURIZED);
    parts = new TreeSet<PartApplication>();
    states = new TreeSet<I_State>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getClassOfSupply()
   */
  public ClassOfSupply getClassOfSupply() {
    return classOfSupply;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setClassOfSupply(edu.mit.spacenet.domain.
   * ClassOfSupply)
   */
  public void setClassOfSupply(ClassOfSupply classOfSupply) {
    this.classOfSupply = classOfSupply;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getEnvironment()
   */
  public Environment getEnvironment() {
    return GlobalParameters.getSingleton().isEnvironmentConstrained() ? environment
        : Environment.UNPRESSURIZED;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_Element#setEnvironment(edu.mit.spacenet.domain.Environment)
   */
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getAccommodationMass()
   */
  public double getAccommodationMass() {
    return GlobalParameters.getSingleton().getRoundedMass(accommodationMass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setAccommodationMass(double)
   */
  public void setAccommodationMass(double mass) {
    this.accommodationMass = mass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getMass()
   */
  public double getMass() {
    return GlobalParameters.getSingleton().getRoundedMass(mass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setMass(double)
   */
  public void setMass(double mass) {
    this.mass = mass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getTotalMass()
   */
  public double getTotalMass() {
    return GlobalParameters.getSingleton().getRoundedMass(getMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getVolume()
   */
  public double getVolume() {
    return GlobalParameters.getSingleton().getRoundedVolume(volume);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setVolume(double)
   */
  public void setVolume(double volume) {
    this.volume = volume;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getTotalVolume()
   */
  public double getTotalVolume() {
    return GlobalParameters.getSingleton().getRoundedVolume(getVolume());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    if (getCurrentState() == null) {
      return new DemandSet();
    } else {
      return getCurrentState().generateDemands(duration, simulator);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#satisfyDemands(edu.mit.spacenet.domain.resource.
   * DemandSet, edu.mit.spacenet.simulator.I_Simulator)
   */
  public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
    if (simulator.getScenario().isScavengeSpares() && getCurrentState() != null
        && getCurrentState().getStateType().equals(StateType.DECOMMISSIONED)) {
      for (PartApplication application : getParts()) {
        for (Demand demand : demands) {
          if (application.getPart().equals(demand.getResource())) {
            if (demand.getAmount() > 0) {
              if (demand.getAmount() > application.getQuantity()) {
                simulator.getScavengedParts().add(new SimScavenge(simulator.getTime(),
                    application.getPart(), application.getQuantity(), getLocation(), this));
                demand.setAmount(demand.getAmount() - application.getQuantity());
                application.setQuantity(0);
              } else {
                simulator.getScavengedParts().add(new SimScavenge(simulator.getTime(),
                    application.getPart(), demand.getAmount(), getLocation(), this));
                application.setQuantity(application.getQuantity() - demand.getAmount());
                demands.remove(demand);
                break;
              }
            }
          }
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_Element#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
   */
  public double getTotalMass(ClassOfSupply cos) {
    double mass = 0;
    if (GlobalParameters.getSingleton().isScavengeSpares() && getCurrentState() != null
        && getCurrentState().getStateType().equals(StateType.DECOMMISSIONED)) {
      double partsMass = 0;
      for (PartApplication p : getParts()) {
        partsMass += p.getQuantity() * p.getPart().getUnitMass();
        if (p.getPart().getClassOfSupply().isInstanceOf(cos)) {
          mass += p.getQuantity() * p.getPart().getUnitMass();
        }
      }
      if (getClassOfSupply().isInstanceOf(cos)) {
        mass += Math.max(0, getMass() - partsMass);
      }
    } else {
      if (getClassOfSupply().isInstanceOf(cos)) {
        mass += getMass();
      }
    }
    return GlobalParameters.getSingleton().getRoundedMass(mass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getParts()
   */
  public SortedSet<PartApplication> getParts() {
    return this.parts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setParts(java.util.SortedSet)
   */
  public void setParts(SortedSet<PartApplication> parts) {
    this.parts = parts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getStates()
   */
  public SortedSet<I_State> getStates() {
    return states;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setStates(java.util.SortedSet)
   */
  public void setStates(SortedSet<I_State> states) {
    this.states = states;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getCurrentState()
   */
  public I_State getCurrentState() {
    return currentState;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setCurrentState(edu.mit.spacenet.domain.element.
   * I_State)
   */
  public boolean setCurrentState(I_State newState) {
    if (states.size() == 0 && newState == null) {
      this.currentState = null;
      return true;
    } else if (states.contains(newState)) {
      this.currentState = newState;
      return true;
    } else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getContainer()
   */
  public I_Container getContainer() {
    return container;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_Element#setContainer(edu.mit.spacenet.domain.I_Container)
   */
  public void setContainer(I_Container container) {
    this.container = container;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getLocation()
   */
  public Location getLocation() {
    if (getContainer() == null)
      return null;
    else if (getContainer() instanceof Location)
      return (Location) getContainer();
    else if (getContainer() instanceof I_Carrier)
      return ((I_Carrier) getContainer()).getLocation();
    else
      throw new RuntimeException("Illegal container: " + getContainer());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#isInside(edu.mit.spacenet.domain.I_Container)
   */
  public boolean isInside(I_Container container) {
    if (getContainer().equals(container))
      return true;
    else if (getContainer() instanceof I_Carrier)
      return ((I_Carrier) getContainer()).isInside(container);
    else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(I_Element element) {
    if (getName().equals(element.getName())) {
      if (getTid() == element.getTid()) {
        return new Integer(getUid()).compareTo(new Integer(element.getUid()));
      } else {
        return new Integer(getTid()).compareTo(new Integer(element.getTid()));
      }
    } else {
      return getName().compareTo(element.getName());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getElementType()
   */
  public ElementType getElementType() {
    return ElementType.ELEMENT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getIcon()
   */
  public ImageIcon getIcon() {
    if (iconType == null)
      return getElementType().getIconType().getIcon();
    else if (icon == null)
      icon = iconType.getIcon();
    return icon;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#getIconType()
   */
  public ElementIcon getIconType() {
    if (iconType == null && icon != null) {
      for (ElementIcon t : ElementIcon.values()) {
        if (t.getIcon().equals(icon)) {
          this.iconType = t;
          break;
        }
      }
    } else if (iconType == null && icon == null) {
      this.iconType = getElementType().getIconType();
    }
    return iconType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_Element#setIconType(java.lang.String)
   */
  public void setIconType(ElementIcon iconType) {
    this.iconType = iconType;
    this.icon = null;
  }

  @Override
  public Element clone() throws CloneNotSupportedException {
    Element e = new Element();
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
    return e;
  }
}
