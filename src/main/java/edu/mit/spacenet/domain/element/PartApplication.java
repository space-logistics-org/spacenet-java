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

import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Defines a relationship between an element and a a part object that includes
 * several modifying quantities.
 * 
 * @author Paul Grogan
 */
public class PartApplication extends DomainType implements Comparable<PartApplication> {
	private Item partType;
	private double meanTimeToFailure;
	private double meanTimeToRepair;
	private double massToRepair;
	private double quantity;
	private double dutyCycle;
	
	/**
	 * Default constructor sets the mean time to failure to 0, the mean time to
	 * repair to 0, the quantity to 1, and the duty cycle to 1.
	 */
	public PartApplication() {
		super();
		partType = new Item();
		setMeanTimeToFailure(0);
		setMeanTimeToRepair(0);
		setQuantity(1);
		setDutyCycle(1);
	}
	
	/**
	 * Gets the part.
	 * 
	 * @return the part
	 */
	public I_Item getPart() {
		return partType;
	}
	
	/**
	 * Sets the part.
	 * 
	 * @param partType the part type
	 */
	public void setPart(Item partType) {
		this.partType = partType;
	}
	
	/**
	 * Gets the unit mean time to repair.
	 * 
	 * @return the unit mean time to repair (hours per kilogram)
	 */
	public double getUnitMeanTimeToRepair() {
		return getMeanTimeToRepair()/partType.getUnitMass();
	}
	
	/**
	 * Gets the mean time to failure.
	 * 
	 * @return the mean time to failure (hours), does not fail if = 0
	 */
	public double getMeanTimeToFailure() {
		return meanTimeToFailure;
	}
	
	/**
	 * Sets the mean time to failure.
	 * 
	 * @param meanTimeToFailure the mean time to failure (hours), does not fail
	 * if < 0
	 */
	public void setMeanTimeToFailure(double meanTimeToFailure) {
		this.meanTimeToFailure = meanTimeToFailure;
	}
	
	/**
	 * Gets the mean time to repair the part.
	 * 
	 * @return the mean time to repair (hours), not repairable if = 0
	 */
	public double getMeanTimeToRepair() {
		return meanTimeToRepair;
	}
	
	/**
	 * Sets the mean time required to repair the part.
	 * 
	 * @param meanTimeToRepair the mean time to repair(hours), not repairable
	 * if < 0
	 */
	public void setMeanTimeToRepair(double meanTimeToRepair) {
		this.meanTimeToRepair = meanTimeToRepair;
	}
	
	/**
	 * Gets the mass of generic COS 4 required to perform a repair.
	 * 
	 * @return the mass required to repair (kilograms)
	 */
	public double getMassToRepair() {
		return massToRepair;
	}
	
	/**
	 * Sets the mass of generic COS 4 required to perform a repair.
	 * 
	 * @param massToRepair the mass required to repair (kilograms)
	 */
	public void setMassToRepair(double massToRepair) {
		this.massToRepair = massToRepair;
	}
	
	/**
	 * Gets the duty cycle.
	 * 
	 * @return the duty cycle (percent)
	 */
	public double getDutyCycle() {
		return dutyCycle;
	}
	
	/**
	 * Sets the duty cycle.
	 * 
	 * @param dutyCycle the duty cycle (percent)
	 */
	public void setDutyCycle(double dutyCycle) {
		this.dutyCycle = dutyCycle;
	}
	
	/**
	 * Gets the quantity.
	 * 
	 * @return the quantity of parts
	 */
	public double getQuantity() {
		return quantity;
	}
	
	/**
	 * Sets the quantity.
	 * 
	 * @param quantity the quantity of parts
	 */
	public void setQuantity(double quantity) {
		this.quantity = GlobalParameters.getRoundedDemand(quantity);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(PartApplication partApplication) {
		if(getPart() == null) {
			if(partApplication.getPart() == null) return 0;
			else return -1;
		} else {
			return getPart().compareTo(partApplication.getPart());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return partType + " (" + ((int)getQuantity()) + ")";
	}
}
