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

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Base class for all events.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractEvent implements I_Event {
  private long uid;
  private String name;
  private double time;
  private int priority;
  private Location location;
  private I_Event parent;

  /**
   * The default constructor sets the priority to a default value in the middle of the priority
   * levels and sets a generic name.
   */
  public AbstractEvent() {
    uid = (long) (Long.MAX_VALUE * Math.random());
    priority = 0;
    name = getEventType() == null ? "" : getEventType().getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getLocation()
   */
  public Location getLocation() {
    return location;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.event.I_Event#setLocation(edu.mit.spacenet.domain.network.Location)
   */
  public void setLocation(Location location) {
    this.location = location;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getName()
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#setName(java.lang.String)
   */
  public void setName(String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getTime()
   */
  public double getTime() {
    return GlobalParameters.getSingleton().getRoundedTime(time);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#setTime(double)
   */
  public void setTime(double time) {
    this.time = time;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getPriority()
   */
  public int getPriority() {
    return priority;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#setPriority(int)
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getParent()
   */
  public I_Event getParent() {
    return parent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getRoot()
   */
  public I_Event getRoot() {
    if (getParent() == null)
      return this;
    else
      return getParent().getRoot();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#setParent(edu.mit.spacenet.domain.event.I_Event)
   */
  public void setParent(I_Event parent) {
    this.parent = parent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#compareTo(edu.mit.spacenet.domain.event.I_Event)
   */
  public int compareTo(I_Event e) {
    if (getTime() < e.getTime()) {
      return -1;
    } else if (getTime() > e.getTime()) {
      return 1;
    } else if (getPriority() > e.getPriority()) {
      return 1;
    } else if (getPriority() < e.getPriority()) {
      return -1;
    } else {
      return getName().compareTo(e.getName());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#print()
   */
  public void print() {
    print(0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#print(int)
   */
  public void print(int tabOrder) {
    String s = "";
    for (int i = 0; i < tabOrder; i++)
      s += "  ";
    System.out.printf("%s- %.2f: ", s, getTime());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#getUid()
   */
  public long getUid() {
    return uid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.event.I_Event#resetUid()
   */
  public void resetUid() {
    uid = (long) (Long.MAX_VALUE * Math.random());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof I_Event) {
      return getUid() == ((I_Event) object).getUid();
    } else
      return false;
  }
}
