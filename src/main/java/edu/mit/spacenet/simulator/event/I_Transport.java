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
