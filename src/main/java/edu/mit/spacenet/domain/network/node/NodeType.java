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
