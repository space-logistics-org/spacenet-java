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

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A scavenging operation.
 * 
 * @author Paul Grogan
 */
public class SimScavenge {
  private double time;
  private I_Item item;
  private double amount;
  private Location location;
  private I_Element element;

  /**
   * Instantiates a new sim scavenge.
   * 
   * @param time the simulation time of the scavenging
   * @param item the item that was scavenged
   * @param amount the amount of item that was scavenged
   * @param location the location of the scavenging
   * @param element the element that received the scavenged part
   */
  public SimScavenge(double time, I_Item item, double amount, Location location,
      I_Element element) {
    this.time = time;
    this.item = item;
    this.amount = amount;
    this.location = location;
    this.element = element;
  }

  /**
   * Gets the simulation time of the scavenging operation.
   * 
   * @return the simulation time
   */
  public double getTime() {
    return GlobalParameters.getSingleton().getRoundedTime(time);
  }

  /**
   * Gets the item that was scavenged.
   * 
   * @return the item
   */
  public I_Item getItem() {
    return item;
  }

  /**
   * Gets the amount of the item that was scavenged.
   * 
   * @return the amount
   */
  public double getAmount() {
    return GlobalParameters.getSingleton().getRoundedDemand(amount);
  }

  /**
   * Gets the location of the scavenging.
   * 
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Gets the element that received the scavenged part.
   * 
   * @return the element
   */
  public I_Element getElement() {
    return element;
  }
}
