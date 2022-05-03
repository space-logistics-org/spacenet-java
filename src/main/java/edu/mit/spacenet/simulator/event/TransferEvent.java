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

import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * An event that transfers resources from one resource container to another.
 * 
 * @author Paul Grogan
 */
public class TransferEvent extends AbstractEvent {
  private DemandSet demands;
  private I_ResourceContainer originContainer, destinationContainer;

  /**
   * Instantiates a new transfer event.
   */
  public TransferEvent() {
    super();
    this.demands = new DemandSet();
    setName("Transfer Event");
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    if (destinationContainer.getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this,
          destinationContainer + " was not found.");
    } else if (originContainer.getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this, originContainer + " was not found.");
    } else if (!originContainer.getLocation().equals(destinationContainer.getLocation())) {
      throw new SimSpatialError(simulator.getTime(), this,
          destinationContainer + " is located at " + destinationContainer.getLocation()
              + " instead of " + originContainer.getLocation() + ".");
    }

    if (demands.size() == 0) {
      simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, "No demands defined."));
    } else {
      System.out.printf("%.3f: %s\n", getTime(),
          "Transferring " + demands + " to " + destinationContainer);
      for (Demand demand : demands) {
        if (originContainer.remove(demand.getResource(), demand.getAmount())) {
          if (destinationContainer.add(demand.getResource(), demand.getAmount())) {
            // success
          } else {
            originContainer.add(demand.getResource(), demand.getAmount());
            throw new SimSpatialError(simulator.getTime(), this,
                demand + " could not be added to " + destinationContainer + ".");
          }
        } else {
          throw new SimSpatialError(simulator.getTime(), this,
              demand + " could not be removed from " + originContainer + ".");
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
   */
  @Override
  public void print(int tabOrder) {
    super.print(tabOrder);
    System.out.println("Transfer Event for " + demands + " -> " + destinationContainer.getName());
  }

  /**
   * Gets the transfer demands.
   * 
   * @return the transfer demands
   */
  public DemandSet getTransferDemands() {
    return demands;
  }

  /**
   * Sets the transfer demands.
   * 
   * @param demands the new transfer demands
   */
  public void setTransferDemands(DemandSet demands) {
    this.demands = demands;
  }

  /**
   * Gets the origin container.
   * 
   * @return the origin container
   */
  public I_ResourceContainer getOriginContainer() {
    return originContainer;
  }

  /**
   * Sets the origin container.
   * 
   * @param originContainer the new origin container
   */
  public void setOriginContainer(I_ResourceContainer originContainer) {
    this.originContainer = originContainer;
  }

  /**
   * Gets the destination container.
   * 
   * @return the destination container
   */
  public I_ResourceContainer getDestinationContainer() {
    return destinationContainer;
  }

  /**
   * Sets the destination container.
   * 
   * @param destinationContainer the new destination container
   */
  public void setDestinationContainer(I_ResourceContainer destinationContainer) {
    this.destinationContainer = destinationContainer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.TRANSFER;
  }
}
