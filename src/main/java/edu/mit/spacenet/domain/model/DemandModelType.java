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
package edu.mit.spacenet.domain.model;

import javax.swing.ImageIcon;

/**
 * An enumeration for the demand model types.
 * 
 * @author Paul Grogan
 */
public enum DemandModelType {
	
	/** The crew consumables model type. */
	CREW_CONSUMABLES("Crew Consumables Demand Model", "icons/comment.png"),
	
	/** The timed impulse model type. */
	TIMED_IMPULSE("Timed Impulse Demand Model", "icons/comment.png"),
	
	/** The rated model type. */
	RATED("Rated Demand Model", "icons/comment.png"),
	
	/** The sparing by mass model type. */
	SPARING_BY_MASS("Sparing by Mass Demand Model", "icons/comment.png");
	
	private String name;
	private ImageIcon icon;
	
	private DemandModelType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the demand model type.
	 * 
	 * @return the demand model type name
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
	 * Gets the icon for the demand model type.
	 * 
	 * @return the demand model type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Gets the enumeration value based on a passed name.
	 * 
	 * @param name the name to match
	 * 
	 * @return the matching demand model type (null if no match found)
	 */
	public static DemandModelType getInstance(String name) {
		for(DemandModelType t : DemandModelType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) {
				return t;
			}
		}
		return null;
	}
}
