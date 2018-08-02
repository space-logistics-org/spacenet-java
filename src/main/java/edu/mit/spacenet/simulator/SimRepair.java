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
