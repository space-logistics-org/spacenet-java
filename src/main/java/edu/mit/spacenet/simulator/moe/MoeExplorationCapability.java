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
package edu.mit.spacenet.simulator.moe;

import edu.mit.spacenet.domain.network.Location;

/**
 * The measure of effectiveness that logs exploration capability.
 * 
 * @author Paul Grogan
 */
public class MoeExplorationCapability implements Comparable<MoeExplorationCapability> {
  private double time;
  private Location location;
  private double mass;
  private double duration;

  /**
   * Instantiates a new MOE exploration capability.
   * 
   * @param time the simulation time of exploration to log
   * @param location the location to log
   * @param mass the mass of exploration-conducive material to log
   * @param duration the exploration duration to log
   */
  public MoeExplorationCapability(double time, Location location, double mass, double duration) {
    this.time = time;
    this.location = location;
    this.mass = mass;
    this.duration = duration;
  }

  /**
   * Gets the simulation time of the exploration.
   * 
   * @return the simulation time
   */
  public double getTime() {
    return time;
  }

  /**
   * Gets the location of the exploration.
   * 
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Gets the mass of exploration-conducive material.
   * 
   * @return the mass (kilograms)
   */
  public double getMass() {
    return mass;
  }

  /**
   * Gets the duration of the exploration.
   * 
   * @return the duration (days)
   */
  public double getDuration() {
    return duration;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(MoeExplorationCapability moe) {
    return Double.compare(getTime(), moe == null ? 0 : moe.getTime());
  }
}
