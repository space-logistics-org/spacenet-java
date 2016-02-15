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

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_Container;

/**
 * The I_Carrier interface represents objects that implement both the I_Element
 * and the I_Container interfaces (that is, elements that can contain other
 * elements).
 * 
 * @author Paul Grogan
 */
public interface I_Carrier extends I_Element, I_Container {
	
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
	 * Gets the maximum crew size.
	 * 
	 * @return the maximum crew size
	 */
	public int getMaxCrewSize();
	
	/**
	 * Sets the maximum crew size.
	 * 
	 * @param maxCrewSize the maximum crew size
	 */
	public void setMaxCrewSize(int maxCrewSize);
}
