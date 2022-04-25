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
package edu.mit.spacenet.domain.network.node;

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.edge.Edge;

/**
 * Network component that represents a static location on a planetary body, a
 * stable orbit, or a Lagrangian point.
 * 
 * @author Paul Grogan
 */
public abstract class Node extends Location {
	private Body body;
	
	/**
	 * The default constructor.
	 */
	public Node() {
		super();
	}
	
	/**
	 * Gets the main body associated with the node.
	 * 
	 * @return the main body
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Sets the main body associated with the node.
	 * 
	 * @param body the main body
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.Location#compareTo(edu.mit.spacenet.domain.network.Location)
	 */
	public int compareTo(Location location) {
		if(location instanceof Node) {
			Node node = (Node)location;
			if(getBody().equals(node.getBody())) {
				if(getBody().equals(Body.EARTH)) {
					// in the case of the Earth, order nodes surface then space
					if(this instanceof SurfaceNode && node instanceof SpaceNode) 
						return -1;
					else if(this instanceof SpaceNode && node instanceof SurfaceNode)
						return 1;
					else 
						return super.compareTo(location);
				} else {
					// for all other bodies, order nodes space then surface
					if(this instanceof SurfaceNode && node instanceof SpaceNode) 
						return 1;
					else if(this instanceof SpaceNode && node instanceof SurfaceNode) 
						return -1;
					else 
						return super.compareTo(location);
				}
			} else if(node.getBody()==Body.EARTH) {
				return 1;
			} else if(getBody()==Body.EARTH) {
				return -1;
			} else {
				return getBody().compareTo(node.getBody());
			}
		} else if(location instanceof Edge) {
			Edge edge = (Edge)location;
			if(this.equals(edge.getOrigin()))
				// if edge has same origin, order node first
				return -1;
			else
				// else order by origin node
				return this.compareTo(edge.getOrigin());
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the node type.
	 * 
	 * @return the node type
	 */
	public abstract NodeType getNodeType();
}