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
package edu.mit.spacenet.domain.model;

import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Demand model that generates an amount of a set demands proportional to a rate
 * constant. For discrete items, it aggregates the fractional items until at
 * least a whole unit can be demanded.
 * 
 * @author Paul Grogan
 */
public class RatedDemandModel extends AbstractDemandModel {
	private SortedSet<Demand> demandRates;
	
	/**
	 * Default constructor that initializes the demand rates and item
	 * generation structures.
	 */
	public RatedDemandModel() {
		demandRates = new TreeSet<Demand>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double, edu.mit.spacenet.simulator.I_Simulator)
	 */
	public DemandSet generateDemands(double duration, I_Simulator simulator) {
		DemandSet demands = new DemandSet();
		for(Demand demand : demandRates) {
			Demand resource = new Demand();
			resource.setResource(demand.getResource());
			resource.setAmount(demand.getAmount()*duration);
			demands.add(resource);
		}
		return demands;
	}
	
	/**
	 * Gets the set of demand rates.
	 * 
	 * @return the set of demands with rate constant in place of amount
	 */
	public SortedSet<Demand> getDemandRates() {
		return demandRates;
	}
	
	/**
	 * Sets the set of demand rates.
	 * 
	 * @param demandRates the set of demands with rate constant in place of
	 * amount
	 */
	public void setDemandRates(SortedSet<Demand> demandRates) {
		this.demandRates = demandRates;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
	 */
	public DemandModelType getDemandModelType() {
		return DemandModelType.RATED;
	}
}