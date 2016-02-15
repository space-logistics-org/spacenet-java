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
		if(node.getNodeType()==NodeType.SURFACE){
			return new SurfaceNodeEditorPanel(dialog, (SurfaceNode)node);}
		else if(node.getNodeType()==NodeType.ORBITAL){
			return new OrbitalNodeEditorPanel(dialog, (OrbitalNode)node);}
		else if(node.getNodeType()==NodeType.LAGRANGE){
			return new LagrangeNodeEditorPanel(dialog, (LagrangeNode)node);}
		else
			return null; // check for other types of nodes
	}
}
