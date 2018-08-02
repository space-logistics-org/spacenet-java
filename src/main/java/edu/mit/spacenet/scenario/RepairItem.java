/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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