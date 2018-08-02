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
