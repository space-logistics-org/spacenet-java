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
package edu.mit.spacenet.domain.network.node;

/**
 * Node that represents stable orbits around planetary bodies.
 * 
 * @author Paul Grogan
 */
public class OrbitalNode extends SpaceNode {
  private double inclination;
  private double periapsis;
  private double apoapsis;

  /**
   * The default constructor.
   */
  public OrbitalNode() {
    super();
  }

  /**
   * Gets the inclination of the orbit.
   * 
   * @return the inclination (degrees)
   */
  public double getInclination() {
    return inclination;
  }

  /**
   * Sets the inclination of the orbit.
   * 
   * @param inclination the inclination (degrees)
   */
  public void setInclination(double inclination) {
    this.inclination = inclination;
  }

  /**
   * Gets the periapsis, or distance from the center of the planetary body to the point on the orbit
   * closest to the planetary body.
   * 
   * @return periapsis the orbital periapsis (kilometers)
   */
  public double getPeriapsis() {
    return periapsis;
  }

  /**
   * Sets the periapsis, or distance from the center of the planetary body to the point on the orbit
   * closest to the planetary body.
   * 
   * @param periapsis the orbital periapsis (kilometers)
   */
  public void setPeriapsis(double periapsis) {
    this.periapsis = periapsis;
  }

  /**
   * Gets the apoapsis, or distance from the center of the planetary body to the point on the orbit
   * farthest away to the planetary body.
   * 
   * @return the orbital apoapsis (kilometers)
   */
  public double getApoapsis() {
    return apoapsis;
  }

  /**
   * Sets the apoapsis, or distance from the center of the planetary body to the point on the orbit
   * farthest away to the planetary body.
   * 
   * @param apoapsis the apoapsis (kilometers)
   */
  public void setApoapsis(double apoapsis) {
    this.apoapsis = apoapsis;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.network.node.Node#getNodeType()
   */
  @Override
  public NodeType getNodeType() {
    return NodeType.ORBITAL;
  }
}
