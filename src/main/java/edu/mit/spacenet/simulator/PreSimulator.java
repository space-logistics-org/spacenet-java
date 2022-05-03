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
package edu.mit.spacenet.simulator;

import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.MissionEvent;

/**
 * A fast simulator that does not log any information.
 * 
 * @author Paul Grogan
 */
public class PreSimulator extends AbstractSimulator {

  /**
   * The constructor sets the scenario.
   * 
   * @param scenario the scenario
   */
  public PreSimulator(Scenario scenario) {
    super(scenario);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#simulate()
   */
  public void simulate() {
    simulate(Double.MAX_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Simulates up to, but not including, an event.
   * 
   * @param event the event
   */
  public void simulate(I_Event event) {
    simulate(event.getTime(), event.getPriority());
  }

  /**
   * Simulates events up to, but not including, an event with an option to initialize before
   * simulation.
   * 
   * @param event the event
   * @param reset whether to initialize before simulation
   */
  public void simulate(I_Event event, boolean reset) {
    simulate(event.getTime(), event.getPriority(), reset);
  }

  /**
   * Simulates events up to, but not including, a simulation time.
   * 
   * @param time the time
   */
  public void simulate(double time) {
    simulate(time, Integer.MAX_VALUE);
  }

  /**
   * Simulates events up to a time and priority.
   * 
   * @param time the time
   * @param priority the priority
   */
  public void simulate(double time, int priority) {
    simulate(time, priority, true);
  }

  /**
   * Simulates events up to a time and priority with an option to initialize before simulation.
   * 
   * @param time the time
   * @param priority the priority
   * @param reset whether to initialize before simulation
   */
  public void simulate(double time, int priority, boolean reset) {
    if (getScenario() == null || reset) {
      initializeSimulation();
    }
    // simulate events up to target event
    while (getEvents().peek() != null && getEvents().peek().getTime() <= time
        && ((!(getEvents().peek() instanceof MissionEvent) && getEvents().peek().getTime() == time)
            ? getEvents().peek().getPriority() < priority
            : true)) {
      getNextEvent();
      handleDemands(); // 7/12/2010: added demand cycle in pre-sim for pre-manifested resources
                       // (delta-v feasibility en route)
      executeEvent();
    }
    // 7-28-2011: added one last demands cycle to trigger demands before
    // prospective event (e.g. resource transfer)
    this.duration = time - getTime();
    setTime(time);
    handleDemands();
  }
}
