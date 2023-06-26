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
 * Demand model that generates an impulse demand on its first call to generate demands, and nothing
 * afterwards.
 * 
 * @author Paul Grogan
 */
public class TimedImpulseDemandModel extends AbstractDemandModel {
  private SortedSet<Demand> demands;
  private boolean flag = false;

  /**
   * The default constructor initializes the set of demands.
   */
  public TimedImpulseDemandModel() {
    demands = new TreeSet<Demand>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    if (!flag) {
      flag = true;
      DemandSet d = new DemandSet();
      for (Demand demand : demands) {
        d.add(demand);
      }
      return d;
    } else
      return new DemandSet();
  }

  /**
   * Gets the set of demands to generate.
   * 
   * @return the set of demands
   */
  public SortedSet<Demand> getDemands() {
    return demands;
  }

  /**
   * Sets the set of demands to generate.
   * 
   * @param demands the set of demands
   */
  public void setDemands(SortedSet<Demand> demands) {
    this.demands = demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
   */
  public DemandModelType getDemandModelType() {
    return DemandModelType.TIMED_IMPULSE;
  }

  @Override
  public TimedImpulseDemandModel clone() throws CloneNotSupportedException {
    TimedImpulseDemandModel m = new TimedImpulseDemandModel();
    m.setTid(getTid());
    m.setName(getName());
    m.setDescription(getDescription());
    for (Demand demand : getDemands()) {
      m.getDemands().add(demand.clone());
    }
    return m;
  }
}
