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
package edu.mit.spacenet.simulator.moe;

import edu.mit.spacenet.domain.network.Location;

/**
 * Measure of effectiveness that logs the utilization of mass capacity.
 * 
 * @author Paul Grogan
 */
public class MoeMassCapacityUtilization implements Comparable<MoeMassCapacityUtilization> {
	private double time;
	private Location location;
	private double amount;
	private double capacity;
	
	/**
	 * Instantiates a new MOE mass capacity utilization.
	 * 
	 * @param time the simulation time to log
	 * @param location the location to log
	 * @param amount the amount of mass apacity used (kilograms)
	 * @param capacity the mass capacity (kilograms)
	 */
	public MoeMassCapacityUtilization(double time, Location location, double amount, double capacity) {
		this.time = time;
		this.location = location;
		this.amount = amount;
		this.capacity = capacity;
	}
	
	/**
	 * Gets the simulation time.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the amount of mass capacity used.
	 * 
	 * @return the amount (kilograms)
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * Gets the mass capacity.
	 * 
	 * @return the capacity (kilograms)
	 */
	public double getCapacity() {
		return capacity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeMassCapacityUtilization moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}