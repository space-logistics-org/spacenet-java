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
package edu.mit.spacenet.gui.edge;

import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.edge.Edge;

/**
 * An abstract edge editor panel.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class AbstractEdgeEditorPanel extends JPanel {
	private static final long serialVersionUID = 1827177893914135006L;
	
	private EdgeEditorDialog dialog;
	
	/**
	 * Instantiates a new abstract edge editor panel.
	 * 
	 * @param dialog the dialog
	 */
	public AbstractEdgeEditorPanel(EdgeEditorDialog dialog) {
		this.dialog = dialog;
	}
	
	/**
	 * Gets the dialog.
	 * 
	 * @return the dialog
	 */
	public EdgeEditorDialog getDialog() {
		return dialog;
	}
	
	/**
	 * Checks if is edge valid.
	 * 
	 * @return true, if is edge valid
	 */
	public abstract boolean isEdgeValid();
	
	/**
	 * Gets the edge.
	 * 
	 * @return the edge
	 */
	public abstract Edge getEdge();
	
	/**
	 * Save edge.
	 */
	public abstract void saveEdge();
}
