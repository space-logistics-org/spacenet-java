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

import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Represents a warning message that occurs during simulation.
 * 
 * @author Paul Grogan
 */
public class SimWarning extends Exception implements Comparable<SimWarning> {
	private static final long serialVersionUID = -4715307155933350996L;
	private double time;
	private I_Event event;
	private String message;
	
	/**
	 * Instantiates a new sim warning.
	 * 
	 * @param time the simulation time of the warning
	 * @param event the event being executed when the warning occurred
	 * @param message the warning message
	 */
	public SimWarning(double time, I_Event event, String message) {
		this.time = time;
		this.event = event;
		this.message = message;
		System.out.println("!!! Warning: (" + event + ") " + message);
	}
	
	/**
	 * Gets the simulation time of the warning.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return GlobalParameters.getRoundedTime(time);
	}
	
	/**
	 * Gets the event that was being executed when the warning occurred.
	 * 
	 * @return the event
	 */
	public I_Event getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the warning message.
	 * 
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		return event + " " + message;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SimWarning warning) {
		return Double.compare(getTime(), warning==null?0:warning.getTime());
	}
}
