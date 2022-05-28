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

import edu.mit.spacenet.util.GlobalParameters;

/**
 * Edge that represents an abstracted flight with a finite duration and capacity constraints on crew
 * and cargo mass.
 * 
 * @author Paul Grogan
 */
public class FlightEdge extends Edge {
  private double duration;
  private int maxCrewSize;
  private double maxCargoMass;

  /**
   * The default constructor.
   */
  public FlightEdge() {
    super();
  }

  /**
   * Gets the flight duration.
   * 
   * @return the duration (days)
   */
  public double getDuration() {
    return GlobalParameters.getSingleton().getRoundedTime(duration);
  }

  /**
   * Sets the flight duration, rounding to nearest time precision.
   * 
   * @param duration the duration (days)
   */
  public void setDuration(double duration) {
    this.duration = duration;
  }

  /**
   * Gets the maximum crew size.
   * 
   * @return the maximum crew size
   */
  public int getMaxCrewSize() {
    return maxCrewSize;
  }

  /**
   * Sets the maximum crew size.
   * 
   * @param maxCrewSize the maximum crew size
   */
  public void setMaxCrewSize(int maxCrewSize) {
    this.maxCrewSize = maxCrewSize;
  }

  /**
   * Gets the maximum cargo mass.
   * 
   * @return the maximum caro mass (kilograms)
   */
  public double getMaxCargoMass() {
    return maxCargoMass;
  }

  /**
   * Sets the maximum cargo mass.
   * 
   * @param maxCargoMass the maximum cargo mass (kilograms)
   */
  public void setMaxCargoMass(double maxCargoMass) {
    this.maxCargoMass = maxCargoMass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.network.edge.Edge#getEdgeType()
   */
  @Override
  public EdgeType getEdgeType() {
    return EdgeType.FLIGHT;
  }
}
