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
import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.Environment;

/**
 * A continuous resource.
 * 
 * @author Paul Grogan
 */
public class Resource extends DomainType implements I_Resource {
	private ClassOfSupply classOfSupply;
	private Environment environment;
	private String units;
	private double unitMass;
	private double unitVolume;
	private double packingFactor;
	
	/**
	 * The default constructor that sets a default name, sets the default class
	 * of supply to COS 0, sets the environment to unpressurized, and sets
	 * default units to kilograms.
	 */
	public Resource() {
		super();
		setName("New " + getClass().getSimpleName());
		setClassOfSupply(ClassOfSupply.COS0);
		setEnvironment(Environment.UNPRESSURIZED);
		setUnitMass(1);
		setUnits("kg");
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getClassOfSupply()
	 */
	public ClassOfSupply getClassOfSupply() {
		return classOfSupply;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setClassOfSupply(edu.mit.spacenet.domain.ClassOfSupply)
	 */
	public void setClassOfSupply(ClassOfSupply classOfSupply) {
		this.classOfSupply = classOfSupply;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getEnvironment()
	 */
	public Environment getEnvironment() {
		return environment;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setEnvironment(edu.mit.spacenet.domain.Environment)
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getUnits()
	 */
	public String getUnits() {
		return units;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setUnits(java.lang.String)
	 */
	public void setUnits(String units) {
		this.units = units;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getUnitMass()
	 */
	public double getUnitMass() {
		return unitMass;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setUnitMass(double)
	 */
	public void setUnitMass(double unitMass) {
		this.unitMass = unitMass;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getUnitVolume()
	 */
	public double getUnitVolume() {
		return unitVolume;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setUnitVolume(double)
	 */
	public void setUnitVolume(double unitVolume) {
		this.unitVolume = unitVolume;
	}
	
	/**
	 * Gets the total mass.
	 * 
	 * @return the total mass
	 */
	public double getTotalMass() {
		return getUnitMass();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(super.equals(object) && object instanceof I_Resource) {
			return getEnvironment().equals(((I_Resource)object).getEnvironment());
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(I_Resource resource) {
		if(getClassOfSupply().equals(resource.getClassOfSupply())) {
			if(getTid()==resource.getTid()) {
				return getEnvironment().compareTo(resource.getEnvironment());
			} else return new Integer(getTid()).compareTo(resource.getTid());
		} else return getClassOfSupply().compareTo(resource.getClassOfSupply());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#isSubstitutableFor(edu.mit.spacenet.domain.resource.I_Resource)
	 */
	public boolean isSubstitutableFor(I_Resource resource) {
		if(getTid() < 0) {
			// tid < 0 means this is a generic resource
			if(getClassOfSupply().equals(resource.getClassOfSupply())
					|| getClassOfSupply().isSubclassOf(resource.getClassOfSupply())) {
				// if this class of supply is the same as or more specific 
				// than target class of supply this resource can be substituted 
				// for target resource
				return true;
			} else {
				return false;
			}
		} else if(getTid() > 0) {
			// tid > 0 means this is a specific resource loaded from the database
			if(getTid() == resource.getTid()) {
				// if this type id is the same as the target type id, the two 
				// resources are mutually substitutable
				return true;
			} else {
				return false;
			}
		} else {
			// tid == 0 means this resource was created during execution
			System.out.println("Cannot currently use non-database resources in demands.");
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getPackingFactor()
	 */
	public double getPackingFactor() {
		return packingFactor;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#setPackingFactor(double)
	 */
	public void setPackingFactor(double packingFactor) {
		this.packingFactor = packingFactor;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.I_Resource#getResourceType()
	 */
	public ResourceType getResourceType() {
		return ResourceType.RESOURCE;
	}
}