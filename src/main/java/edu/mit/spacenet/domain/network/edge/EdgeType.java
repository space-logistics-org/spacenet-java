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

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Enumeration to represent the different subclasses of edges.
 * 
 * @author Paul Grogan
 */
public enum EdgeType {
	
	/** The flight edge type. */
	FLIGHT("Flight", "icons/edge_yellow.png", Color.YELLOW), 
	
	/** The space edge type. */
	SPACE("Space", "icons/edge_red.png", Color.RED),
	
	/** The surface edge type. */
	SURFACE("Surface", "icons/edge_green.png", Color.GREEN),
	
	/** The time dependent edge type. */
	TIME_DEPENDENT("Time-Dependent", "icons/edge_red.png", Color.RED);
	
	private String name;
	private ImageIcon icon;
	private Color color;
	
	private EdgeType(String name, String iconUrl, Color color) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
		this.color = color;
	}
	
	/**
	 * Gets the name of the edge type.
	 * 
	 * @return the edge type name
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
	 * Gets the icon of the edge type.
	 * 
	 * @return the edge type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Gets the color of the edge type (used in visualizations).
	 * 
	 * @return the edge type color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Gets the edge type based on a given name.
	 * 
	 * @param name the edge type name
	 * 
	 * @return the edge type, return null if not found
	 */
	public static EdgeType getInstance(String name) {
		for(EdgeType t : EdgeType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) {
				return t;
			}
		}
		return null;
	}
}
