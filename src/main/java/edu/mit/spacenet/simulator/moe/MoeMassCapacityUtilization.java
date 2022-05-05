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
 * Measure of effectiveness that logs the utilization of mass capacity.
 * 
 * @author Paul Grogan
 */
public class MoeMassCapacityUtilization implements Comparable<MoeMassCapacityUtilization> {
  private double time;
  private Location location;
  private double amount;
  private double capacity;

  /**
   * Instantiates a new MOE mass capacity utilization.
   * 
   * @param time the simulation time to log
   * @param location the location to log
   * @param amount the amount of mass apacity used (kilograms)
   * @param capacity the mass capacity (kilograms)
   */
  public MoeMassCapacityUtilization(double time, Location location, double amount,
      double capacity) {
    this.time = time;
    this.location = location;
    this.amount = amount;
    this.capacity = capacity;
  }

  /**
   * Gets the simulation time.
   * 
   * @return the simulation time
   */
  public double getTime() {
    return time;
  }

  /**
   * Gets the location.
   * 
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Gets the amount of mass capacity used.
   * 
   * @return the amount (kilograms)
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Gets the mass capacity.
   * 
   * @return the capacity (kilograms)
   */
  public double getCapacity() {
    return capacity;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(MoeMassCapacityUtilization moe) {
    return Double.compare(getTime(), moe == null ? 0 : moe.getTime());
  }
}
