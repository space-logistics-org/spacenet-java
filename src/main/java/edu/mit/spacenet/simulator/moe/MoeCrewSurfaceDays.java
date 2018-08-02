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
 * The measure of effectiveness that tabulates the number of crew surface days.
 * 
 * @author Paul Grogan
 */
public class MoeCrewSurfaceDays implements Comparable<MoeCrewSurfaceDays> {
	private double time;
	private Location location;
	private double amount;
	
	/**
	 * Instantiates a new MOE crew surface days.
	 * 
	 * @param time the simulation time of a crew surface day to log
	 * @param location the location of the crew surface day to log
	 * @param amount the amount time to log (crew-hours)
	 */
	public MoeCrewSurfaceDays(double time, Location location, double amount) {
		this.time = time;
		this.location = location;
		this.amount = amount;
	}
	
	/**
	 * Gets the simulation time of the log.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Gets the location of the log.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the amount of man-hours logged.
	 * 
	 * @return the amount (crew-hours)
	 */
	public double getAmount() {
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeCrewSurfaceDays moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}