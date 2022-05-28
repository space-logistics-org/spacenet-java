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
package edu.mit.spacenet.scenario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.PreSimulator;
import edu.mit.spacenet.simulator.event.CreateEvent;
import edu.mit.spacenet.simulator.event.EvaEvent;
import edu.mit.spacenet.simulator.event.ExplorationProcess;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.I_Process;
import edu.mit.spacenet.simulator.event.I_Transport;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.util.DateFunctions;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A mission is usually described as one crew rotation or the unmanned delivery of supplies.
 * 
 * @author Paul Grogan
 */
public class Mission implements Comparable<Mission> {
  private String name;
  private Date startDate;
  private Scenario scenario;
  private List<I_Event> eventList;
  private SortedSet<I_DemandModel> demandModels;
  private Node destination;
  private Node origin;
  private Node returnOrigin;
  private Node returnDestination;

  /**
   * The constructor takes a reference to the parent scenario.
   * 
   * @param scenario the scenario
   */
  public Mission(Scenario scenario) {
    this.eventList = new ArrayList<I_Event>();
    this.demandModels = new TreeSet<I_DemandModel>();
    setName("New Mission");
    setStartDate(new Date());
    setScenario(scenario);
  }

  /**
   * Gets the name of the mission.
   * 
   * @return the mission name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the mission and updates and member elements' prefix names appropriately with
   * the first five letters.
   * 
   * @param name the mission name
   */
  public void setName(String name) {
    this.name = name;

    for (I_Event event : getEventList()) {
      if (event.getName().contains(" | "))
        event.setName(getName().substring(0, Math.min(getName().length(), 5))
            + event.getName().substring(event.getName().indexOf(" | ")));
      if (event instanceof CreateEvent) {
        for (I_Element element : ((CreateEvent) event).getElements()) {
          if (element.getName().contains(" | "))
            element.setName(getName().substring(0, Math.min(getName().length(), 5))
                + element.getName().substring(element.getName().indexOf(" | ")));
        }
      }
    }
  }

  /**
   * Gets the list of events.
   * 
   * @return the event list
   */
  public List<I_Event> getEventList() {
    return eventList;
  }

  /**
   * Gets the mission start date.
   * 
   * @return the mission start date
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Sets the mission start date.
   * 
   * @param startDate the mission start date
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * Gets the parent scenario.
   * 
   * @return the scenario
   */
  public Scenario getScenario() {
    return scenario;
  }

