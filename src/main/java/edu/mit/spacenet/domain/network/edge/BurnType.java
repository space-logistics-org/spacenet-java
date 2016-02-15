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

import java.io.Serializable;

/**
 * Enumeration of the two types of propulsive burns (OMS for Orbit Maneuvering
 * System, RCS for Reaction Control System).
 * 
 * @author Paul Grogan
 */
public enum BurnType implements Serializable { 
	
	/** Abbreviation for Orbit Maneuvering System. */
	OMS("OMS"), 
	
	/** Abbreviation for Reaction Control System. */
	RCS("RCS"); 
	
	private String name;
	
	/**
	 * The default constructor.
	 * @param name	the name of the burn type to display
	 */
	private BurnType(String name) { 
		this.name = name;
	}
	
	/**
	 * Gets the name of the burn type.
	 * 
	 * @return the burn type name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() { 
		return name;
	}
	
	/**
	 * Method to get a particular instance of a state type based on a
	 * case-insensitive string.
	 * 
	 * @param name 	case-insensitive string of name
	 * 
	 * @return the burn type, returns RCS if unknown name
	 */
	public static BurnType getInstance(String name) {
        if(name.toLowerCase().equals(OMS.getName().toLowerCase())) return OMS;
        else return RCS;
    }
}