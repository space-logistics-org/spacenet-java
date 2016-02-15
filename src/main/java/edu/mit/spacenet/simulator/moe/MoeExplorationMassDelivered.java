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
 * Measure of effectiveness that logs exploration mass that is delivered to a
 * location.
 * 
 * @author Paul Grogan
 */
public class MoeExplorationMassDelivered implements Comparable<MoeExplorationMassDelivered> {
	private double time;
	private Location location;
	private double amount;
	
	/**
	 * Instantiates a new MOE exploration mass delivered.
	 * 
	 * @param time the simulation time of delivery
	 * @param location the location of delivery
	 * @param amount the amount of exploration mass delivered
	 */
	public MoeExplorationMassDelivered(double time, Location location, double amount) {
		this.time = time;
		this.location = location;
		this.amount = amount;
	}
	
	/**
	 * Gets the simulation time of delivery.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Gets the location of delivery.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the amount of exploration mass delivered.
	 * 
	 * @return the amount (kilograms)
	 */
	public double getAmount() {
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeExplorationMassDelivered moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}