  /**
   * Sets the parent scenario.
   * 
   * @param scenario the scenario
   */
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    SimpleDateFormat s = new SimpleDateFormat("M/d/yy");
    return getName() + " (" + s.format(getStartDate()) + ")";
  }

  /**
   * Gets the set of mission-level demand models.
   * 
   * @return the set of demand models
   */
  public SortedSet<I_DemandModel> getDemandModels() {
    return demandModels;
  }

  /**
   * Generate demands from the mission-level models.
   * 
   * @param simulator the simulator
   * 
   * @return the set of mission-level demands
   */
  public DemandSet generateDemands(I_Simulator simulator) {
    DemandSet demands = new DemandSet();
    for (I_DemandModel model : demandModels) {
      for (Demand demand : model.generateDemands(getDuration(), simulator)) {
        demands.add(demand);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Mission mission) {
    if (getStartDate().equals(mission.getStartDate())) {
      return getName().compareTo(mission.getName());
    } else {
      return getStartDate().compareTo(mission.getStartDate());
    }
  }

  /**
   * Gets the mission destination node.
   * 
   * @return the mission destination
   */
  public Node getDestination() {
    return destination;
  }

  /**
   * Sets the mission destination node.
   * 
   * @param destination the mission destination
   */
  public void setDestination(Node destination) {
    this.destination = destination;
  }

  /**
   * Gets the mission origin node.
   * 
   * @return the mission origin
   */
  public Node getOrigin() {
    return origin;
  }

  /**
   * Sets the missino origin node.
   * 
   * @param origin the mission origin
   */
  public void setOrigin(Node origin) {
    this.origin = origin;
  }

  /**
   * Gets the mission return destination node.
   * 
   * @return the mission return destination, null if no return
   */
  public Node getReturnDestination() {
    return returnDestination;
  }

  /**
   * Sets the mission return destination node.
   * 
   * @param returnDestination the mission return destination, null if no return
   */
  public void setReturnDestination(Node returnDestination) {
    this.returnDestination = returnDestination;
  }

  /**
   * Gets the mission return origin node.
   * 
   * @return the mission return origin, null if no return
   */
  public Node getReturnOrigin() {
    return returnOrigin;
  }

  /**
   * Sets the mission return origin node.
   * 
   * @param returnOrigin the mission return origin, null if no return
   */
  public void setReturnOrigin(Node returnOrigin) {
    this.returnOrigin = returnOrigin;
  }

  private double getRemainingCapacityTo(Node node) {
    double capacity = 0;
    PreSimulator preSim = new PreSimulator(getScenario());
    for (I_Event e : getEventList()) {
      if (e instanceof FlightTransport
          && ((FlightTransport) e).getEdge().getDestination().equals(node)) {
        capacity += ((FlightTransport) e).getEdge().getMaxCargoMass();
        preSim.simulate(
            e.getTime()
                + DateFunctions.getDaysBetween(getStartDate(), getScenario().getStartDate()),
            e.getPriority(), false);
        for (I_Element element : ((FlightTransport) e).getElements()) {
          if (element instanceof I_Carrier
              && preSim.getScenario().getNetwork().getRegistrar().containsKey(element.getUid())) {
            capacity -=
                ((I_Carrier) preSim.getScenario().getNetwork().getRegistrar().get(element.getUid()))
                    .getCargoMass();
          }
        }
      } else if (e instanceof SpaceTransport
          && ((SpaceTransport) e).getEdge().getDestination().equals(node)) {
        preSim.simulate(
            e.getTime()
                + DateFunctions.getDaysBetween(getStartDate(), getScenario().getStartDate()),
            e.getPriority(), false);
        for (I_Element element : ((SpaceTransport) e).getElements()) {
          if (element instanceof I_Carrier
              && preSim.getScenario().getNetwork().getRegistrar().containsKey(element.getUid())) {
            capacity +=
                ((I_Carrier) element).getMaxCargoMass() - ((I_Carrier) element).getCargoMass();
          }
        }
      }
    }
    return capacity;
  }

  /**
   * Gets the remaining delivery capacity to the destination node.
   * 
   * @return the amount of remaining capacity (kilograms)
   */
  public double getRemainingDeliveryCapacity() {
    return getRemainingCapacityTo(getDestination());
  }

  /**
   * Gets the remaining return capacity to the return destination node.
   * 
   * @return the amount of remaining capacity (kilograms)
   */
  public double getRemainingReturnCapacity() {
    return getRemainingCapacityTo(getReturnDestination());
  }

  /**
   * Gets the set of delivery carriers in the mission (elements that arrive at the mission
   * destination).
   * 
   * @return the set of carriers
   */
  public Set<I_Carrier> getDeliveryCarriers() {
    HashSet<I_Carrier> carriers = new HashSet<I_Carrier>();
    for (I_Event e : getEventList()) {
      if (e instanceof I_Transport && ((I_Transport) e).getDestination().equals(getDestination())) {
        for (I_Element element : ((I_Transport) e).getElements()) {
          if (element instanceof I_Carrier) {
            carriers.add((I_Carrier) element);
          }
        }
        break;
      }
    }
    return carriers;
  }

  /**
   * Gets the set of return carriers in the missino (elements that arrive at the mission return
   * destination).
   * 
   * @return the set of carriers
   */
  public Set<I_Carrier> getReturnCarriers() {
    HashSet<I_Carrier> carriers = new HashSet<I_Carrier>();
    for (I_Event e : getEventList()) {
      if (e instanceof I_Transport
          && ((I_Transport) e).getDestination().equals(getReturnDestination())) {
        for (I_Element element : ((I_Transport) e).getElements()) {
          if (element instanceof I_Carrier) {
            carriers.add((I_Carrier) element);
          }
        }
        break;
      }
    }
    return carriers;
  }

  private double getRawCapacityTo(Node node) {
    double capacity = 0;
    for (I_Event e : getEventList()) {
      if (e instanceof FlightTransport
          && ((FlightTransport) e).getEdge().getDestination().equals(node)) {
        capacity += ((FlightTransport) e).getEdge().getMaxCargoMass();
      } else if (e instanceof SpaceTransport
          && ((SpaceTransport) e).getEdge().getDestination().equals(node)) {
        for (I_Element i : ((SpaceTransport) e).getElements()) {
          if (i instanceof I_Carrier) {
            capacity += ((I_Carrier) i).getMaxCargoMass();
          }
        }
      }
    }
    return capacity;
  }

  /**
   * Gets the raw delivery capacity to the mission destination.
   * 
   * @return the raw delivery capacity (kilograms)
   */
  public double getRawDeliveryCapacity() {
    return getRawCapacityTo(getDestination());
  }

  /**
   * Gets the raw return capacity to the mission return destination.
   * 
   * @return the raw return capacity (kilograms)
   */
  public double getRawReturnCapacity() {
    return getRawCapacityTo(getReturnDestination());
  }

  /**
   * Gets the crew size of the mission.
   * 
   * @return the crew size
   */
  public int getCrewSize() {
    int crewSize = 0;
    for (I_Event e : getEventList()) {
      if (e instanceof CreateEvent) {
        for (I_Element i : ((CreateEvent) e).getElements()) {
          if (i instanceof CrewMember) {
            crewSize++;
          } else if (i instanceof Carrier) {
            crewSize += ((Carrier) i).getCrewSize();
          }
        }
      }
    }
    return crewSize;
  }

  /**
   * Gets the number of EVAs.
   * 
   * @return the number of EVAs
   */
  public int getNumberEva() {
    int numberEva = 0;
    for (I_Event e : getEventList()) {
      if (e instanceof EvaEvent) {
        numberEva++;
      } else if (e instanceof ExplorationProcess) {
        numberEva += ((ExplorationProcess) e).getNumberEva();
      }
    }
    return numberEva;
  }

  /**
   * Gets the EVA crew time.
   * 
   * @return the EVA crew time (crew-hour)
   */
  public double getEvaCrewTime() {
    double time = 0;
    for (I_Event e : getEventList()) {
      if (e instanceof EvaEvent) {
        time += ((EvaEvent) e).getEvaDuration() * ((EvaEvent) e).getStateMap().size();
      } else if (e instanceof ExplorationProcess) {
        time += ((ExplorationProcess) e).getNumberEva() * ((ExplorationProcess) e).getEvaDuration()
            * ((ExplorationProcess) e).getStateMap().size();
      }
    }
    return time;
  }

  /**
   * Gets the total mission duration.
   * 
   * @return the mission duration (days)
   */
  public double getDuration() {
    double startTime = DateFunctions.getDaysBetween(getStartDate(), getScenario().getStartDate());
    double endTime = startTime;
    // if this is the last mission in the mission list
    Collections.sort(getScenario().getMissionList());
    if (this
        .equals(getScenario().getMissionList().get(getScenario().getMissionList().size() - 1))) {
      // the end time is the start time plus the time of the last event
      // or duration if it is a process
      Collections.sort(getEventList());
      for (I_Event e : getEventList()) {
        double newEndTime = startTime + e.getTime();
        if (e instanceof I_Process)
          newEndTime += ((I_Process) e).getDuration();
        if (newEndTime > endTime)
          endTime = newEndTime;
      }
    } else {
      // else the end time is the days between the start of the scenario
      // and the start of the next mission
      endTime = DateFunctions.getDaysBetween(getScenario().getMissionList()
          .get(getScenario().getMissionList().indexOf(this) + 1).getStartDate(),
          getScenario().getStartDate());
    }
    return GlobalParameters.getSingleton().getRoundedTime(endTime - startTime);
  }

  /**
   * Gets the mission duration at the destination node.
   * 
   * @return the destination duration (days)
   */
  public double getDestinationDuration() {
    double arrival = -1;
    double departure = -1;
    for (I_Event e : getEventList()) {
      if (e instanceof I_Transport) {
        if (((I_Transport) e).getDestination().equals(getDestination()) && arrival == -1) {
          arrival = e.getTime() + ((I_Transport) e).getDuration();
        } else if (((I_Transport) e).getOrigin().equals(getDestination())) {
          departure = e.getTime();
        }
      }
    }
    if (arrival < 0)
      return 0;
    else if (departure < 0)
      return GlobalParameters.getSingleton().getRoundedTime(getDuration() - arrival);
    else
      return GlobalParameters.getSingleton().getRoundedTime(departure - arrival);
  }

  /**
   * Gets the duration in transit to the destination node. The transit duration is defined as the
   * time elapsed from the start of the mission to the arrival of a transport event at the mission
   * destination node.
   * 
   * @return the transit duration (days)
   */
  public double getTransitDuration() {
    double arrivalTime = -1;
    for (I_Event e : getEventList()) {
      if (e instanceof I_Transport) {
        if (((I_Transport) e).getDestination().equals(getDestination()) && arrivalTime == -1) {
          arrivalTime = e.getTime() + ((I_Transport) e).getDuration();
        }
      }
    }
    return GlobalParameters.getSingleton()
        .getRoundedTime((arrivalTime != -1) ? arrivalTime : getDuration());
  }

  /**
   * Gets the return duration in transit from the destination node. The return transit duration is
   * defined as the time elapsed from the start of the last transport event to leave the mission
   * return origin node to the arrival of the last transport event to arrive at the mission return
   * destination node.
   * 
   * @return the transit duration (days)
   */
  public double getReturnTransitDuration() {
    double transitStart = -1;
    double transitEnd = -1;
    for (I_Event e : getEventList()) {
      if (e instanceof I_Transport) {
        if (((I_Transport) e).getDestination().equals(getReturnDestination())) {
          transitEnd = e.getTime() + ((I_Transport) e).getDuration();
        }
        if (((I_Transport) e).getOrigin().equals(getReturnOrigin())) {
          transitStart = e.getTime();
        }
      }
    }
    return GlobalParameters.getSingleton()
        .getRoundedTime((transitEnd != -1 && transitStart != -1) ? transitEnd - transitStart : 0);
  }

  /**
   * Gets the duration not accounted for in the transit or destination duration.
   * 
   * @return the dormant duration (days)
   */
  public double getDormantDuration() {
    return GlobalParameters.getSingleton().getRoundedTime(getDuration() - getTransitDuration()
        - getDestinationDuration() - getReturnTransitDuration());
  }

  /**
   * Gets the set of elements that were created during this mission.
   * 
   * @return the set of elements
   */
  public SortedSet<I_Element> getElements() {
    TreeSet<I_Element> elements = new TreeSet<I_Element>();
    for (I_Event event : getEventList()) {
      if (event instanceof CreateEvent) {
        elements.addAll(((CreateEvent) event).getElements());
      }
    }
    return elements;
  }

  /**
   * Gets whether this is a crewed mission (i.e. contains crew members).
   * 
   * @return whether this is a crewed mission
   */
  public boolean isCrewed() {
    for (I_Element element : getElements()) {
      if (element instanceof CrewMember) {
        return true;
      }
    }
    return false;
  }
}
