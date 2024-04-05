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

import edu.mit.spacenet.domain.I_DomainType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * The I_State interface represents an element's operational state, which contains a set of demand
 * models (to drive demands) and a duty cycle.
 * 
 * @author Paul Grogan
 */
public interface I_State extends I_DomainType, Comparable<I_State>, Cloneable {

  /**
   * Gets the state type.
   * 
   * @return the state type
   */
  public StateType getStateType();

  /**
   * Sets the state type.
   * 
   * @param stateType the state type
   */
  public void setStateType(StateType stateType);

  /**
   * Gets the set of demand models.
   * 
   * @return the set of demand models
   */
  public SortedSet<I_DemandModel> getDemandModels();

  /**
   * Sets the set of demand models.
   * 
   * @param demandModels the set of demand models
   */
  public void setDemandModels(SortedSet<I_DemandModel> demandModels);

  /**
   * Generates the set of aggregate demands from the demand models for a specified duration (in
   * days).
   * 
   * @param duration the duration (in days) to generate for
   * @param simulator the simulator requesting the demands
   * 
   * @return the aggregated set of demands
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator);

  /**
   * Creates a clone of this state.
   * 
   * @return the cloned state
   * @throws CloneNotSupportedException
   */
  public I_State clone() throws CloneNotSupportedException;
}
