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
		for(I_Element element : network.getRegistrar().values()) {
			locationMap.put(element, element.getContainer());
		}
	}
	
	/**
	 * Gets the simulation time of the log.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return GlobalParameters.getRoundedTime(time);
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
		if(locationMap.get(element) instanceof Location) 
			return (Location)locationMap.get(element);
		else if(locationMap.get(element) instanceof I_Carrier) 
			return getLocation((I_Carrier)locationMap.get(element));
		else return null;
	}
	
	/**
	 * Gets the set of elements in the log.
	 * 
	 * @return the set of elements
	 */
	public Set<I_Element> getElements() {
		return locationMap.keySet();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SimState state) {
		return Double.compare(getTime(), state==null?0:state.getTime());
	}
}
