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
package edu.mit.spacenet.gui.event;

import edu.mit.spacenet.simulator.event.AddEvent;
import edu.mit.spacenet.simulator.event.BurnEvent;
import edu.mit.spacenet.simulator.event.CreateEvent;
import edu.mit.spacenet.simulator.event.DemandEvent;
import edu.mit.spacenet.simulator.event.EvaEvent;
import edu.mit.spacenet.simulator.event.ExplorationProcess;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.MoveEvent;
import edu.mit.spacenet.simulator.event.ReconfigureEvent;
import edu.mit.spacenet.simulator.event.ReconfigureGroupEvent;
import edu.mit.spacenet.simulator.event.RemoveEvent;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.simulator.event.TransferEvent;

/**
 * A factory for creating event panels.
 * 
 * @author Paul Grogan
 */
public class EventPanelFactory {
	
	/**
	 * Creates a new EventPanel object.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 * 
	 * @return the abstract event panel
	 */
	public static AbstractEventPanel createEventPanel(EventDialog eventDialog, I_Event event) {
		switch(event.getEventType()) {
		case CREATE: return new CreateEventPanel(eventDialog, (CreateEvent)event);
		case MOVE: return new MoveEventPanel(eventDialog, (MoveEvent)event);
		case REMOVE: return new RemoveEventPanel(eventDialog, (RemoveEvent)event);
		case RECONFIGURE: return new ReconfigureEventPanel(eventDialog, (ReconfigureEvent)event);
		case RECONFIGURE_GROUP: return new ReconfigureGroupEventPanel(eventDialog, (ReconfigureGroupEvent)event);
		case ADD: return new AddEventPanel(eventDialog, (AddEvent)event);
		case TRANSFER: return new TransferEventPanel(eventDialog, (TransferEvent)event);
		case DEMAND: return new DemandEventPanel(eventDialog, (DemandEvent)event);
		case EVA: return new EvaEventPanel(eventDialog, (EvaEvent)event);
		case EXPLORATION: return new ExplorationProcessPanel(eventDialog, (ExplorationProcess)event);
		case BURN: return new BurnEventPanel(eventDialog, (BurnEvent)event);
		case SPACE_TRANSPORT: return new SpaceTransportPanel(eventDialog, (SpaceTransport)event);
		case SURFACE_TRANSPORT: return new SurfaceTransportPanel(eventDialog, (SurfaceTransport)event);
		case FLIGHT_TRANSPORT: return new FlightTransportPanel(eventDialog, (FlightTransport)event);
		default: throw new RuntimeException("Unsupported Event");
		}
	}
}
