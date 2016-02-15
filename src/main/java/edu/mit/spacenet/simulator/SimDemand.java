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
package edu.mit.spacenet.simulator;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * Represents a demand for resources that has not been satisfied in the demand
 * cycle that has been logged during the simulation.
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
	public SimDemand(double time, I_Event event, Location location, I_Element element, DemandSet demands) {
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
	 * Gets the element generating the demand, null if not associated with an
	 * element (e.g. a mission-level demand).
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