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
 * Logs a repair activity.
 * 
 * @author Paul Grogan
 */
public class SimRepair {
	private double time;
	private I_Item item;
	private double amount;
	private double repairTime;
	private double repairMass;
	private Location location;
	private I_Element element;
	
	/**
	 * Instantiates a new sim repair.
	 * 
	 * @param time the simulation time of the repair
	 * @param item the item that was repaired
	 * @param amount the amount of the item that was repaired
	 * @param repairTime the crew time required for the repair
	 * @param repairMass the mass required for the repair
	 * @param location the location of the repair
	 * @param element the element being repaired
	 */
	public SimRepair(double time, I_Item item, double amount, double repairTime, 
			double repairMass, Location location, I_Element element) {
		this.time = time;
		this.item = item;
		this.amount = amount;
		this.repairTime = repairTime;
		this.repairMass = repairMass;
		this.location = location;
		this.element = element;
	}
	
	/**
	 * Gets the simulation time of the repair.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return GlobalParameters.getRoundedTime(time);
	}
	
	/**
	 * Gets the item that was repaired.
	 * 
	 * @return the item
	 */
	public I_Item getItem() {
		return item;
	}
	
	/**
	 * Gets the amount of the item that was repaired.
	 * 
	 * @return the amount
	 */
	public double getAmount() {
		return GlobalParameters.getRoundedDemand(amount);
	}
	
	/**
	 * Gets the time required for the repair.
	 * 
	 * @return the time
	 */
	public double getRepairTime() {
		return repairTime;
	}
	
	/**
	 * Gets the mass required for the repair.
	 * 
	 * @return the mass
	 */
	public double getRepairMass() {
		return repairMass;
	}
	
	/**
	 * Gets the location of the repair.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the element that was repaired.
	 * 
	 * @return the element
	 */
	public I_Element getElement() {
		return element;
	}
}
