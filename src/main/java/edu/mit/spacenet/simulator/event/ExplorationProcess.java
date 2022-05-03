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

import java.util.SortedMap;
import java.util.TreeMap;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * Event that automatically schedules EVA events with equal time before and after each EVA.
 * 
 * @author Paul Grogan
 */
public class ExplorationProcess extends AbstractEvent implements I_Process {
  private I_Carrier vehicle;
  private double duration;
  private double evaPerWeek;
  private double evaDuration;
  private SortedMap<I_Element, I_State> stateMap;
  private DemandSet demands;

  /**
   * The default constructor initializes the demands set and EVA states map and defaults the EVA
   * duration to 8 hours.
   */
  public ExplorationProcess() {
    super();
    stateMap = new TreeMap<I_Element, I_State>();
    demands = new DemandSet();
    setEvaDuration(8);
  }

  /**
   * Get the number of EVAs to schedule.
   * 
   * @return the number of EVAs
   */
  public int getNumberEva() {
    return (int) Math.floor(duration * evaPerWeek / 7);
  }

  /**
   * Gets the hours between EVAs.
   * 
   * @return the time between EVAs (hours)
   */
  public double getHoursBetweenEva() {
    return (duration * 24 - evaDuration * getNumberEva()) / (getNumberEva() + 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
   */
  public void execute(I_Simulator simulator) throws SimError {
    if (stateMap.size() == 0) {
      simulator.getWarnings()
          .add(new SimWarning(simulator.getTime(), this, "No crew members defined."));
    }
    if (vehicle == null) {
      throw new SimSpatialError(simulator.getTime(), this, "No crew habitat defined.");
    }
    if (vehicle.getLocation() == null) {
      throw new SimSpatialError(simulator.getTime(), this, vehicle + " was not found.");
    }
    if (!getLocation().equals(vehicle.getLocation())) {
      throw new SimSpatialError(simulator.getTime(), this, vehicle + " is located at "
          + vehicle.getLocation() + " instead of " + getLocation() + ".");
    }
    for (I_Element member : stateMap.keySet()) {
      if (member.getLocation() == null) {
        throw new SimSpatialError(simulator.getTime(), this, member + " was not found.");
      } else if (!getLocation().equals(member.getLocation())) {
        throw new SimSpatialError(simulator.getTime(), this, member + " is located at "
            + member.getLocation() + " instead of " + getLocation() + ".");
      }
    }

    System.out.printf("%.3f: %s\n", getTime(), "Commencing exploration at " + getLocation()
        + " for " + getDuration() + " days with " + getNumberEva() + " EVAs");

    if (simulator.getScenario().isDetailedEva()) {
      for (int i = 0; i < getNumberEva(); i++) {
        EvaEvent e = new EvaEvent();
        e.setTime(getTime() + (i + 1) * getHoursBetweenEva() / 24);
        e.setParent(this);
        e.setLocation(getLocation());
        e.setEvaDuration(evaDuration);
        e.setVehicle(vehicle);
        e.setStateMap(stateMap);
        e.setDemands(demands);
        simulator.schedule(e);
      }
    }
  }

  /**
   * Gets the crew member vehicle.
   * 
   * @return the crew member vehicle
   */
  public I_Carrier getVehicle() {
    return vehicle;
  }

  /**
   * Sets the crew member vehicle.
   * 
   * @param vehicle the crew member vehicle
   */
  public void setVehicle(I_Carrier vehicle) {
    this.vehicle = vehicle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Process#getDuration()
   */
  public double getDuration() {
    return duration;
  }

  /**
   * Sets the duration of the exploration process.
   * 
   * @param duration the duration (days)
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Gets the mapping of crew member's EVA states.
   * 
   * @return the map of crew member's EVA states
   */
  public SortedMap<I_Element, I_State> getStateMap() {
    return stateMap;
  }

  /**
   * Sets the mapping of crew member's EVA states.
   * 
   * @param evaStates the mapping of crew member's EVA states
   */
  public void setStateMap(SortedMap<I_Element, I_State> evaStates) {
    this.stateMap = evaStates;
  }

  /**
   * Gets the set of additional EVA demands.
   * 
   * @return the set of additional EVA demands
   */
  public DemandSet getDemands() {
    return demands;
  }

  /**
   * Sets the set of additional EVA demands.
   * 
   * @param demands the set of additional EVA demands
   */
  public void setDemands(DemandSet demands) {
    this.demands = demands;
  }

  /**
   * Gets the number of EVAs per week.
   * 
   * @return the number of EVAs per week
   */
  public double getEvaPerWeek() {
    return evaPerWeek;
  }

  /**
   * Sets the number of EVAs per week.
   * 
   * @param evaPerWeek the number of EVAs per week
   */
  public void setEvaPerWeek(double evaPerWeek) {
    this.evaPerWeek = evaPerWeek;
  }

  /**
   * Gets the duration of each EVA.
   * 
   * @return the duration of each EVA (hours)
   */
  public double getEvaDuration() {
    return evaDuration;
  }

  /**
   * Sets the duration of each EVA.
   * 
   * @param evaDuration the duration of each EVA (hours)
   */
  public void setEvaDuration(double evaDuration) {
    this.evaDuration = evaDuration;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
   */
  public EventType getEventType() {
    return EventType.EXPLORATION;
  }
}
