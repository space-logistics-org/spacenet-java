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

import java.util.HashSet;
import java.util.Set;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;

/**
 * An event that either creates or moves a set of resource containers to a new
 * carrier.
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
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		for(I_ResourceContainer container : containers) {
			if(!carrier.getContents().contains(container)) {
				System.out.printf("%.3f: %s\n", 
						getTime(), "Manifesting " + container + " into " + carrier);
				if(carrier.add(container)) {
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
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return null;
	}
}