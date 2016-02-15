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
import java.util.TreeSet;

import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Class that defines an operational state of an element. An operational state
 * is composed of a state type, one or more demand models, and an overall duty
 * cycle.
 * 
 * @author Paul Grogan
 */
public class State extends DomainType implements I_State {
	private StateType stateType;
	private SortedSet<I_DemandModel> demandModels;
	
	/**
	 * The default constructor sets the default values for name to Default,
	 * state type to ACTIVE, and duty cycle to 1. It also initializes the set of
	 * demand models, but does not add any.
	 */
	public State() {
		super();
		setName("Default");
		setStateType(StateType.ACTIVE);
		this.demandModels = new TreeSet<I_DemandModel>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_State#getStateType()
	 */
	public StateType getStateType() {
		return stateType;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_State#setStateType(edu.mit.spacenet.domain.element.StateType)
	 */
	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_State#getDemandModels()
	 */
	public SortedSet<I_DemandModel> getDemandModels() {
		return demandModels;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_State#setDemandModels(java.util.SortedSet)
	 */
	public void setDemandModels(SortedSet<I_DemandModel> demandModels) {
		this.demandModels = demandModels;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_State#generateDemands(double, edu.mit.spacenet.simulator.I_Simulator)
	 */
	public DemandSet generateDemands(double duration, I_Simulator simulator) {
		DemandSet demands = new DemandSet();
		for(I_DemandModel model : demandModels) {
			for(Demand demand : model.generateDemands(duration, simulator)) {
				demands.add(demand);
			}
		}
		return demands;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(I_State state) {
		if(getStateType().equals(state.getStateType())){
			if(getName().equals(state.getName())) {
				return new Integer(getTid()).compareTo(new Integer(state.getTid()));
			} else {
				return getName().compareTo(state.getName());
			}
		} else {
			return getStateType().compareTo(state.getStateType());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
}