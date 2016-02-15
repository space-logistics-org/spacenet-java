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
package edu.mit.spacenet.simulator;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.scenario.Scenario;

/**
 * A simulator that logs an element history.
 * 
 * @author Paul Grogan
 */
public class MiniSimulator extends AbstractSimulator {
	private List<SimState> locationHistory;
	
	/**
	 * The constructor sets the scenario and initializes the location history.
	 * 
	 * @param scenario the scenario
	 */
	public MiniSimulator(Scenario scenario) {
		super(scenario);
		locationHistory = new ArrayList<SimState>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.simulator.I_Simulator#simulate()
	 */
	public void simulate() {
		initializeSimulation();
		locationHistory.clear();
		
		// simulate events, serializing and saving after each time step
		while(getEvents().peek() != null) {
			getNextEvent();
			
			locationHistory.add(new SimState(getTime(), getScenario().getNetwork()));
			handleDemands(); //TODO 7/12/2010: added demand cycle in for pre-manifested resources (delta-v feasibility en route)
			executeEvent();
			locationHistory.add(new SimState(getTime(), getScenario().getNetwork()));
		}
	}
	
	/**
	 * Gets a list of states representing the location history during the
	 * simulation.
	 * 
	 * @return the list of states
	 */
	public List<SimState> getLocationHistory() {
		return locationHistory;
	}
}