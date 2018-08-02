/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
