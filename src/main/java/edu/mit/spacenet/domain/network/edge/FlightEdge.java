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
package edu.mit.spacenet.domain.network.edge;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * Edge that represents an abstracted flight with a finite duration and
 * capacity constraints on crew and cargo mass.
 * 
 * @author Paul Grogan
 */
public class FlightEdge extends Edge {
	private double duration;
	private int maxCrewSize;
	private double maxCargoMass;
	
	/**
	 * The default constructor.
	 */
	public FlightEdge() {
		super();
	}
	
	/**
	 * Gets the flight duration.
	 * 
	 * @return the duration (days)
	 */
	public double getDuration() {
		return GlobalParameters.getRoundedTime(duration);
	}
	
	/**
	 * Sets the flight duration, rounding to nearest time precision.
	 * 
	 * @param duration the duration (days)
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	/**
	 * Gets the maximum crew size.
	 * 
	 * @return the maximum crew size
	 */
	public int getMaxCrewSize() {
		return maxCrewSize;
	}
	
	/**
	 * Sets the maximum crew size.
	 * 
	 * @param maxCrewSize the maximum crew size
	 */
	public void setMaxCrewSize(int maxCrewSize) {
		this.maxCrewSize = maxCrewSize;
	}
	
	/**
	 * Gets the maximum cargo mass.
	 * 
	 * @return the maximum caro mass (kilograms)
	 */
	public double getMaxCargoMass() {
		return maxCargoMass;
	}
	
	/**
	 * Sets the maximum cargo mass.
	 * 
	 * @param maxCargoMass the maximum cargo mass (kilograms)
	 */
	public void setMaxCargoMass(double maxCargoMass) {
		this.maxCargoMass = maxCargoMass;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.edge.Edge#getEdgeType()
	 */
	@Override
	public EdgeType getEdgeType() {
		return EdgeType.FLIGHT;
	}
}