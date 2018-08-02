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

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * An event that reconfigures an element to a new operational state.
 * 
 * @author Paul Grogan
 */
public class ReconfigureEvent extends AbstractEvent {
	private I_Element element;
	private I_State state;
	
	/**
	 * The default constructor.
	 */
	public ReconfigureEvent() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(element == null) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
				"No element defined."));
		} else {
			if(element.getLocation()==null) {
				throw new SimSpatialError(simulator.getTime(), this, 
						element + " was not found.");
			} else if(!getLocation().equals(element.getLocation())) {
				throw new SimSpatialError(simulator.getTime(), this, 
						element + " is located at " + element.getLocation() + " instead of " + getLocation() + ".");
			} else if(element.setCurrentState(state)) {
				System.out.printf("%.3f: %s\n", 
						getTime(), "Reconfiguring " + element + " to " + state + " state");
			} else {
				throw new SimSpatialError(simulator.getTime(), this, 
						"Element " + element + " does not contain state " + state + ".");
			}
		}
	}
	
	/**
	 * Gets the element to reconfigure.
	 * 
	 * @return the element to reconfigure
	 */
	public I_Element getElement() {
		return element;
	}
	
	/**
	 * Sets the element to reconfigure.
	 * 
	 * @param element the element to reconfigure
	 */
	public void setElement(I_Element element) {
		this.element = element;
	}
	
	/**
	 * Gets the state to reconfigure to.
	 * 
	 * @return the state to reconfigure to
	 */
	public I_State getState() {
		return state;
	}
	
	/**
	 * Sets the state to reconfigure to.
	 * 
	 * @param state the state to reconfigure to
	 */
	public void setState(I_State state) {
		this.state = state;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.RECONFIGURE;
	}
}