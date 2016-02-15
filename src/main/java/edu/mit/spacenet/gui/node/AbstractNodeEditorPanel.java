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
package edu.mit.spacenet.gui.node;

import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.node.Node;

/**
 * Abstract component that serves as an interface to the various node panels on the editor.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class AbstractNodeEditorPanel extends JPanel {
	private static final long serialVersionUID = -7771843429615660661L;
	
	private NodeEditorDialog dialog;
	
	/**
	 * Instantiates a new abstract node editor panel.
	 * 
	 * @param dialog the dialog
	 */
	public AbstractNodeEditorPanel(NodeEditorDialog dialog) {
		this.dialog = dialog;
	}
	
	/**
	 * Gets the dialog.
	 * 
	 * @return the dialog
	 */
	public NodeEditorDialog getDialog() {
		return dialog;
	}
	
	/**
	 * Checks if is node valid.
	 * 
	 * @return true, if is node valid
	 */
	public abstract boolean isNodeValid();

	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public abstract Node getNode();
	
	/**
	 * Saves the node.
	 */
	public abstract void saveNode();
}
