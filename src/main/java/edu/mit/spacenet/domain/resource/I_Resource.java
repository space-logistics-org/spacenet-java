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
package edu.mit.spacenet.domain.resource;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.I_DomainType;

/**
 * Interface that represents any supply item (discrete or continuous).
 * 
 * @author Paul Grogan
 */
public interface I_Resource extends I_DomainType, Comparable<I_Resource> {
	
	/**
	 * Gets the class of supply.
	 * 
	 * @return the class of supply
	 */
	public ClassOfSupply getClassOfSupply();
	
	/**
	 * Sets the class of supply.
	 * 
	 * @param classOfSupply the class of supply
	 */
	public void setClassOfSupply(ClassOfSupply classOfSupply);
	
	/**
	 * Gets the resource environment.
	 * 
	 * @return the environment
	 */
	public Environment getEnvironment();
	
	/**
	 * Sets the resource environment.
	 * 
	 * @param environment the environment
	 */
	public void setEnvironment(Environment environment);
	
	/**
	 * Gets the packing factor, which is the factor of additional COS 5 demands
	 * that is added to unpacked demand figures.
	 * 
	 * @return the packing factor
	 */
	public double getPackingFactor();
	
	/**
	 * Sets the packing factor, which is the factor of additional COS 5 demands
	 * that is added to unpacked demand figures.
	 * 
	 * @param factor the packing factor
	 */
	public void setPackingFactor(double factor);
	
	/**
	 * Gets the units of measure.
	 * 
	 * @return the units of measure
	 */
	public String getUnits();
	
	/**
	 * Sets the units of measure.
	 * 
	 * @param units the units of measure
	 */
	public void setUnits(String units);
	
	/**
	 * Gets the unit mass.
	 * 
	 * @return the unit mass (kilograms per unit of consumption)
	 */
	public double getUnitMass();
	
	/**
	 * Sets the unit mass.
	 * 
	 * @param unitMass the unit mass
	 */
	public void setUnitMass(double unitMass);
	
	/**
	 * Gets the unit volume.
	 * 
	 * @return the unit volume (cubic meters per unit)
	 */
	public double getUnitVolume();
	
	/**
	 * Sets the unit volume.
	 * 
	 * @param unitVolume the unit volume (cubic meters per unit)
	 */
	public void setUnitVolume(double unitVolume);
	
	/**
	 * Checks whether this resource is substitutable (same or higher level of
	 * fidelity) for a given resource during demand consumption.
	 * 
	 * @param resource the resource to check for substitutability
	 * 
	 * @return true if this can be substituted for resource, false otherwise
	 */
	public boolean isSubstitutableFor(I_Resource resource);
	
	/**
	 * Gets the resource type.
	 * 
	 * @return the resource type
	 */
	public ResourceType getResourceType();
}