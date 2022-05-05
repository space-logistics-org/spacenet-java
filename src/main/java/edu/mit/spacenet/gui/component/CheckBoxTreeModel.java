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

import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * The model for the check box tree.
 * 
 * @author Paul Grogan
 */
public class CheckBoxTreeModel extends DefaultTreeModel {
  private static final long serialVersionUID = 2682670639071078594L;
  private boolean childrenChecked;

  /**
   * The constructor.
   * 
   * @param node the root of the model
   */
  public CheckBoxTreeModel(Node node) {
    super(CheckBoxNode.createNode(node));
    childrenChecked = true;
  }

  /**
   * The constructor which places a blank default node at the root.
   */
  public CheckBoxTreeModel() {
    super(new DefaultMutableTreeNode());
    childrenChecked = true;
  }

  private Set<I_Element> getCheckedElements(TreeNode node) {
    HashSet<I_Element> elements = new HashSet<I_Element>();
    for (int i = 0; i < node.getChildCount(); i++) {
      if (node.getChildAt(i) instanceof CheckBoxNode) {
        CheckBoxNode child = (CheckBoxNode) node.getChildAt(i);
        if (child.isChecked()) {
          elements.add(child.getElement());
        } else {
          elements.addAll(getCheckedElements(child));
        }
      }
    }
    return elements;
  }

  /**
   * Gets a set of elements from all of the checked nodes.
   * 
   * @return the set of elements
   */
  public Set<I_Element> getCheckedElements() {
    return getCheckedElements(root);
  }

  /**
   * Gets a set of elements from only the top-level of checked nodes (no nested checked elements).
   * 
   * @return the set of elements
   */
  public Set<I_Element> getTopCheckedElements() {
    return getTopCheckedElements(root);
  }

  private Set<I_Element> getTopCheckedElements(TreeNode node) {
    HashSet<I_Element> elements = new HashSet<I_Element>();
    for (int i = 0; i < node.getChildCount(); i++) {
      CheckBoxNode child = (CheckBoxNode) node.getChildAt(i);
      if (child.isChecked()) {
        elements.add(child.getElement());
      } else {
        elements.addAll(getTopCheckedElements(child));
      }
    }
    return elements;
  }

  /**
   * Checks whether the model contains an element.
   * 
   * @param element the element
   * 
   * @return whether the model contains the element
   */
  public boolean containsElement(I_Element element) {
    return containsElement(root, element);
  }

  private boolean containsElement(TreeNode node, I_Element element) {
    for (int i = 0; i < node.getChildCount(); i++) {
      CheckBoxNode child = (CheckBoxNode) node.getChildAt(i);
      if (child.getElement().equals(element)) {
        return true;
      } else {
        if (containsElement(child, element))
          return true;
      }
    }
    return false;
  }

  /*
   * TODO: suspect public void checkElement(I_Element element) { checkElement(root, element); }
   * private void checkElement(TreeNode node, I_Element element) { for(int i=0; i <
   * node.getChildCount(); i++) { CheckBoxNode child = (CheckBoxNode)node.getChildAt(i);
   * if(child.getElement().equals(element)) { child.setChecked(true); break; } else {
   * checkElement(child, element); } } }
   */
  /**
   * Checks a set of elements.
   * 
   * @param elements the elements to check
   */
  public void setCheckedElements(Set<I_Element> elements) {
    setCheckedElements(root, elements);
  }

  private void setCheckedElements(TreeNode node, Set<I_Element> elements) {
    for (int i = 0; i < node.getChildCount(); i++) {
      CheckBoxNode child = (CheckBoxNode) node.getChildAt(i);
      for (I_Element element : elements) {
        if (child.getElement().equals(element)) {
          child.setChecked(true);
          checkChildren(child, true);
          nodeChanged(child);
        }
      }
      setCheckedElements(child, elements);
    }
  }

  private void checkChildren(TreeNode node, boolean isSelected) {
    if (isChildrenChecked() && node.getParent() instanceof CheckBoxNode
        && ((CheckBoxNode) node.getParent()).isChecked() && !isSelected) {
      ((CheckBoxNode) node).setChecked(true);
      checkChildren(node, true);
    } else if (isChildrenChecked()) {
      for (int i = 0; i < node.getChildCount(); i++) {
        if (node.getChildAt(i) instanceof CheckBoxNode) {
          CheckBoxNode child = (CheckBoxNode) node.getChildAt(i);
          child.setChecked(isSelected);
          nodeChanged(child);
          checkChildren(child, isSelected);
        }
      }
    }
  }

  /**
   * Checks the children nodes of a parent node.
   * 
   * @param node the parent node.
   */
  public void checkChildren(TreeNode node) {
    checkChildren(node, true);
  }

  /**
   * Unchecks the children nodes of a parent node.
   * 
   * @param node the parent node
   */
  public void uncheckChildren(TreeNode node) {
    checkChildren(node, false);
  }

  /**
   * Checks all nodes.
   */
  public void checkAll() {
    checkChildren(root, true);
  }

  /**
   * Unchecks all nodes.
   */
  public void uncheckAll() {
    checkChildren(root, false);
  }

  /**
   * Gets whether children nodes are automatically checked.
   * 
   * @return whether children nodes are automatically checked
   */
  public boolean isChildrenChecked() {
    return childrenChecked;
  }

  /**
   * Sets whether children nodes are automatically checked.
   * 
   * @param childrenChecked whether children nodes are automatically checked
   */
  public void setChildrenChecked(boolean childrenChecked) {
    this.childrenChecked = childrenChecked;
  }
}
