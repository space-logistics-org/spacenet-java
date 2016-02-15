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
 * Demand model that generates an impulse demand on its first call to generate
 * demands, and nothing afterwards.
 * 
 * @author Paul Grogan
 */
public class TimedImpulseDemandModel extends AbstractDemandModel {
	private SortedSet<Demand> demands;
	private boolean flag = false;
	
	/**
	 * The default constructor initializes the set of demands.
	 */
	public TimedImpulseDemandModel() {
		demands = new TreeSet<Demand>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double, edu.mit.spacenet.simulator.I_Simulator)
	 */
	public DemandSet generateDemands(double duration, I_Simulator simulator) {
		if(!flag) {
			flag = true;
			DemandSet d = new DemandSet();
			for(Demand demand : demands) {
				d.add(demand);
			}
			return d;
		} else return new DemandSet();
	}
	
	/**
	 * Gets the set of demands to generate.
	 * 
	 * @return the set of demands
	 */
	public SortedSet<Demand> getDemands() {
		return demands;
	}
	
	/**
	 * Sets the set of demands to generate.
	 * 
	 * @param demands the set of demands
	 */
	public void setDemands(SortedSet<Demand> demands) {
		this.demands = demands;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
	 */
	public DemandModelType getDemandModelType() {
		return DemandModelType.TIMED_IMPULSE;
	}
}
