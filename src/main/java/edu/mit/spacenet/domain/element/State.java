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
package edu.mit.spacenet.domain.element;

import java.util.SortedSet;
import java.util.TreeSet;
import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Class that defines an operational state of an element. An operational state is composed of a
 * state type, one or more demand models, and an overall duty cycle.
 * 
 * @author Paul Grogan
 */
public class State extends DomainType implements I_State {
  private StateType stateType;
  private SortedSet<I_DemandModel> demandModels;

  /**
   * The default constructor sets the default values for name to Default, state type to ACTIVE, and
   * duty cycle to 1. It also initializes the set of demand models, but does not add any.
   */
  public State() {
    super();
    setName("Default");
    setStateType(StateType.ACTIVE);
    this.demandModels = new TreeSet<I_DemandModel>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_State#getStateType()
   */
  public StateType getStateType() {
    return stateType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.domain.element.I_State#setStateType(edu.mit.spacenet.domain.element.StateType)
   */
  public void setStateType(StateType stateType) {
    this.stateType = stateType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_State#getDemandModels()
   */
  public SortedSet<I_DemandModel> getDemandModels() {
    return demandModels;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_State#setDemandModels(java.util.SortedSet)
   */
  public void setDemandModels(SortedSet<I_DemandModel> demandModels) {
    this.demandModels = demandModels;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.I_State#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    DemandSet demands = new DemandSet();
    for (I_DemandModel model : demandModels) {
      for (Demand demand : model.generateDemands(duration, simulator)) {
        demands.add(demand);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(I_State state) {
    if (getStateType().equals(state.getStateType())) {
      if (getName().equals(state.getName())) {
        return new Integer(getTid()).compareTo(new Integer(state.getTid()));
      } else {
        return getName().compareTo(state.getName());
      }
    } else {
      return getStateType().compareTo(state.getStateType());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#toString()
   */
  @Override
  public String toString() {
    return getName();
  }

  @Override
  public State clone() throws CloneNotSupportedException {
    State s = new State();
    s.setTid(getTid());
    s.setName(getName());
    s.setDescription(getDescription());
    s.setStateType(stateType);
    for (I_DemandModel model : demandModels) {
      s.demandModels.add(model.clone());
    }
    return s;
  }
}
