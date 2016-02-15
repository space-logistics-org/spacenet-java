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
package edu.mit.spacenet.domain;

import java.util.SortedSet;

import edu.mit.spacenet.domain.element.I_Element;

/**
 * The interface for any object that can contain items.
 * 
 * @author Paul Grogan
 */
public interface I_Container extends I_DomainType {
	
	/**
	 * Returns the contents of this container.
	 * 
	 * @return the set of items
	 */
	public SortedSet<I_Element> getContents();
	
	/**
	 * Gets the complete contents of a location, including any nested elements
	 * at any level.
	 * 
	 * @return the set of elements
	 */
	public SortedSet<I_Element> getCompleteContents();
	
	/**
	 * Gets whether an element can be added.
	 * 
	 * @param element the element
	 * 
	 * @return true, if successful
	 */
	public boolean canAdd(I_Element element);
	
	/**
	 * Gets whether mass can be added to a nested element.
	 * 
	 * @param addedMass the added mass
	 * 
	 * @return true, if successful
	 */
	public boolean canAdd(double addedMass);
	
	/**
	 * Adds the item to the container.
	 * 
	 * @param element the element to add
	 * 
	 * @return 	true, if successful
	 */
	public boolean add(I_Element element);
	
	/**
	 * Removes the element from the container.
	 * 
	 * @param element the element to add
	 * 
	 * @return 		true, if successful
	 */
	public boolean remove(I_Element element);
	
	/**
	 * Gets the current cargo mass.
	 * 
	 * @return the cargo mass (kilograms)
	 */
	public double getCargoMass();
	
	/**
	 * Gets the total cargo mass including any nested elements and resources.
	 * 
	 * @return the total cargo mass (kilograms)
	 */
	public double getTotalMass();
	
	/**
	 * Gets the current number of crew members.
	 * 
	 * @return the number of crew members
	 */
	public int getCrewSize();
	
	/**
	 * Gets the total crew size including any nested elements.
	 * 
	 * @return the total number of crew members
	 */
	public int getTotalCrewSize();
	
	/**
	 * Gets the total volume of cargo elements.
	 * 
	 * @return the cargo volume (cubic meters)
	 */
	public double getCargoVolume();
}