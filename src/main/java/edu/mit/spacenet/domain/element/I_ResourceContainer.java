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

import java.util.SortedMap;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;

/**
 * The I_ResourceContainer represents elements that can contain resources.
 * 
 * @author Paul Grogan
 */
public interface I_ResourceContainer extends I_Element {
	
	/**
	 * Determines if the resource can be added.
	 * 
	 * @param resource the resource to add
	 * @param amount the amount to add
	 * 
	 * @return true if it can be added, false otherwise
	 */
	public boolean canAdd(I_Resource resource, double amount);
	
	/**
	 * Attempts to add an amount of a resource to the container.
	 * 
	 * @param resource the resource to add
	 * @param amount the amount to add
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean add(I_Resource resource, double amount);
	
	/**
	 * Attempts to remove an amount of a resource from the container.
	 * 
	 * @param resource the resource to remove
	 * @param amount the amount to remove
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean remove(I_Resource resource, double amount);
	
	/**
	 * Gets the cargo environment.
	 * 
	 * @return the cargo environment
	 */
	public Environment getCargoEnvironment();
	
	/**
	 * Sets the cargo environment.
	 * 
	 * @param cargoEnvironment the cargo environment
	 */
	public void setCargoEnvironment(Environment cargoEnvironment);
	
	/**
	 * Gets the maximum cargo mass.
	 * 
	 * @return the maximum cargo mass (kilograms)
	 */
	public double getMaxCargoMass();
	
	/**
	 * Sets the maximum cargo mass.
	 * 
	 * @param maxCargoMass the maximum cargo mass (kilograms)
	 */
	public void setMaxCargoMass(double maxCargoMass);
	
	/**
	 * Gets the maximum cargo volume.
	 * 
	 * @return the maximum volume (cubic meters)
	 */
	public double getMaxCargoVolume();
	
	/**
	 * Sets the maximum cargo volume.
	 * 
	 * @param maxCargoVolume the maximum volume (cubic meters)
	 */
	public void setMaxCargoVolume(double maxCargoVolume);
	
	/**
	 * Gets the total volume of cargo items.
	 * 
	 * @return the cargo volume (cubic meters)
	 */
	public double getCargoVolume() ;
	
	/**
	 * Gets the total mass of cargo items.
	 * 
	 * @return the cargo mass (kilograms)
	 */
	public double getCargoMass();
	
	/**
	 * Gets the contents of the container.
	 * 
	 * @return the contents
	 */
	public SortedMap<I_Resource, Double> getContents();
}
