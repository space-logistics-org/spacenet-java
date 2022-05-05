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
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Represents an abstracted error that occurs during simulation.
 * 
 * @author Paul Grogan
 */
public abstract class SimError extends Exception implements Comparable<SimError> {
  private static final long serialVersionUID = -3741803666732501991L;
  private double time;
  private I_Event event;
  private String message;

  /**
   * Instantiates a new sim error.
   * 
   * @param time the simulation time of the error
   * @param event the event being executed when the error occurs
   * @param message a message associated with the error
   */
  public SimError(double time, I_Event event, String message) {
    this.time = time;
    this.event = event;
    this.message = message;
  }

  /**
   * Gets the simulation time of the error.
   * 
   * @return the simulation time
   */
  public double getTime() {
    return GlobalParameters.getRoundedTime(time);
  }

  /**
   * Gets the event that was being executed when the error occurred.
   * 
   * @return the event
   */
  public I_Event getEvent() {
    return event;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Throwable#getMessage()
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the warning message.
   * 
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Throwable#toString()
   */
  public String toString() {
    return event + " " + message;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(SimError error) {
    return Double.compare(getTime(), error == null ? 0 : error.getTime());
  }
}
