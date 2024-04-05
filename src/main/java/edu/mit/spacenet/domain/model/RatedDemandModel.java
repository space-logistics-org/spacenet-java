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
package edu.mit.spacenet.domain.model;

import java.util.SortedSet;
import java.util.TreeSet;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Demand model that generates an amount of a set demands proportional to a rate constant. For
 * discrete items, it aggregates the fractional items until at least a whole unit can be demanded.
 * 
 * @author Paul Grogan
 */
public class RatedDemandModel extends AbstractDemandModel {
  private SortedSet<Demand> demandRates;

  /**
   * Default constructor that initializes the demand rates and item generation structures.
   */
  public RatedDemandModel() {
    demandRates = new TreeSet<Demand>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    DemandSet demands = new DemandSet();
    for (Demand demand : demandRates) {
      Demand resource = new Demand();
      resource.setResource(demand.getResource());
      resource.setAmount(demand.getAmount() * duration);
      demands.add(resource);
    }
    return demands;
  }

  /**
   * Gets the set of demand rates.
   * 
   * @return the set of demands with rate constant in place of amount
   */
  public SortedSet<Demand> getDemandRates() {
    return demandRates;
  }

  /**
   * Sets the set of demand rates.
   * 
   * @param demandRates the set of demands with rate constant in place of amount
   */
  public void setDemandRates(SortedSet<Demand> demandRates) {
    this.demandRates = demandRates;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
   */
  public DemandModelType getDemandModelType() {
    return DemandModelType.RATED;
  }

  @Override
  public RatedDemandModel clone() throws CloneNotSupportedException {
    RatedDemandModel m = new RatedDemandModel();
    m.setTid(getTid());
    m.setName(getName());
    m.setDescription(getDescription());
    for (Demand demand : getDemandRates()) {
      m.getDemandRates().add(demand.clone());
    }
    return m;
  }
}
