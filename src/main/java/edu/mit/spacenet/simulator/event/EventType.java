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
package edu.mit.spacenet.simulator.event;

import javax.swing.ImageIcon;

/**
 * Enumeration which represents the different concrete classes of events.
 * 
 * @author Paul Grogan
 */
public enum EventType {
	
	/** The create event type. */
	CREATE("Create Elements", "icons/add.png"), 
	
	/** The add event type. */
	ADD("Add Resources", "icons/add.png"), 
	
	/** The move event type. */
	MOVE("Move Elements", "icons/arrow_right.png"),
	
	/** The transfer event type. */
	TRANSFER("Transfer Resources", "icons/arrow_right.png"),
	
	/** The remove event type. */
	REMOVE("Remove Elements", "icons/delete.png"),
	
	/** The reconfigure event type. */
	RECONFIGURE("Reconfigure Element", "icons/arrow_switch.png"),
	
	/** The reconfigure group event type. */
	RECONFIGURE_GROUP("Reconfigure Group", "icons/arrow_switch.png"),
	
	/** The demand event type. */
	DEMAND("Demand Resources", "icons/comment.png"),
	
	/** The burn event type. */
	BURN("Propulsive Burn", "icons/flame.png"), 
	
	/** The EVA event type. */
	EVA("Crewed EVA", "icons/door_open.png"),
	
	/** The exploration process type. */
	EXPLORATION("Crewed Exploration", "icons/flag_blue.png"),
	
	/** The space transport type. */
	SPACE_TRANSPORT("Space Transport", "icons/flag_red.png"),
	
	/** The surface transport type. */
	SURFACE_TRANSPORT("Surface Transport", "icons/flag_green.png"),
	
	/** The flight transport. */
	FLIGHT_TRANSPORT("Flight Transport", "icons/flag_yellow.png");
	
	private String name;
	private ImageIcon icon;
	
	private EventType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the event type.
	 * 
	 * @return the event type name
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
	 * Gets the icon representation for the event type.
	 * 
	 * @return the event type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
}
