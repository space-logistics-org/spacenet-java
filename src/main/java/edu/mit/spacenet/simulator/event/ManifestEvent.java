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
package edu.mit.spacenet.simulator.event;

import java.util.HashSet;
import java.util.Set;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;

/**
 * An event that either creates or moves a set of resource containers to a new carrier.
 * 
 * @author Paul Grogan
 */
public class ManifestEvent extends AbstractEvent {
  private Set<I_ResourceContainer> containers;
  private I_Carrier carrier;

  /**
   * Instantiates a new manifest event.
   */
  public ManifestEvent() {
    super();
    setName("Manifest Event");
    containers = new HashSet<I_ResourceContainer>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    for (I_ResourceContainer container : containers) {
      if (!carrier.getContents().contains(container)) {
        System.out.printf("%.3f: %s\n", getTime(), "Manifesting " + container + " into " + carrier);
        if (carrier.add(container)) {
          simulator.getScenario().getNetwork().getRegistrar().put(container.getUid(), container);
        } else {
          throw new SimSpatialError(simulator.getTime(), this,
              container + " could not be manifested in " + carrier);
        }
      }
    }
  }

  /**
   * Gets the containers.
   * 
   * @return the containers
   */
  public Set<I_ResourceContainer> getContainers() {
    return containers;
  }

  /**
   * Sets the containers.
   * 
   * @param items the new containers
   */
  public void setContainers(Set<I_ResourceContainer> items) {
    this.containers = items;
  }

  /**
   * Gets the carrier.
   * 
   * @return the carrier
   */
  public I_Carrier getCarrier() {
    return carrier;
  }

  /**
   * Sets the carrier.
   * 
   * @param carrier the new carrier
   */
  public void setCarrier(I_Carrier carrier) {
    this.carrier = carrier;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return null;
  }
}
