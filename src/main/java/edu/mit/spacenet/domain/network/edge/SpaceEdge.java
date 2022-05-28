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
package edu.mit.spacenet.domain.network.edge;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * Edge that represents a series of propulsive burns with a finite duration.
 * 
 * @author Paul Grogan
 */
public class SpaceEdge extends Edge {
  private double duration;
  private List<Burn> burns;

  /**
   * The default constructor that initializes the list of burns.
   */
  public SpaceEdge() {
    super();
    burns = new ArrayList<Burn>();
  }

  /**
   * Gets the duration.
   * 
   * @return the duration (days)
   */
  public double getDuration() {
    return GlobalParameters.getSingleton().getRoundedTime(duration);
  }

  /**
   * Sets the duration, rounding to nearest time precision.
   * 
   * @param duration the duration (days)
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Gets the list of burns.
   * 
   * @return the list of burns
   */
  public List<Burn> getBurns() {
    return burns;
  }

  /**
   * Sets the list of burns.
   * 
   * @param burns the list of burns
   */
  public void setBurns(List<Burn> burns) {
    this.burns = burns;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.network.edge.Edge#getEdgeType()
   */
  @Override
  public EdgeType getEdgeType() {
    return EdgeType.SPACE;
  }
}
