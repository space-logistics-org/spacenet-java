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



import edu.mit.spacenet.domain.I_DomainType;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Interface for broad class of demand models. A demand model generates a set
 * of demands based on the duration of time that has elapsed.
 * 
 * @author Paul Grogan
 */
public interface I_DemandModel extends I_DomainType, Comparable<I_DemandModel> {
	
	/**
	 * Generates a set of demands based on the duration (in days) of time that
	 * has elapsed.
	 * 
	 * @param duration 	the duration (in days) to generate for
	 * @param simulator the simulator requesting the demands
	 * 
	 * @return the set of demands
	 */
	public DemandSet generateDemands(double duration, I_Simulator simulator);
	
	/**
	 * Gets the demand model type.
	 * 
	 * @return the demand model type
	 */
	public DemandModelType getDemandModelType();
}