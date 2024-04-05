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

import edu.mit.spacenet.domain.DomainType;

/**
 * Base class for all demand models.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractDemandModel extends DomainType implements I_DemandModel {

  /**
   * The default constructor that sets a default name.
   */
  public AbstractDemandModel() {
    setName(getDemandModelType().getName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(I_DemandModel demandModel) {
    if (getName().equals(demandModel.getName())) {
      return new Integer(getTid()).compareTo(new Integer(demandModel.getTid()));
    } else {
      return getName().compareTo(demandModel.getName());
    }
  }

  @Override
  public AbstractDemandModel clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
}
