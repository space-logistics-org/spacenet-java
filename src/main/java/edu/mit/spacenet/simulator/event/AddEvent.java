/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
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
