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