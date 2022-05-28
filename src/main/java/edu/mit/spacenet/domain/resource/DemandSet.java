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
package edu.mit.spacenet.domain.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * A wrapper for a set of demands that performs grouping operations.
 * 
 * @author Paul Grogan
 */
public class DemandSet implements Iterable<Demand> {
  private SortedSet<Demand> demands;

  /**
   * The constructor.
   */
  public DemandSet() {
    demands = new TreeSet<Demand>();
  }

  /**
   * Adds a demand.
   * 
   * @param demand the demand
   * 
   * @return whether the operation was successful
   */
  public boolean add(Demand demand) {
    for (Demand d : demands) {
      if (d.getResource().equals((demand.getResource()))) {
        d.setAmount(d.getAmount() + demand.getAmount());
        return true;
      }
    }
    Demand d = new Demand();
    d.setResource(demand.getResource());
    d.setAmount(demand.getAmount());
    return demands.add(d);
  }

  /**
   * Adds a set of demands.
   * 
   * @param demands the demands
   */
  public void addAll(Iterable<Demand> demands) {
    for (Demand demand : demands) {
      add(demand);
    }
  }

  /**
   * Removes a demand.
   * 
   * @param demand the demand
   * 
   * @return whether the operation was successful
   */
  public boolean remove(Demand demand) {
    for (Demand d : demands) {
      if (d.getResource().equals(demand.getResource())) {
        if (d.getAmount() >= demand.getAmount()) {
          d.setAmount(d.getAmount() - demand.getAmount());
          demand.setAmount(0);
          return true;
        } else {
          demand.setAmount(demand.getAmount() - d.getAmount());
          d.setAmount(0);
          return false;
        }
      }
    }
    return false;
  }

  /**
   * Gets the total mass of all demands.
   * 
   * @return the mass (kilograms)
   */
  public double getTotalMass() {
    double mass = 0;
    for (Demand demand : demands) {
      mass += demand.getMass();
    }
    return GlobalParameters.getSingleton().getRoundedMass(mass);
  }

  /**
   * Gets the total volume of all demands.
   * 
   * @return the volume (cubic meters)
   */
  public double getTotalVolume() {
    double volume = 0;
    for (Demand demand : demands) {
      volume += demand.getVolume();
    }
    return GlobalParameters.getSingleton().getRoundedVolume(volume);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  public Iterator<Demand> iterator() {
    return demands.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return demands.toString();
  }

  /**
   * Gets the number of different demands.
   * 
   * @return the size of the set
   */
  public int size() {
    return demands.size();
  }

  /**
   * Clears all of the demands.
   */
  public void clear() {
    demands.clear();
  }

  /**
   * Removes any demands with zero amount.
   */
  public void clean() {
    List<Demand> forRemoval = new ArrayList<Demand>();
    for (Demand demand : this) {
      if (demand == null || demand.getAmount() == 0)
        forRemoval.add(demand);
    }
    for (Demand d : forRemoval) {
      demands.remove(d);
    }
  }
}
