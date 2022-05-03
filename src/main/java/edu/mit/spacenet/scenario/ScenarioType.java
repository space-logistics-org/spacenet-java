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
package edu.mit.spacenet.scenario;

import javax.swing.ImageIcon;

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * An enumeration of the different types of scenarios used for network filtering.
 * 
 * @author Paul Grogan
 */
public enum ScenarioType { 
	
	/** An ISS scenario selects all of the EARTH surface and space nodes by default. */
	ISS("ISS", "icons/earth_icon.png"), 
	
	/** A Lunar scenario selects all of the EARTH and MOON surface and space nodes by default. */
	LUNAR("Lunar", "icons/earth_moon_icon.png"), 
	
	/** A Moon-only scenario selects all of the MOON surface and space nodes by default. */
	MOON_ONLY("Moon-only", "icons/moon_icon.png"),
	
	/** A Martian scenario selects all of the EARTH and MARS surface and space nodes by default. */
	MARTIAN("Martian", "icons/earth_mars_icon.png"), 
	
	/** A Mars-only scenario selects all of the MARS surface and space nodes by default. */
	MARS_ONLY("Mars-only", "icons/mars_icon.png"),
	
	/** A scenario that selects all nodes by default. */
	SOLAR_SYSTEM("Solar System", "icons/solar_icon.png");
	
	private String name;
	private ImageIcon icon;
	
	private ScenarioType(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the scenario type.
	 * 
	 * @return the scenario type name
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
	 * Gets the icon of the scenario type.
	 * 
	 * @return the scenario type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Method to get a particular instance of a scenario type based on a
	 * case-insensitive string.
	 * 
	 * @param name 	case-insensitive string of name
	 * 
	 * @return the scenario type, returns null if unknown name
	 */
	public static ScenarioType getInstance(String name) {
        if(name.equals(ISS.getName())) return ISS;
        else if(name.equals(LUNAR.getName())) return LUNAR;
        else if(name.equals(MOON_ONLY.getName())) return MOON_ONLY;
        else if(name.equals(MARTIAN.getName())) return MARTIAN;
        else if(name.equals(MARS_ONLY.getName())) return MARS_ONLY;
        else return null;
    }
	
	/**
	 * Gets whether a node is visible under this scenario type.
	 * 
	 * @param node the node to check
	 * 
	 * @return whether the node is visible
	 */
	public boolean isNodeVisible(Node node) {
		switch(this) {
		case ISS:
			if(node.getBody()==Body.EARTH) return true;
			else return false;
		case LUNAR:
			if(node.getBody()==Body.EARTH || node.getBody()==Body.MOON) return true;
			else return false;
		case MOON_ONLY:
			if(node.getBody()==Body.MOON) return true;
			else return false;
		case MARTIAN:
			if(node.getBody()==Body.EARTH || node.getBody()==Body.MARS) return true;
			else return false;
		case MARS_ONLY:
			if(node.getBody()==Body.MARS) return true;
			else return false;
		case SOLAR_SYSTEM:
			return true;
		}
		return false;
	}
	
	/**
	 * Gets whether an edge is visible under this scenario type.
	 * 
	 * @param edge the edge to check
	 * 
	 * @return whether the edge is visible
	 */
	public boolean isEdgeVisible(Edge edge) {
		if(isNodeVisible(edge.getOrigin()) 
				&& isNodeVisible(edge.getDestination())) return true;
		else return false;
	}
}
