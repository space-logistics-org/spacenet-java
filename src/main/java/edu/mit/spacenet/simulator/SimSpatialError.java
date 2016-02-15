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

/**
 * Represents an error when an element is not in the right place at the right
 * time.
 * 
 * @author Paul Grogan
 */
public class SimSpatialError extends SimError {
	private static final long serialVersionUID = -4013581934283397903L;

	/**
	 * Instantiates a new sim spatial error.
	 * 
	 * @param time the simulation time of the error
	 * @param event the event that was being executed at the time of the error
	 * @param message the error message
	 */
	public SimSpatialError(double time, I_Event event, String message) {
		super(time, event, message);
		System.out.println("!!! Error: (" + event + ") " + message);
	}
}
