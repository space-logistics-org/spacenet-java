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
 * A factory for creating elements.
 * 
 * @author Paul Grogan
 */
public abstract class ElementFactory {
	
	/**
	 * Factory method that creates a new element based on the passed element
	 * type. Throws a runtime exception if an unsupported element type is used.
	 * 
	 * @param type the element type to create
	 * 
	 * @return the newly-created element
	 */
	public static I_Element createElement(ElementType type) {
		switch(type) {
		case ELEMENT: return new Element();
		case CREW_MEMBER: return new CrewMember();
		case RESOURCE_CONTAINER: return new ResourceContainer();
		case RESOURCE_TANK: return new ResourceTank();
		case CARRIER: return new Carrier();
		case SURFACE_VEHICLE: return new SurfaceVehicle();
		case PROPULSIVE_VEHICLE: return new PropulsiveVehicle();
		default: throw new RuntimeException("Unsupported Element");
		}
	}
}
