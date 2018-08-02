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