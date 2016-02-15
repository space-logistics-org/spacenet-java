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


/**
 * The element type enumeration lists the different types of elements, typically
 * one per element subclass. The element type provides a default name and a
 * default icon.
 * 
 * @author Paul Grogan
 */
public enum ElementType {
	
	/** Used for elements of the Element class. */
	ELEMENT("Element", ElementIcon.BRICK), 
	
	/** Used for elements of the CrewMember class. */
	CREW_MEMBER("Crew Member", ElementIcon.USER),
	
	/** Used for elements of the ResourceContainer class. */
	RESOURCE_CONTAINER("Resource Container", ElementIcon.PACKAGE),
	
	/** Used for elements of the ResourceTank class. */
	RESOURCE_TANK("Resource Tank", ElementIcon.PACKAGE),
	
	/** Used for elements of the Carrier class. */
	CARRIER("Carrier", ElementIcon.LORRY),
	
	/** Used for elements of the PropulsiveVehicle class. */
	PROPULSIVE_VEHICLE("Propulsive Vehicle", ElementIcon.BRICK),
	
	/** Used for elements of the SurfaceVehicle class. */
	SURFACE_VEHICLE("Surface Vehicle", ElementIcon.CAR);
	
	private String name;
	private ElementIcon icon;
	
	private ElementType(String name, ElementIcon icon) {
		this.name = name;
		this.icon = icon;
	}
	
	/**
	 * Gets the text representation of the element type.
	 * 
	 * @return the element type name
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
	 * Gets the icon used as the default for elements of this type.
	 * 
	 * @return the default icon
	 */
	public ElementIcon getIconType() {
		return icon;
	}
	
	/**
	 * Gets an element type instance based on a give name.
	 * 
	 * @param name the element type name to match
	 * 
	 * @return the element type, null if no match was found.
	 */
	public static ElementType getInstance(String name) {
		for(ElementType t : ElementType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) {
				return t;
			}
		}
		return null;
	}
}
