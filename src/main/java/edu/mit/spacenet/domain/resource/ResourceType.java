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
package edu.mit.spacenet.domain.resource;

import javax.swing.ImageIcon;

/**
 * An enumeration for the different subclasses of resources.
 * 
 * @author Paul Grogan
 */
public enum ResourceType {
	
	/** The generic resource type. */
	GENERIC("Generic", "icons/bullet_pink.png"), 
	
	/** The continuous resource type. */
	RESOURCE("Continuous", "icons/bullet_blue.png"),
	
	/** The discrete resource type. */
	ITEM("Discrete", "icons/bullet_wrench.png");
	
	private String name;
	private ImageIcon icon;
	
	private ResourceType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the resource type.
	 * 
	 * @return the resource type name
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
	 * Gets the icon of the resource type.
	 * 
	 * @return the resource type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Gets the resource type instance based on a passed name.
	 * 
	 * @param name the resource type name
	 * 
	 * @return the resource type, null if not found
	 */
	public static ResourceType getInstance(String name) {
		for(ResourceType t : ResourceType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) {
				return t;
			}
		}
		return null;
	}
}
