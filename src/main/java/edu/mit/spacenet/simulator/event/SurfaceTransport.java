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

import java.util.HashSet;
import java.util.Set;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An event that models the surface transport along a surface edge using a surface vehicle.
 * 
 * @author Paul Grogan
 */
public class SurfaceTransport extends AbstractEvent implements I_Transport {
  private double dutyCycle;
  private SurfaceVehicle vehicle;
  private I_State transportState;
  private double speed;
  private SurfaceEdge edge;

  /**
   * The default constructor sets the duty cycle to 1.
   */
  public SurfaceTransport() {
    super();
    dutyCycle = 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    if (edge == null) {
      throw new SimSpatialError(simulator.getTime(), this, "No surface edge defined.");
    }
    if (vehicle == null) {
      throw new SimSpatialError(simulator.getTime(), this, "No surface vehicle defined.");
    }
    if (vehicle.getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this, vehicle + " was not found.");
    }
    if (!getLocation().equals(vehicle.getLocation())) {
      throw new SimSpatialError(simulator.getTime(), this, vehicle + " is located at "
          + vehicle.getLocation() + " instead of " + getLocation() + ".");
    }
    if (speed <= 0 || dutyCycle <= 0) {
      throw new SimSpatialError(simulator.getTime(), this, "Infinite travel duration.");
    }

    System.out.printf("%.3f: %s\n", getTime(), "Commencing surface transport");

    I_State previousState = vehicle.getCurrentState();

    if (transportState != null) {
      ReconfigureEvent r1 = new ReconfigureEvent();
      r1.setTime(getTime());
      r1.setPriority(getPriority());
      r1.setParent(this);
      if (isReversed())
        r1.setLocation(edge.getDestination());
      else
        r1.setLocation(edge.getOrigin());
      r1.setElement(vehicle);
      r1.setState(transportState);
      r1.execute(simulator);
    }

    MoveEvent m1 = new MoveEvent();
    m1.setTime(getTime());
    m1.setPriority(getPriority());
    m1.setParent(this);
    m1.setLocation(getLocation());
    m1.setContainer(edge);
    m1.getElements().add(vehicle);
    m1.execute(simulator);

    MoveEvent m2 = new MoveEvent();
    m2.setTime(getTime() + getDuration());
    m2.setPriority(-1);
    m2.setParent(this);
    m2.setLocation(edge);
    if (isReversed())
      m2.setContainer(edge.getOrigin());
    else
      m2.setContainer(edge.getDestination());
    m2.getElements().add(vehicle);
    simulator.schedule(m2);

    if (previousState != null) {
      ReconfigureEvent r2 = new ReconfigureEvent();
      r2.setTime(getTime() + getDuration());
      r2.setPriority(0);
      r2.setParent(this);
      if (isReversed())
        r2.setLocation(edge.getOrigin());
      else
        r2.setLocation(edge.getDestination());
      r2.setElement(vehicle);
      r2.setState(previousState);
      simulator.schedule(r2);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Process#getDuration()
   */
  public double getDuration() {
    if (getSpeed() == 0 || getDutyCycle() == 0)
      return Double.POSITIVE_INFINITY;
    else
      return GlobalParameters
          .getRoundedTime(edge.getDistance() / (getSpeed() * getDutyCycle() * 24));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getOrigin()
   */
  public Node getOrigin() {
    return isReversed() ? edge.getDestination() : edge.getOrigin();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getDestination()
   */
  public Node getDestination() {
    return isReversed() ? edge.getOrigin() : edge.getDestination();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getElements()
   */
  public Set<I_Element> getElements() {
    HashSet<I_Element> elements = new HashSet<I_Element>();
    elements.add(vehicle);
    return elements;
  }

  /**
   * Gets the duty cycle (percent of time in motion).
   * 
   * @return the duty cycle (percent)
   */
  public double getDutyCycle() {
    return dutyCycle;
  }

  /**
   * Sets the duty cycle (percent of time in motion).
   * 
   * @param dutyCycle the duty cycle (percent)
   */
  public void setDutyCycle(double dutyCycle) {
    this.dutyCycle = dutyCycle;
  }

  /**
   * Gets the surface vehicle.
   * 
   * @return the surface vehicle
   */
  public SurfaceVehicle getVehicle() {
    return vehicle;
  }

  /**
   * Sets the surface vehicle.
   * 
   * @param vehicle the surface vehicle
   */
  public void setVehicle(SurfaceVehicle vehicle) {
    this.vehicle = vehicle;
  }

  /**
   * Gets the transport speed.
   * 
   * @return the transport speed (meters per second)
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * Sets the transport speed.
   * 
   * @param speed the transport speed (meters per second)
   */
  public void setSpeed(double speed) {
    this.speed = speed;
  }

  /**
   * Gets the surface edge.
   * 
   * @return the surface edge
   */
  public SurfaceEdge getEdge() {
    return edge;
  }

  /**
   * Sets the surface edge.
   * 
   * @param edge the surface edge
   */
  public void setEdge(SurfaceEdge edge) {
    this.edge = edge;
  }

  /**
   * Gets whether the edge is reversed or not.
   * 
   * @return true if the edge's origin and destination are swapped, false otherwise
   */
  public boolean isReversed() {
    return getLocation().equals(edge.getDestination());
  }

  /**
   * Gets the state of the transport vehicle during transport.
   * 
   * @return the transport state
   */
  public I_State getTransportState() {
    return transportState;
  }

  /**
   * Sets the state of the transport vehicle during transport.
   * 
   * @param transportState the transport state
   */
  public void setTransportState(I_State transportState) {
    this.transportState = transportState;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.SURFACE_TRANSPORT;
  }
}
