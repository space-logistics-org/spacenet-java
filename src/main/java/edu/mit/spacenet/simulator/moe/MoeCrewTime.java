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
 * The measure of effectiveness that logs the crew time usage.
 * 
 * @author Paul Grogan
 */
public class MoeCrewTime implements Comparable<MoeCrewTime> {
	
	/** Unavailable crew time. */
	public static final String UNAVAILABLE = "Unavailable";
	
	/** Corrective maintenance crew time. */
	public static final String CORRECTIVE_MAINTENANCE = "Corrective Maintenance";
	
	/** Preventative maintenance crew time. */
	public static final String PREVENTATIVE_MAINTENANCE = "Preventative Maintenance";
	
	/** Exploration crew time. */
	public static final String EXPLORATION = "Exploration";
	
	private double time;
	private Location location;
	private String type;
	private double amount;
	
	/**
	 * Instantiates a new MOE crew time.
	 * 
	 * @param time the simulation time of the crew time to log
	 * @param location the location of the crew time to log
	 * @param type the type of time to log
	 * @param amount the amount of time to log (crew-hours)
	 */
	public MoeCrewTime(double time, Location location, String type, double amount) {
		this.time = time;
		this.location = location;
		this.type = type;
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
	 * Gets the type of time logged.
	 * 
	 * @return the type of time
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets the amount of time logged.
	 * 
	 * @return the amount (crew-hours)
	 */
	public double getAmount() {
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MoeCrewTime moe) {
		return Double.compare(getTime(), moe==null?0:moe.getTime());
	}
}