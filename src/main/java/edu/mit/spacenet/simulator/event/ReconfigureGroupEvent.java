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

import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * An event that reconfigures a group of elements to a new operational state
 * type.
 */
public class ReconfigureGroupEvent extends AbstractEvent {
	private SortedSet<I_Element> elements;
	private StateType stateType;
	
	/**
	 * The default constructor.
	 */
	public ReconfigureGroupEvent() {
		super();
		elements = new TreeSet<I_Element>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(elements.size() == 0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
				"No elements defined."));
		} else {
			for(I_Element element : elements) {
				I_State state = null;
				for(I_State s : element.getStates()) {
					if(s.getStateType().equals(stateType)) {
						state = s;
						break;
					}
				}
				
				if(element.getLocation()==null) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " was not found.");
				} else if(!getLocation().equals(element.getLocation())) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " is located at " + element.getLocation() + " instead of " + getLocation() + ".");
				} else if(state==null) {
					simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
						"No " + stateType + " state for " + element + "."));
				} else if(element.setCurrentState(state)) {
					System.out.printf("%.3f: %s\n", 
							getTime(), "Reconfiguring " + element + " to " + state + " state");
				} else {
					throw new SimSpatialError(simulator.getTime(), this, 
							"Element " + element + " does not contain state " + state + ".");
				}
			}
		}
	}
	
	/**
	 * Gets the elements to reconfigure.
	 * 
	 * @return the elements to reconfigure
	 */
	public SortedSet<I_Element> getElements() {
		return elements;
	}
	
	/**
	 * Sets the elements to reconfigure.
	 * 
	 * @param elements the elements to reconfigure
	 */
	public void setElements(SortedSet<I_Element> elements) {
		this.elements = elements;
	}
	
	/**
	 * Gets the state type to reconfigure to.
	 * 
	 * @return the state type to reconfigure to
	 */
	public StateType getStateType() {
		return stateType;
	}
	
	/**
	 * Sets the state type to reconfigure to.
	 * 
	 * @param stateType the state type to reconfigure to
	 */
	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.RECONFIGURE_GROUP;
	}
}