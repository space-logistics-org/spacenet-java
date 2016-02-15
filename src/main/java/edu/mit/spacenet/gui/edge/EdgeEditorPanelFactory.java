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

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;

/**
 * A factory for creating EdgePanelEditor objects.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class EdgeEditorPanelFactory {
	
	/**
	 * Creates a new EdgePanelEditor object.
	 * 
	 * @param edge the edge
	 * 
	 * @return the abstract edge panel editor
	 */
	public static AbstractEdgeEditorPanel createEdgePanel(EdgeEditorDialog dialog, Edge edge) {
		if(edge.getEdgeType()==EdgeType.SPACE)
			return new SpaceEdgeEditorPanel(dialog, (SpaceEdge)edge);
		else if (edge.getEdgeType()==EdgeType.FLIGHT){
			return new FlightEdgeEditorPanel(dialog, (FlightEdge)edge);
		}
		else if (edge.getEdgeType()==EdgeType.SURFACE){
			return new SurfaceEdgeEditorPanel(dialog, (SurfaceEdge)edge);
		}
		else return null; // check for other types of edges
	}
}
