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

import java.util.SortedMap;
import java.util.TreeMap;

import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * Event that represents an EVA excursion outside a crew vehicle. It includes
 * reconfigure events to/from the nominal states and move events to/from the
 * external location.
 * 
 * @author Paul Grogan
 */
public class EvaEvent extends AbstractEvent {
	private I_Carrier vehicle;
	private double evaDuration;
	private SortedMap<CrewMember, I_State> stateMap;
	private DemandSet demands;
	
	/**
	 * The default constructor initializes the demands set and EVA states map
	 * and defaults the EVA duration to 8 hours.
	 */
	public EvaEvent() {
		super();
		stateMap = new TreeMap<CrewMember, I_State>();
		demands = new DemandSet();
		setEvaDuration(8);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(stateMap.size()==0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
					"No crew members defined."));
		}
		if(vehicle == null) {
			throw new SimSpatialError(simulator.getTime(), this, 
					"No crew habitat defined.");
		}
		if(vehicle.getLocation()==null) {
			throw new SimSpatialError(simulator.getTime(), this, 
					vehicle + " was not found.");
		}
		if(!getLocation().equals(vehicle.getLocation())) {
			throw new SimSpatialError(simulator.getTime(), this, 
					vehicle + " is located at " + vehicle.getLocation() + " instead of " + getLocation() + ".");
		}
		for(CrewMember member : stateMap.keySet()) {
			if(member.getLocation()==null) {
				throw new SimSpatialError(simulator.getTime(), this, 
						member + " was not found.");
			} else if(!getLocation().equals(member.getLocation())) {
				throw new SimSpatialError(simulator.getTime(), this, 
						member + " is located at " + member.getLocation() + " instead of " + getLocation() + ".");
			}
		}
		
		System.out.printf("%.3f: %s\n", getTime(), "Commencing EVA at " + getLocation() + 
				" for " + getEvaDuration() + 
				" hours");
		
		if(simulator.getScenario().isDetailedEva()) {
			TreeMap<CrewMember, I_State> previousStates = new TreeMap<CrewMember, I_State>();
			for(CrewMember c : stateMap.keySet()) {
				if(stateMap.get(c) != null) {
					previousStates.put(c, c.getCurrentState());
					ReconfigureEvent r = new ReconfigureEvent();
					r.setTime(getTime());
					r.setPriority(getPriority());
					r.setParent(this);
					r.setLocation(getLocation());
					r.setElement(c);
					r.setState(stateMap.get(c));
					simulator.schedule(r);
				}
			}
			
			MoveEvent m1 = new MoveEvent();
			m1.setTime(getTime());
			m1.setPriority(getPriority());
			m1.setParent(this);
			m1.setLocation(getLocation());
			m1.setContainer(getLocation());
			for(CrewMember c : stateMap.keySet()) m1.getElements().add(c);
			simulator.schedule(m1);
			
			MoveEvent m2 = new MoveEvent();
			m2.setTime(getTime() + evaDuration/24);
			m2.setParent(this);
			m2.setLocation(getLocation());
			m2.setContainer(vehicle);
			for(CrewMember c : stateMap.keySet()) m2.getElements().add(c);
			simulator.schedule(m2);
			
			for(CrewMember c : stateMap.keySet()) {
				if(previousStates.get(c) != null) {
					ReconfigureEvent r = new ReconfigureEvent();
					r.setTime(getTime() + evaDuration/24);
					r.setParent(this);
					r.setLocation(getLocation());
					r.setElement(c);
					r.setState(previousStates.get(c));
					simulator.schedule(r);
				}
			}
			if(demands.size() > 0) {
				DemandEvent d = new DemandEvent();
				d.setTime(getTime() + evaDuration/24);
				d.setParent(this);
				d.setLocation(getLocation());
				d.setElement(vehicle);
				d.setDemands(demands);
				simulator.schedule(d);
			}
		}
	}
	
	/**
	 * Gets the crew member vehicle.
	 * 
	 * @return the crew member vehicle
	 */
	public I_Carrier getVehicle() {
		return vehicle;
	}
	
	/**
	 * Sets the crew member vehicle.
	 * 
	 * @param vehicle the crew member vehicle
	 */
	public void setVehicle(I_Carrier vehicle) {
		this.vehicle = vehicle;
	}
	
	/**
	 * Gets the duration of the EVA.
	 * 
	 * @return the duration (hours)
	 */
	public double getEvaDuration() {
		return evaDuration;
	}
	
	/**
	 * Sets the duration of the EVA.
	 * 
	 * @param evaDuration the duration (hours)
	 */
	public void setEvaDuration(double evaDuration) {
		this.evaDuration = evaDuration;
	}
	
	/**
	 * Gets the mapping of crew member's EVA states.
	 * 
	 * @return the map of crew member's EVA states
	 */
	public SortedMap<CrewMember, I_State> getStateMap() {
		return stateMap;
	}
	
	/**
	 * Sets the mapping of crew member's EVA states.
	 * 
	 * @param evaStates the mapping of crew member's EVA states
	 */
	public void setStateMap(SortedMap<CrewMember, I_State> evaStates) {
		this.stateMap = evaStates;
	}
	
	/**
	 * Gets the set of additional EVA demands.
	 * 
	 * @return the set of additional EVA demands
	 */
	public DemandSet getDemands() {
		return demands;
	}
	
	/**
	 * Sets the set of additional EVA demands.
	 * 
	 * @param demands the set of additional EVA demands
	 */
	public void setDemands(DemandSet demands) {
		this.demands = demands;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.EVA;
	}
}
