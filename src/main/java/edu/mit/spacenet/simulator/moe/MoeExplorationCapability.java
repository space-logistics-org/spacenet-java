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
 * The measure of effectiveness that logs exploration capability.
 * 
 * @author Paul Grogan
 */
public class MoeExplorationCapability implements Comparable<MoeExplorationCapability> {
	private double time;
	private Location location;
	private double mass;
	private double duration;
	
	/**
	 * Instantiates a new MOE exploration capability.
	 * 
	 * @param time the simulation time of exploration to log
	 * @param location the location to log
	 * @param mass the mass of exploration-conducive material to log
	 * @param duration the exploration duration to log
	 */
	public MoeExplorationCapability(double time, Location location, double mass, double duration) {
		this.time = time;
		this.location = location;
		this.mass = mass;
		this.duration = duration;
	}
	
	/**
	 * Gets the simulation time of the exploration.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Gets the location of the exploration.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the mass of exploration-conducive material.
	 * 
	 * @return the mass (kilograms)
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Gets the duration of the exploration.
	 * 
	 * @return the duration (days)
	 */
	public double getDuration() {
		return duration;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeExplorationCapability moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}