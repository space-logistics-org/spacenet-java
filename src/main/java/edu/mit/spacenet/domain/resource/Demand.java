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

import java.text.DecimalFormat;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * Represents a demanded resource and the demanded amount.
 * 
 * @author Paul Grogan
 */
public class Demand implements Comparable<Demand> {
	private I_Resource resource;
	private double amount;
	
	/**
	 * The default constructor.
	 */
	public Demand() {
		
	}
	
	/**
	 * Gets the mass of the associated demand.
	 * 
	 * @return the mass of the associated demand (kilograms)
	 */
	public double getMass() {
		if(resource==null) return 0;
		else return GlobalParameters.getRoundedMass(resource.getUnitMass()*amount);
	}
	
	/**
	 * Gets the volume of the associated demand.
	 * 
	 * @return the volume of the associated demand (cubic meters)
	 */
	public double getVolume() {
		if(resource==null) return 0;
		else return GlobalParameters.getRoundedVolume(resource.getUnitVolume()*amount);
	}
	
	/**
	 * A constructor that sets the resource and amount.
	 * 
	 * @param resource the resource
	 * @param amount the amount
	 */
	public Demand(I_Resource resource, double amount) {
		super();
		setResource(resource);
		setAmount(amount);
	}
	
	/**
	 * Gets the demanded resource.
	 * 
	 * @return the resource
	 */
	public I_Resource getResource() {
		return resource;
	}
	
	/**
	 * Sets the demanded resource.
	 * 
	 * @param resource the resource
	 */
	public void setResource(I_Resource resource) {
		this.resource = resource;
	}
	
	/**
	 * Gets the demanded amount.
	 * 
	 * @return the demanded amount (units of resource)
	 */
	public double getAmount() {
		return GlobalParameters.getRoundedDemand(amount);
	}
	
	/**
	 * Sets the demanded amount.
	 * 
	 * @param amount the demanded amount (units of resource)
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.00");
		if(resource == null) {
			return format.format(getAmount()) + " units of unknown";
		} else {
			return format.format(getAmount()) + " " + resource.getUnits() + " of " + resource.getName();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Demand demand) {
		if(getResource() == null) {
			if(demand.getResource() == null) return 0;
			else return -1;
		} else {
			return getResource().compareTo(demand.getResource());
		}
	}
}