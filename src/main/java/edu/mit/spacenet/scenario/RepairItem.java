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
package edu.mit.spacenet.scenario;

import java.text.DecimalFormat;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.Demand;

/**
 * This class represents the repair of an item during simulation.
 * 
 * @author Paul Grogan
 */
public class RepairItem implements Comparable<RepairItem> {
	private Demand demand;
	private I_Element element;
	
	/**
	 * The constructor takes the demand that was eliminated by repair, and the
	 * element that received the repair.
	 * 
	 * @param demand the demand that was eliminated
	 * @param element the element receiving repair
	 */
	public RepairItem(Demand demand, I_Element element) {
		this.demand = demand;
		this.element = element;
	}
	
	/**
	 * Gets the demand eliminated by repair.
	 * 
	 * @return the demand
	 */
	public Demand getDemand() {
		return demand;
	}
	
	/**
	 * Gets the element repaired.
	 * 
	 * @return the repaired element
	 */
	public I_Element getElement() {
		return element;
	}
	
	/**
	 * Gets the unit mean repair time of the repair.
	 * 
	 * @return the unit mean repair time (hours per unit)
	 */
	public double getUnitMeanRepairTime() {
		for(PartApplication application : element.getParts()) {
			if(application.getPart().equals(demand.getResource())) {
				return application.getMeanTimeToRepair();
			}
		}
		return Double.MAX_VALUE;
	}
	
	/**
	 * Gets the mean repair time of the repair.
	 * 
	 * @return the repair time (hours)
	 */
	public double getMeanRepairTime() {
		return getUnitMeanRepairTime()*demand.getAmount();
	}
	
	/**
	 * Gets the unit amount of mass require for repair.
	 * 
	 * @return the amount of additional mass (kilograms per unit)
	 */
	public double getUnitMassToRepair() {
		for(PartApplication application : element.getParts()) {
			if(application.getPart().equals(demand.getResource())) {
				return application.getMassToRepair();
			}
		}
		return 0;
	}
	
	/**
	 * Gets the amount of mass required to perform the repair.
	 * 
	 * @return the amount of mass (kilograms)
	 */
	public double getMassToRepair() {
		return getUnitMassToRepair()*demand.getAmount();
	}
	
	/**
	 * Gets the value of the repair.
	 * 
	 * @return the repair value (hours used per kilogram saved)
	 */
	public double getRepairValue() {
		return getUnitMeanRepairTime()/(demand.getResource().getUnitMass() - getUnitMassToRepair());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.000");
		return demand + " (" + format.format(getMassToRepair()) + " kg, " + format.format(getMeanRepairTime()) + " hr)";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(RepairItem repairItem) {
		return new Double(getRepairValue()).compareTo(repairItem.getRepairValue());
	}
}