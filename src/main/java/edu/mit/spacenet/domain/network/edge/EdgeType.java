/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
