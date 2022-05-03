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

/**
 * Edge that represents a surface transfer with a specified distance.
 * 
 * @author Paul Grogan
 */
public class SurfaceEdge extends Edge {

  /** The distance. */
  double distance;

  /**
   * The default constructor.
   */
  public SurfaceEdge() {
    super();
  }

  /**
   * Gets the distance.
   * 
   * @return the distance (kilometers)
   */
  public double getDistance() {
    return this.distance;
  }

  /**
   * Sets the distance.
   * 
   * @param distance the distance (kilometers)
   */
  public void setDistance(double distance) {
    this.distance = distance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.network.edge.Edge#getEdgeType()
   */
  @Override
  public EdgeType getEdgeType() {
    return EdgeType.SURFACE;
  }
}
