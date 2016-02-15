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

import java.util.SortedSet;

import edu.mit.spacenet.domain.I_DomainType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * The I_State interface represents an element's operational state, which
 * contains a set of demand models (to drive demands) and a duty cycle.
 * 
 * @author Paul Grogan
 */
public interface I_State extends I_DomainType, Comparable<I_State> {
	
	/**
	 * Gets the state type.
	 * 
	 * @return the state type
	 */
	public StateType getStateType();
	
	/**
	 * Sets the state type.
	 * 
	 * @param stateType the state type
	 */
	public void setStateType(StateType stateType);
	
	/**
	 * Gets the set of demand models.
	 * 
	 * @return the set of demand models
	 */
	public SortedSet<I_DemandModel> getDemandModels();
	
	/**
	 * Sets the set of demand models.
	 * 
	 * @param demandModels the set of demand models
	 */
	public void setDemandModels(SortedSet<I_DemandModel> demandModels);
	
	/**
	 * Generates the set of aggregate demands from the demand models for a
	 * specified duration (in days).
	 * 
	 * @param duration the duration (in days) to generate for
	 * @param simulator the simulator requesting the demands
	 * 
	 * @return the aggregated set of demands
	 */
	public DemandSet generateDemands(double duration, I_Simulator simulator);
}
