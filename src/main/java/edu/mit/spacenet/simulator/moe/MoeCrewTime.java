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