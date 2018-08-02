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

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;

/**
 * Interface for any event to be executed in the simulator.
 * 
 * @author Paul Grogan
 */
public interface I_Event extends Comparable<I_Event> {
	
	/**
	 * Gets the time of event execution.
	 * 
	 * @return the time of execution (days)
	 */
	public double getTime();
	
	/**
	 * Sets the time of event execution.
	 * 
	 * @param time the time of execution (days)
	 */
	public void setTime(double time);
	
	/**
	 * Gets the priority of this event over other events with the same time
	 * (Lowest value first).
	 * 
	 * @return the priority of execution (lowest value first)
	 */
	public int getPriority();
	
	/**
	 * Sets the priority of this event over other events with the same time
	 * (Lowest value first).
	 * 
	 * @param priority the priority of execution (lowest value first)
	 */
	public void setPriority(int priority);
	
	/**
	 * Gets the parent event that generated this event, null if no parent.
	 * 
	 * @return the parent event
	 */
	public I_Event getParent();
	
	/**
	 * Sets the parent event that generated this event, null if no parent.
	 * 
	 * @param parent the parent event
	 */
	public void setParent(I_Event parent);
	
	/**
	 * Gets the root-generating event.
	 * 
	 * @return the root event
	 */
	public I_Event getRoot();
	
	/**
	 * Gets the location that the event will act at (in the case of processes,
	 * this returns the initial location.
	 * 
	 * @return the node
	 */
	public Location getLocation();
	
	/**
	 * Sets the location that the event will act at (in the case of processes,
	 * this sets the initial location.
	 * 
	 * @param location the location
	 */
	public void setLocation(Location location);
	
	/**
	 * Gets the name of the event.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Sets the name of the event.
	 * 
	 * @param name the name
	 */
	public void setName(String name);
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(I_Event e);
	
	/**
	 * Executes the event inside the specified simulator.
	 * 
	 * @param simulator the simulator
	 * 
	 * @throws SimError the sim error
	 */
	public void execute(I_Simulator simulator) throws SimError;
	
	/**
	 * Prints the event to console without any indentation.
	 */
	public void print();
	
	/**
	 * Prints the event to console at a specific tab indentation level.
	 * 
	 * @param tabOrder the indentation level
	 */
	public void print(int tabOrder);
	
	/**
	 * Gets the event's unique identifier.
	 * 
	 * @return the unique identifier
	 */
	public long getUid();
	
	/**
	 * Resets the event's unique identifier (used during copy operations).
	 */
	public void resetUid();
	
	/**
	 * Gets the event's type.
	 * 
	 * @return the event type
	 */
	public EventType getEventType();
}