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
