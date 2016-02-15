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
package edu.mit.spacenet.domain.element;

import javax.swing.ImageIcon;

/**
 * Enumeration that defines the four operational states for elements.
 * 
 * @author Paul Grogan
 */
public enum StateType { 
	
	/** The active state type is used for states where the element is in a typical or nominal active condition. */
	ACTIVE("Active", "icons/clock_play.png"), 

	/** The special state type is used for states where the element is in a special active condition. */
	SPECIAL("Special", "icons/clock_add.png"), 
	
	/** The quiescent state type is used for states where the element is only operational part of the time (duty cycle < 1). */
	QUIESCENT("Quiescent", "icons/clock_pause.png"), 
	
	/** The dormant state type is used for states where the element is operational for a small percentage of the time (duty cycle << 1). */
	DORMANT("Dormant", "icons/clock_stop.png"), 
	
	/** The decommissioned state type is used for states where the element is not operational and will not become operational in the future. This state type is reserved for cannibalization of spare parts. */
	DECOMMISSIONED("Decommissioned", "icons/clock_red.png");
	
	private String name;
	private ImageIcon icon;
	
	private StateType(String name, String iconUrl) { 
		this.name = name; 
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the textual representation of the state type.
	 * 
	 * @return the state type name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() { 
		return name;
	}
	
	/**
	 * Gets the icon representation of the state type.
	 * 
	 * @return the state type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Method to get a particular instance of a state type based on a
	 * case-insensitive string.
	 * 
	 * @param name 	case-insensitive string of name
	 * 
	 * @return the state type, returns null if unmatched
	 */
	public static StateType getInstance(String name) {
		for(StateType t : values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) 
				return t;
		}
		return null;
    }
}
