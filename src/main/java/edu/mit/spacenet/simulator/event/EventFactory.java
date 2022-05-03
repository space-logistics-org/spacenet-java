/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
    switch (type) {
      case ADD:
        return new AddEvent();
      case BURN:
        return new BurnEvent();
      case CREATE:
        return new CreateEvent();
      case DEMAND:
        return new DemandEvent();
      case EVA:
        return new EvaEvent();
      case EXPLORATION:
        return new ExplorationProcess();
      case FLIGHT_TRANSPORT:
        return new FlightTransport();
      case MOVE:
        return new MoveEvent();
      case RECONFIGURE:
        return new ReconfigureEvent();
      case RECONFIGURE_GROUP:
        return new ReconfigureGroupEvent();
      case REMOVE:
        return new RemoveEvent();
      case SPACE_TRANSPORT:
        return new SpaceTransport();
      case SURFACE_TRANSPORT:
        return new SurfaceTransport();
      case TRANSFER:
        return new TransferEvent();
      default:
        throw new RuntimeException("Unsupported Event");
    }
  }
}
