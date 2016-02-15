/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.mit.spacenet.gui.component;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * Displays elements in a tree view.
 * 
 * @author Paul Grogan
 */
public class ElementTree extends JTree {
	private static final long serialVersionUID = 3758692951781867272L;
	private ElementTreeModel model;
	
	/**
	 * The constructor which creates an empty tree.
	 */
	public ElementTree() {
		this(new ElementTreeModel());
	}
	
	/**
	 * The constructor.
	 * 
	 * @param model the element tree model
	 */
	public ElementTree(ElementTreeModel model) {
		super(model);
		this.model = model;
		expandAll();
		setRootVisible(false);
		setShowsRootHandles(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 6981300046370175632L;
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				if(value instanceof DefaultMutableTreeNode && ((DefaultMutableTreeNode)value).getUserObject() instanceof I_Element) {
					setIcon(((I_Element)((DefaultMutableTreeNode)value).getUserObject()).getIcon());
				} else if(value instanceof DefaultMutableTreeNode && ((DefaultMutableTreeNode)value).getUserObject() instanceof Node) {
					setIcon(((Node)((DefaultMutableTreeNode)value).getUserObject()).getNodeType().getIcon());
				} else if(value instanceof DefaultMutableTreeNode && ((DefaultMutableTreeNode)value).getUserObject() instanceof Edge) {
					setIcon(((Edge)((DefaultMutableTreeNode)value).getUserObject()).getEdgeType().getIcon());
				}
				return this;
			}
			
		});
	}
	
	/**
	 * Sets the root of the tree to a new object.
	 * 
	 * @param object the root of the tree
	 */
	public void setRoot(Object object) {
		model = new ElementTreeModel(object);
		setModel(model);
		expandAll();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTree#getModel()
	 */
	@Override
	public ElementTreeModel getModel() {
		return model;
	}
	
	/**
	 * Expands all nodes of the tree.
	 */
	public void expandAll() {
		for(int i=0; i<getRowCount(); i++) {
			expandRow(i);
		}
	}
	
	/**
	 * Gets the selected object.
	 * 
	 * @return the selected object
	 */
	public Object getSelection() {
		if(getSelectionPath()==null){
			return null;
		} else {
			return ((DefaultMutableTreeNode)getSelectionPath().getLastPathComponent()).getUserObject();
		}
	}
	
	/**
	 * Gets the selected element.
	 * 
	 * @return the selected element, null if not an element
	 */
	public I_Element getElementSelection() {
		if(getSelection() instanceof I_Element) return (I_Element)getSelection();
		else return null;
	}
	
	/**
	 * Sets the selection to an element.
	 * 
	 * @param element the element
	 */
	public void setSelection(I_Element element) {
		for(int i=0; i < getRowCount(); i++) {
			if(((DefaultMutableTreeNode)getPathForRow(i).getLastPathComponent()).getUserObject().equals(element)) {
				setSelectionRow(i);
				scrollPathToVisible(getSelectionPath());
				break;
			}
		}
	}
	
	/**
	 * Adds an element to the selection as a nested element.
	 * 
	 * @param element the element
	 */
	public void addToSelection(I_Element element) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)getSelectionPath().getLastPathComponent();
		if(parent.getUserObject() instanceof I_Container) {
			((I_Container)parent.getUserObject()).add(element);
			parent.add(ElementTreeModel.createNode(element));
			model.nodeStructureChanged(parent);
		}
	}
	
	/**
	 * Removes the selected object from the tree.
	 */
	public void removeSelection() {
		if(getSelectionPath().getPathCount() > 1) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)getSelectionPath().getPathComponent(getSelectionPath().getPathCount()-2);
			parent.remove((DefaultMutableTreeNode)getSelectionPath().getLastPathComponent());
			((I_Container)parent.getUserObject()).remove(getElementSelection());
			model.nodeStructureChanged(parent);
		}
	}
}
