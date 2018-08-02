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
package edu.mit.spacenet.domain.network.edge;

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * Base class for network components that connect two nodes (edges).
 * 
 * @author Paul Grogan
 */
public abstract class Edge extends Location {
	private Node origin;
	private Node destination;
	private EdgeType edgetype;
	
	/**
	 * The default constructor.
	 */
	public Edge() { 
		super();
	}
	
	/**
	 * Gets the origin.
	 * 
	 * @return the origin
	 */
	public Node getOrigin() { 
		return origin;
	}
	
	/**
	 * Sets the origin.
	 * 
	 * @param origin the origin
	 */
	public void setOrigin(Node origin) {
		this.origin = origin;
	}
	
	/**
	 * Gets the destination.
	 * 
	 * @return the destination
	 */
	public Node getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination.
	 * 
	 * @param destination the destination
	 */
	public void setDestination(Node destination) {
		this.destination = destination;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.Location#compareTo(edu.mit.spacenet.domain.network.Location)
	 */
	public int compareTo(Location location) {
		if(location instanceof Edge) {
			return super.compareTo(location);
			/* TODO ordering by node origin breaks compatibility
			Edge edge = (Edge)location;
			if(getOrigin().equals(edge.getOrigin()))
				// if both edges have same origin, order by name/id
				return super.compareTo(location);
			else
				// if edges have different origin, order by origin
				return getOrigin().compareTo(edge.getOrigin());
			*/
		} else if(location instanceof Node) {
			Node node = (Node)location;
			if(getOrigin().equals(node))
				// if node is edge's origin, order node first
				return 1;
			else 
				// else order by origin node
				return getOrigin().compareTo(node);
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the edge type.
	 * 
	 * @return the edge type
	 */
	public EdgeType getEdgeType(){
		return edgetype;
	}

	public void setEdgeType(EdgeType edgetype1) {
		this.edgetype=edgetype1;
		
	}
}
