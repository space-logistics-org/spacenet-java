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

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * Represents a demand for resources that has not been satisfied in the demand cycle that has been
 * logged during the simulation.
 * 
 * @author Paul Grogan
 */
public class SimDemand extends SimError {
  private static final long serialVersionUID = 8446495287458296511L;
  private Location location;
  private I_Element element;
  private DemandSet demands;

  /**
   * Instantiates a new sim demand.
   * 
   * @param time the time of the demand
   * @param event the event being executed when the demand occurs
   * @param location the location of the demand
   * @param element the element requesting the demand
   * @param demands the set of demands
   */
  public SimDemand(double time, I_Event event, Location location, I_Element element,
      DemandSet demands) {
    super(time, event, "Insufficient resources for demand: " + demands);
    this.location = location;
    this.element = element;
    demands.clean();
    setMessage("Insufficient resources for demand: " + demands);
    this.demands = demands;
  }

  /**
   * Gets the location of the demand.
   * 
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Gets the element generating the demand, null if not associated with an element (e.g. a
   * mission-level demand).
   * 
   * @return the element
   */
  public I_Element getElement() {
    return element;
  }

  /**
   * Gets the set of demands.
   * 
   * @return the set of demands
   */
  public DemandSet getDemands() {
    return demands;
  }
}
