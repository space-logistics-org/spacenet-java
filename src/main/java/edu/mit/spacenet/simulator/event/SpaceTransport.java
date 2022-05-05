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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * Event that represents a series of propulsive burns to move from node to node.
 * 
 * @author Paul Grogan
 */
public class SpaceTransport extends AbstractEvent implements I_Transport {
  private SpaceEdge edge;
  private SortedSet<I_Element> elements;
  private List<List<BurnStageItem>> burnStageSequence;

  /**
   * The default constructor initializes the items and burn / stage sequence for each burn.
   */
  public SpaceTransport() {
    super();
    this.elements = new TreeSet<I_Element>();
    this.burnStageSequence = new ArrayList<List<BurnStageItem>>();
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

    for (I_Element element : getElements()) {
      if (element.getLocation() == null) {
        throw new SimSpatialError(simulator.getTime(), this, element + " was not found.");
      } else if (!getLocation().equals(element.getLocation())) {
        throw new SimSpatialError(simulator.getTime(), this, element + " is located at "
            + element.getLocation() + " instead of " + getLocation() + ".");
      }
    }

    System.out.printf("%.3f: %s\n", getTime(), "Commencing space transport");

    MoveEvent m1 = new MoveEvent();
    m1.setTime(getTime());
    m1.setPriority(getPriority());
    m1.setParent(this);
    m1.setLocation(edge.getOrigin());
    m1.setContainer(edge);
    m1.setElements(elements);
    m1.execute(simulator);

    for (int i = 0; i < edge.getBurns().size(); i++) {
      BurnEvent b = new BurnEvent();
      b.setBurn(edge.getBurns().get(i));
      b.setTime(getTime() + edge.getBurns().get(i).getTime());
      b.setPriority(edge.getBurns().size() * -1 + i);
      b.setParent(this);
      b.setLocation(edge);
      b.setElements(elements);
      b.setBurnStateSequence(burnStageSequence.get(i));
      simulator.schedule(b);
    }

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
    System.out.println("Transport Process with " + edge.getBurns().size() + " burn"
        + (edge.getBurns().size() > 1 ? "s" : ""));
  }

  /**
   * Gets the space edge.
   * 
   * @return the space edge
   */
  public SpaceEdge getEdge() {
    return edge;
  }

  /**
   * Sets the space edge. Note that currently this method also re-initializes the burn stage
   * sequence with the proper number of burns... this may cause errors.
   * 
   * @param edge the space edge
   */
  public void setEdge(SpaceEdge edge) {
    this.edge = edge;
    // TODO: this may cause problems overwriting the existing sequence...
    burnStageSequence.clear();
    for (int i = 0; i < edge.getBurns().size(); i++) {
      burnStageSequence.add(new ArrayList<BurnStageItem>());
    }
  }

  /**
   * Gets the set of elements.
   * 
   * @return the set of elements
   */
  public SortedSet<I_Element> getElements() {
    return elements;
  }

  /**
   * Sets the set of elements.
   * 
   * @param elements the set of elements
   */
  public void setElements(SortedSet<I_Element> elements) {
    this.elements = elements;
  }

  /**
   * Gets the burn / stage sequence for each burn.
   * 
   * @return the burn / stage sequence for each burn
   */
  public List<List<BurnStageItem>> getBurnStageSequence() {
    return burnStageSequence;
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
    return edge.getOrigin();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Transport#getDestination()
   */
  public Node getDestination() {
    return edge.getDestination();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.SPACE_TRANSPORT;
  }
}
