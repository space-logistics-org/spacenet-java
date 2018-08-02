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
