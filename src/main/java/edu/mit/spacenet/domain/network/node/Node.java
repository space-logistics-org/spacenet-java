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