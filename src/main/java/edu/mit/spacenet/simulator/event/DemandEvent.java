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

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimDemand;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;

/**
 * An event that schedules an instantaneous set of demands.
 * 
 * @author Paul Grogan
 */
public class DemandEvent extends AbstractEvent {
  private I_Element element;
  private DemandSet demands;

  /**
   * The default constructor initializes the set of demands.
   */
  public DemandEvent() {
    super();
    this.demands = new DemandSet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    if (getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this, "Mission location.");
    } else if (element != null && element.getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this, element + " was not found.");
    } else if (element != null && !getLocation().equals(element.getLocation())) {
      throw new SimSpatialError(simulator.getTime(), this, element + " is located at "
          + element.getLocation() + " instead of " + getLocation() + ".");
    }
    DemandSet d = new DemandSet();
    for (Demand demand : demands) {
      // TODO review production demands if(demand.getAmount() > 0) d.add(demand);
      if (Math.abs(demand.getAmount()) > 0)
        d.add(demand);
    }

    System.out.printf("%.3f: %s\n", getTime(),
        "Demand for " + demands + " from " + (element == null ? getLocation() : element));

    if (simulator.isDemandsSatisfied()) {
      if (element != null) {
        element.satisfyDemands(d, simulator);
        // TODO this was changed to look at container first in demands
        if (element.getContainer() instanceof I_Carrier) {
          ((I_Carrier) element.getContainer()).satisfyDemands(d, simulator);
        }
      }
      for (I_Element e : getLocation().getContents()) {
        e.satisfyDemands(d, simulator);
      }
    }
    if (Math.abs(d.getTotalMass()) > 0) {
      if (d.getTotalMass() > 0 && simulator.isPackingDemandsAdded()) {
        Demand packingDemand = new Demand(new GenericResource(ClassOfSupply.COS5), 0);
        for (Demand demand : d) {
          if (demand.getMass() > 0 && demand.getResource().getPackingFactor() > 0)
            packingDemand.setAmount(packingDemand.getAmount()
                + demand.getAmount() * demand.getResource().getPackingFactor());
        }
        d.add(packingDemand);
      }
      throw new SimDemand(simulator.getTime(), this, getLocation(), getElement(), d);
    }
  }

  /**
   * Gets the element requesting the demand.
   * 
   * @return the element requesting the demand
   */
  public I_Element getElement() {
    return element;
  }

  /**
   * Sets the element requesting the demand.
   * 
   * @param element the element
   */
  public void setElement(I_Element element) {
    this.element = element;
  }

  /**
   * Gets the set of demands.
   * 
   * @return the set of demands
   */
  public DemandSet getDemands() {
    return demands;
  }

  /**
   * Sets the set of demands.
   * 
   * @param demands the set of demands
   */
  public void setDemands(DemandSet demands) {
    this.demands = demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.DEMAND;
  }
}
