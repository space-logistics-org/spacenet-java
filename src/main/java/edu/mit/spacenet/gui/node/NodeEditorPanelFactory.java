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
package edu.mit.spacenet.gui.node;

import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;

/**
 * A factory for creating NodeEditorPanel objects.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class NodeEditorPanelFactory {

  /**
   * Creates a new NodeEditorPanel object.
   * 
   * @param node the node
   * 
   * @return the abstract node editor panel
   */
  public static AbstractNodeEditorPanel createNodePanel(NodeEditorDialog dialog, Node node) {
    if (node.getNodeType() == NodeType.SURFACE) {
      return new SurfaceNodeEditorPanel(dialog, (SurfaceNode) node);
    } else if (node.getNodeType() == NodeType.ORBITAL) {
      return new OrbitalNodeEditorPanel(dialog, (OrbitalNode) node);
    } else if (node.getNodeType() == NodeType.LAGRANGE) {
      return new LagrangeNodeEditorPanel(dialog, (LagrangeNode) node);
    } else
      return null; // check for other types of nodes
  }
}
