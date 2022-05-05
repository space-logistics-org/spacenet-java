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
 * Node that represents Lagrangian points in space.
 * 
 * @author Paul Grogan
 */
public class LagrangeNode extends SpaceNode {
  private Body minorBody;
  private int number;

  /**
   * The default constructor.
   */
  public LagrangeNode() {
    super();
  }

  /**
   * Gets the Lagrangian number.
   * 
   * @return the Lagrangian number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Sets the Lagrangian number.
   * 
   * @param number the Lagrangian number
   */
  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * Gets the minor body of the Lagrangian.
   * 
   * @return the minor body
   */
  public Body getMinorBody() {
    return minorBody;
  }

  /**
   * Sets the major body of the Lagrangian.
   * 
   * @param minorBody the minor body
   */
  public void setMinorBody(Body minorBody) {
    this.minorBody = minorBody;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.network.node.Node#getNodeType()
   */
  @Override
  public NodeType getNodeType() {
    return NodeType.LAGRANGE;
  }
}
