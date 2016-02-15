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
package edu.mit.spacenet.scenario;

/**
 * Enumeration representing the different methods of item discretization.
 * 
 * @author Paul Grogan
 */
public enum ItemDiscretization {
	
	/** Items are not discretized and are treated as continuous resources. */
	NONE("None"),
	
	/** Items are discretized on a per-element basis. */
	BY_ELEMENT("Element"),
	
	/** Items are discretized on a per-location basis. */
	BY_LOCATION("Location"),
	
	/** Items are discretized on a global basis. */
	BY_SCENARIO("Scenario");
	
	private String name;
	
	private ItemDiscretization(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of the item discretization.
	 * 
	 * @return the item discretization name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return getName();
	}
}