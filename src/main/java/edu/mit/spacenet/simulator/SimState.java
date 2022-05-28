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
package edu.mit.spacenet.simulator;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Logs the state of the network at a simulation time.
 * 
 * @author Paul Grogan
 */
public class SimState implements Comparable<SimState> {
  private double time;
  private SortedMap<I_Element, I_Container> locationMap;

  /**
   * Instantiates a new sim state.
   * 
   * @param time the simulation time
   * @param network the scenario network
   */
  public SimState(double time, Network network) {
    this.time = time;
    locationMap = new TreeMap<I_Element, I_Container>();
    for (I_Element element : network.getRegistrar().values()) {
      locationMap.put(element, element.getContainer());
    }
  }

  /**
   * Gets the simulation time of the log.
   * 
   * @return the simulation time
   */
  public double getTime() {
    return GlobalParameters.getSingleton().getRoundedTime(time);
  }

  /**
   * Gets the container for an element.
   * 
   * @param element the element
   * 
   * @return the element's container
   */
  public I_Container getContainer(I_Element element) {
    return locationMap.get(element);
  }

  /**
   * Gets the location of an element.
   * 
   * @param element the element
   * 
   * @return the element's location
   */
  public Location getLocation(I_Element element) {
    if (locationMap.get(element) instanceof Location)
      return (Location) locationMap.get(element);
    else if (locationMap.get(element) instanceof I_Carrier)
      return getLocation((I_Carrier) locationMap.get(element));
    else
      return null;
  }

  /**
   * Gets the set of elements in the log.
   * 
   * @return the set of elements
   */
  public Set<I_Element> getElements() {
    return locationMap.keySet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(SimState state) {
    return Double.compare(getTime(), state == null ? 0 : state.getTime());
  }
}
