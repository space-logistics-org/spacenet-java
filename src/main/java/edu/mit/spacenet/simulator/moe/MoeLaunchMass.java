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
package edu.mit.spacenet.simulator.moe;

import edu.mit.spacenet.domain.network.Location;

/**
 * Measure of effectiveness that logs the launch mass.
 * 
 * @author Paul Grogan
 */
public class MoeLaunchMass implements Comparable<MoeLaunchMass> {
	private double time;
	private Location location;
	private double amount;
	
	/**
	 * Instantiates a new MOE launch mass.
	 * 
	 * @param time the simulation time of launch
	 * @param location the location of launch
	 * @param amount the amount of launch mass (kilograms)
	 */
	public MoeLaunchMass(double time, Location location, double amount) {
		this.time = time;
		this.location = location;
		this.amount = amount;
	}
	
	/**
	 * Gets the simulation time of launch.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Gets the location of launch.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the amount of launch mass.
	 * 
	 * @return the launch mass (kilograms)
	 */
	public double getAmount() {
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeLaunchMass moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}