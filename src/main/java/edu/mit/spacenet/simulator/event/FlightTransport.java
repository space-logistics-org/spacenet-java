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

import java.text.DecimalFormat;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * Event that represents an abstract flight with capacity constraints.
 * 
 * @author Paul Grogan
 */
public class FlightTransport extends AbstractEvent implements I_Transport {
  private FlightEdge edge;
  private SortedSet<I_Element> elements;

  /**
   * The default constructor initializes the item mapping.
   */
  public FlightTransport() {
    super();
    elements = new TreeSet<I_Element>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    if (elements.size() == 0) {
      simulator.getWarnings()
          .add(new SimWarning(simulator.getTime(), this, "No elements defined."));
    }
    if (edge == null) {
      throw new SimSpatialError(simulator.getTime(), this, "No flight edge defined.");
    }
    double mass = 0;
    double crew = 0;
    for (I_Element element : getElements()) {
      if (element.getLocation() == null) {
        throw new SimSpatialError(simulator.getTime(), this, element + " was not found.");
      } else if (!getLocation().equals(element.getLocation())) {
        throw new SimSpatialError(simulator.getTime(), this, element + " is located at "
            + element.getLocation() + " instead of " + getLocation() + ".");
      }
      mass += element.getTotalMass();
      if (element instanceof I_Carrier) {
        crew += ((I_Carrier) element).getCrewSize();
      }
    }
    DecimalFormat format = new DecimalFormat("0.0");
    if (mass > edge.getMaxCargoMass()) {
      throw new SimSpatialError(simulator.getTime(), this, "Flight cargo mass over capacity: "
          + format.format(mass) + "/" + format.format(edge.getMaxCargoMass()) + " kg");
    } else if (crew > edge.getMaxCrewSize()) {
      throw new SimSpatialError(simulator.getTime(), this,
          "Flight crew size over capacity: " + crew + "/" + format.format(edge.getMaxCrewSize()));
    }

    System.out.printf("%.3f: %s\n", getTime(), "Commencing flight");

    MoveEvent m1 = new MoveEvent();
    m1.setTime(getTime());
    m1.setPriority(getPriority());
    m1.setParent(this);
    m1.setLocation(edge.getOrigin());
    m1.setContainer(edge);
    m1.setElements(elements);
    m1.execute(simulator);

    MoveEvent m2 = new MoveEvent();
    m2.setTime(getTime() + edge.getDuration());
    m2.setParent(this);
    m2.setLocation(edge);
    m2.setContainer(edge.getDestination());
    m2.setElements(elements);
    simulator.schedule(m2);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
   */
  @Override
  public void print(int tabOrder) {
    super.print(tabOrder);
    System.out.println("Flight Process for " + edge.getName());

  }

  /**
   * Gets the flight edge.
   * 
   * @return the flight edge
   */
  public FlightEdge getEdge() {
    return edge;
  }

  /**
   * Sets the flight edge.
   * 
   * @param edge the edge
   */
  public void setEdge(FlightEdge edge) {
    this.edge = edge;
  }

  /**
   * Gets the top-level elements.
   * 
   * @return the element mapping
   */
  public SortedSet<I_Element> getElements() {
    return elements;
  }

  /**
   * Sets the top-level elements.
   * 
   * @param elements the elements
   */
  public void setElements(SortedSet<I_Element> elements) {
    this.elements = elements;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Process#getDuration()
   */
  public double getDuration() {
    return edge.getDuration();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getOrigin()
   */
  public Node getOrigin() {
    if (edge == null) {
      return null;
    }
    return edge.getOrigin();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getDestination()
   */
  public Node getDestination() {
    if (edge == null) {
      return null;
    }
    return edge.getDestination();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.FLIGHT_TRANSPORT;
  }
}
