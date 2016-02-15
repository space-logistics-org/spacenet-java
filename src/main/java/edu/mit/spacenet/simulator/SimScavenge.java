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
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A scavenging operation.
 * 
 * @author Paul Grogan
 */
public class SimScavenge {
	private double time;
	private I_Item item;
	private double amount;
	private Location location;
	private I_Element element;
	
	/**
	 * Instantiates a new sim scavenge.
	 * 
	 * @param time the simulation time of the scavenging
	 * @param item the item that was scavenged
	 * @param amount the amount of item that was scavenged
	 * @param location the location of the scavenging
	 * @param element the element that received the scavenged part
	 */
	public SimScavenge(double time, I_Item item, double amount, Location location, 
			I_Element element) {
		this.time = time;
		this.item = item;
		this.amount = amount;
		this.location = location;
		this.element = element;
	}
	
	/**
	 * Gets the simulation time of the scavenging operation.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return GlobalParameters.getRoundedTime(time);
	}
	
	/**
	 * Gets the item that was scavenged.
	 * 
	 * @return the item
	 */
	public I_Item getItem() {
		return item;
	}
	
	/**
	 * Gets the amount of the item that was scavenged.
	 * 
	 * @return the amount
	 */
	public double getAmount() {
		return GlobalParameters.getRoundedDemand(amount);
	}
	
	/**
	 * Gets the location of the scavenging.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the element that received the scavenged part.
	 * 
	 * @return the element
	 */
	public I_Element getElement() {
		return element;
	}
}
