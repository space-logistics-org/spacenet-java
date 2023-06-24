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

import javax.swing.ImageIcon;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.I_DomainObject;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * The I_Element interface represents the elements of the simulation.
 * 
 * @author Paul Grogan
 */
public interface I_Element extends I_DomainObject, Comparable<I_Element>, Cloneable {

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
   * Gets the element environment.
   * 
   * @return the environment
   */
  public Environment getEnvironment();

  /**
   * Sets the element environment.
   * 
   * @param environment the environment
   */
  public void setEnvironment(Environment environment);

  /**
   * Gets the mass of the element.
   * 
   * @return the mass (kilograms)
   */
  public double getMass();

  /**
   * Sets the mass of the element.
   * 
   * @param mass the mass (kilograms)
   */
  public void setMass(double mass);

  /**
   * Gets the mass of the element and any nested resources.
   * 
   * @return the total mass of the element and any nested elements (kilograms)
   */
  public double getTotalMass();

  /**
   * Get the total mass of all contained resources of the specified class of supply.
   * 
   * @param cos the class of supply
   * 
   * @return the total mass of all resources
   */
  public double getTotalMass(ClassOfSupply cos);

  /**
   * Gets the volume of the element.
   * 
   * @return the volume (cubic meters)
   */
  public double getVolume();

  /**
   * Sets the volume of the element.
   * 
   * @param volume the volume (cubic meters)
   */
  public void setVolume(double volume);

  /**
   * Gets the total volume of the element.
   * 
   * @return the total volume (cubic meters)
   */
  public double getTotalVolume();

  /**
   * Gets the container that this element resides in.
   * 
   * @return the element's container
   */
  public I_Container getContainer();

  /**
   * Sets the container that this element resides in.
   * 
   * @param container the element's container
   */
  public void setContainer(I_Container container);

  /**
   * Gets the location (node or edge) for this element.
   * 
   * @return the location
   */
  public Location getLocation();

  /**
   * Gets whether this element is nested at some level inside a given container.
   * 
   * @param container the container to look inside
   * 
   * @return if this element is nested inside
   */
  public boolean isInside(I_Container container);

  /**
   * Generates the set of aggregate demands from the element's active state for the specified
   * duration (in days).
   * 
   * @param duration the duration (in days) to generate for
   * @param simulator the simulator requesting the demands
   * 
   * @return the aggregated set of demands
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator);

  /**
   * Request the element to satisfy the given demands (may be passed to nested elements).
   * 
   * @param demands the demands to be satisfied
   * @param simulator the simulator requesting the demand satisfaction
   */
  void satisfyDemands(DemandSet demands, I_Simulator simulator);

  /**
   * Gets the set of part applications.
   * 
   * @return the set of part applications
   */
  public SortedSet<PartApplication> getParts();

  /**
   * Sets the set of part applications.
   * 
   * @param parts the set of part applications
   */
  public void setParts(SortedSet<PartApplication> parts);

  /**
   * Gets the set of states.
   * 
   * @return the set of states
   */
  public SortedSet<I_State> getStates();

  /**
   * Sets the set of states.
   * 
   * @param states the set of states
   */
  public void setStates(SortedSet<I_State> states);

  /**
   * Gets the current state.
   * 
   * @return the current state
   */
  public I_State getCurrentState();

  /**
   * Sets the current state.
   * 
   * @param newState the current state.
   * 
   * @return true if successful, false otherwise
   */
  public boolean setCurrentState(I_State newState);

  /**
   * Gets the amount of additional COS5 required to secure this element when being placed inside a
   * carrier.
   * 
   * @return the accommodation mass
   */
  public double getAccommodationMass();

  /**
   * Sets the amount of additional COS5 required to secure this element when being placed inside a
   * carrier.
   * 
   * @param mass the accommodation mass
   */
  public void setAccommodationMass(double mass);

  /**
   * Gets the element type.
   * 
   * @return the element type
   */
  public ElementType getElementType();

  /**
   * Gets the icon representation of the element. Returns a default icon based on element type if no
   * icon is previously set.
   * 
   * @return the element icon
   */
  public ImageIcon getIcon();

  /**
   * Gets the icon type.
   *
   * @return the icon type
   */
  public ElementIcon getIconType();

  /**
   * Sets the icon type.
   *
   * @param iconType the new icon type
   */
  public void setIconType(ElementIcon iconType);

  /**
   * Creates a clone of this element.
   * 
   * @return the cloned element
   * @throws CloneNotSupportedException
   */
  public I_Element clone() throws CloneNotSupportedException;
}
