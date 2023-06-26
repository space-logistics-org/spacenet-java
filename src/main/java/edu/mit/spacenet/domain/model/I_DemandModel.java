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



import edu.mit.spacenet.domain.I_DomainType;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Interface for broad class of demand models. A demand model generates a set of demands based on
 * the duration of time that has elapsed.
 * 
 * @author Paul Grogan
 */
public interface I_DemandModel extends I_DomainType, Comparable<I_DemandModel>, Cloneable {

  /**
   * Generates a set of demands based on the duration (in days) of time that has elapsed.
   * 
   * @param duration the duration (in days) to generate for
   * @param simulator the simulator requesting the demands
   * 
   * @return the set of demands
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator);

  /**
   * Gets the demand model type.
   * 
   * @return the demand model type
   */
  public DemandModelType getDemandModelType();


  /**
   * Creates a clone of this demand model.
   * 
   * @return the cloned demand model
   * @throws CloneNotSupportedException
   */
  public I_DemandModel clone() throws CloneNotSupportedException;
}
