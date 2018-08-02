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
package edu.mit.spacenet.simulator.event;

import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimWarning;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Simple event that pushes a mission's events onto the simulation stack.
 * 
 * @author Paul Grogan
 */
public class MissionEvent extends AbstractEvent {
	private Mission mission;
	
	/**
	 * The default constructor.
	 */
	public MissionEvent() {
		super();
	}
	
	/**
	 * Gets the mission.
	 * 
	 * @return the mission
	 */
	public Mission getMission() {
		return mission;
	}
	
	/**
	 * Sets the mission.
	 * 
	 * @param mission the mission
	 */
	public void setMission(Mission mission) {
		this.mission = mission;
		setLocation(mission.getOrigin());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) {
		if(mission.getEventList().size()==0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
					"No events defined."));
		}
		
		System.out.printf("%.3f: %s\n", 
				getTime(), "Commencing Mission: " + mission.getName());
		for(I_Event e : mission.getEventList()) {
			e.setTime(e.getTime() + getTime());
			simulator.schedule(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		System.out.println("Mission: " + mission.getName());
		for(I_Event e : mission.getEventList()) {
			e.print(tabOrder+1);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#getName()
	 */
	@Override
	public String getName() {
		return mission.getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#getTime()
	 */
	@Override
	public double getTime() {
		return DateFunctions.getDaysBetween(mission.getStartDate(), mission.getScenario().getStartDate());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return null;
	}
}