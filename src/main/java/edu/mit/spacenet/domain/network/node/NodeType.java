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
package edu.mit.spacenet.domain.network.node;

import javax.swing.ImageIcon;

/**
 * An enumeration that represents the different subclasses of nodes.
 * 
 * @author Paul Grogan
 */
public enum NodeType {
	/** The lagrange node type. */
	LAGRANGE("Lagrange", "icons/bullet_purple.png"), 
	
	/** The orbital node type. */
	ORBITAL("Orbital", "icons/bullet_red.png"),
	
	/** The surface node type. */
	SURFACE("Surface", "icons/bullet_yellow.png"), ;
	
	private String name;
	private ImageIcon icon;
	
	private NodeType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the node type.
	 * 
	 * @return the node type name
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
	 * Gets the icon of the node type.
	 * 
	 * @return the node type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Gets the node type instance based on a passed name.
	 * 
	 * @param name the node type name
	 * 
	 * @return the node type, null if not found
	 */
	public static NodeType getInstance(String name) {
		for(NodeType t : NodeType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) {
				return t;
			}
		}
		return null;
	}
}
