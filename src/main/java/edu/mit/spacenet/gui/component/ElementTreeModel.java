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
package edu.mit.spacenet.gui.component;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * The model for the element tree.
 * 
 * @author Paul Grogan
 */
public class ElementTreeModel extends DefaultTreeModel {
  private static final long serialVersionUID = -2759958352258498303L;
  private DefaultMutableTreeNode root;
  private static boolean hideEmptyLocations = true;

  /**
   * The constructor.
   * 
   * @param object the root of the tree
   */
  public ElementTreeModel(Object object) {
    super(createNode(object));
    this.root = (DefaultMutableTreeNode) getRoot();
  }

  /**
   * The constructor which sets the root to a new blank node.
   */
  public ElementTreeModel() {
    super(new DefaultMutableTreeNode());
    this.root = (DefaultMutableTreeNode) getRoot();
  }

  /**
   * Static method to recursively create a new node and insert any child nodes as necessary to
   * represent nested elements.
   * 
   * @param object the parent object
   * 
   * @return the node
   */
  public static DefaultMutableTreeNode createNode(Object object) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(object);
    if (object instanceof Network) {
      int index = 0;
      for (Node n : ((Network) object).getNodes()) {
        if (hideEmptyLocations && n.getContents().size() > 0)
          node.insert(createNode(n), index++);
      }
      for (Edge e : ((Network) object).getEdges()) {
        if (hideEmptyLocations && e.getContents().size() > 0)
          node.insert(createNode(e), index++);
      }
      if (((Network) object).getRemovedRegistrar().size() > 0) {
        DefaultMutableTreeNode removedNode = createNode("Removed");
        node.insert(removedNode, index++);
        int removedIndex = 0;
        for (I_Element e : ((Network) object).getRemovedRegistrar().values()) {
          removedNode.insert(createNode(e), removedIndex++);
        }
      }
    } else if (object instanceof I_Container) {
      int index = 0;
      for (I_Element e : ((I_Container) object).getContents()) {
        node.insert(createNode(e), index++);
      }
    }
    return node;
  }

  /**
   * Adds an element to the root.
   * 
   * @param element the element
   */
  public void addElement(I_Element element) {
    if (root.getUserObject() == null || root.getUserObject() instanceof Node) {
      root.insert(createNode(element), root.getChildCount());
      fireTreeStructureChanged(root, root.getPath(), null, null);
    } else
      throw new RuntimeException("wrong root");
  }

  /**
   * Removes an element from the model.
   * 
   * @param element the element
   */
  @SuppressWarnings("unchecked")
  public void removeElement(I_Element element) {
    Enumeration<TreeNode> e = root.breadthFirstEnumeration();
    while (e.hasMoreElements()) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
      if (node.getUserObject() != null && node.getUserObject().equals(element)) {
        node.removeFromParent();
        fireTreeStructureChanged(root, root.getPath(), null, null);
      }
    }
  }

  /**
   * Shameless hack to force the model to update (simply fires a tree structure changed event...
   * note that this usually loses information on the expanded and/or selected nodes).
   */
  public void hackedUpdate() {
    fireTreeStructureChanged(root, root.getPath(), null, null);
  }

  /**
   * Gets the child elements of the root.
   * 
   * @return the set of elements
   */
  public Set<I_Element> getElements() {
    HashSet<I_Element> elements = new HashSet<I_Element>();
    for (int i = 0; i < root.getChildCount(); i++) {
      if (((DefaultMutableTreeNode) root.getChildAt(i)).getUserObject() instanceof I_Element) {
        elements.add((I_Element) ((DefaultMutableTreeNode) root.getChildAt(i)).getUserObject());
      }
    }
    return elements;
  }
}
