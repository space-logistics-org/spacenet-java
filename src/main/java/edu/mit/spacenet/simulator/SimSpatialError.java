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

import edu.mit.spacenet.simulator.event.I_Event;

/**
 * Represents an error when an element is not in the right place at the right time.
 * 
 * @author Paul Grogan
 */
public class SimSpatialError extends SimError {
  private static final long serialVersionUID = -4013581934283397903L;

  /**
   * Instantiates a new sim spatial error.
   * 
   * @param time the simulation time of the error
   * @param event the event that was being executed at the time of the error
   * @param message the error message
   */
  public SimSpatialError(double time, I_Event event, String message) {
    super(time, event, message);
    System.out.println("!!! Error: (" + event + ") " + message);
  }
}
