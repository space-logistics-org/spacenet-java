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
package edu.mit.spacenet.simulator.event;

/**
 * A factory for creating events.
 * 
 * @author Paul Grogan
 */
public abstract class EventFactory {
	
	/**
	 * Factory method that creates new events.
	 * 
	 * @param type the type of event to create
	 * 
	 * @return the event
	 */
	public static I_Event createEvent(EventType type) {
		switch(type) {
		case ADD: return new AddEvent();
		case BURN: return new BurnEvent();
		case CREATE: return new CreateEvent();
		case DEMAND: return new DemandEvent();
		case EVA: return new EvaEvent();
		case EXPLORATION: return new ExplorationProcess();
		case FLIGHT_TRANSPORT: return new FlightTransport();
		case MOVE: return new MoveEvent();
		case RECONFIGURE: return new ReconfigureEvent();
		case RECONFIGURE_GROUP: return new ReconfigureGroupEvent();
		case REMOVE: return new RemoveEvent();
		case SPACE_TRANSPORT: return new SpaceTransport();
		case SURFACE_TRANSPORT: return new SurfaceTransport();
		case TRANSFER: return new TransferEvent();
		default: throw new RuntimeException("Unsupported Event");
		}
	}
}
