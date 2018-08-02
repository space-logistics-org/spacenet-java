/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * An event that adds resources to a resource container.
 * @author Paul Grogan
 */
public class AddEvent extends AbstractEvent {
	private DemandSet demands;
	private I_ResourceContainer container;
	
	/**
	 * Instantiates a new adds the event.
	 */
	public AddEvent() {
		super();
		this.demands = new DemandSet();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(container.getLocation()==null) {
			throw new SimSpatialError(simulator.getTime(), this, 
					container + " was not found.");
		} else if(!container.getLocation().equals(getLocation())) {
			throw new SimSpatialError(simulator.getTime(), this, 
					container + " is located at " + container.getLocation() 
					+ " instead of " + getLocation() + ".");
		}
		if(demands.size() == 0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
				"No demands defined."));
		} else {
			System.out.printf("%.3f: %s\n", 
					getTime(), "Adding " + demands + " to " + container);
			for(Demand demand : demands) {
				if(container.add(demand.getResource(), demand.getAmount())) {
					// success
				} else {
					throw new SimSpatialError(simulator.getTime(), this, 
							demand + " could not be added to " + container + ".");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		System.out.println("Add Event for " + demands + " -> " + container);
	}
	
	/**
	 * Gets the demands.
	 * 
	 * @return the demands
	 */
	public DemandSet getDemands() {
		return demands;
	}
	
	/**
	 * Sets the demands.
	 * 
	 * @param demands the new demands
	 */
	public void setDemands(DemandSet demands) {
		this.demands = demands;
	}
	
	/**
	 * Gets the container.
	 * 
	 * @return the container
	 */
	public I_ResourceContainer getContainer() {
		return container;
	}
	
	/**
	 * Sets the container.
	 * 
	 * @param container the new container
	 */
	public void setContainer(I_ResourceContainer container) {
		this.container = container;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.ADD;
	}
}
