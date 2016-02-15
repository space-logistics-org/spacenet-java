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
package edu.mit.spacenet.simulator.event;

import java.util.Set;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * The I_Transport interface is used for processes that have a distinct origin
 * and destination location.
 * 
 * @author Paul Grogan
 */
public interface I_Transport extends I_Process {
	
	/**
	 * Gets the network edge for the transport.
	 * 
	 * @return the edge
	 */
	public Edge getEdge();
	
	/**
	 * Gets the origin node of the transport.
	 * 
	 * @return the origin node
	 */
	public Node getOrigin();
	
	/**
	 * Gets the destination node of the transport.
	 * 
	 * @return the destination node
	 */
	public Node getDestination();
	
	/**
	 * Gets the set of elements.
	 * 
	 * @return the elements
	 */
	public Set<I_Element> getElements();
}
