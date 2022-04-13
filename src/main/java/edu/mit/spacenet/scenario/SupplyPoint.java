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
package edu.mit.spacenet.scenario;

import java.text.DecimalFormat;

import edu.mit.spacenet.domain.network.node.Node;

/**
 * A supply point is the point in time and space that is supplied by a
 * supply edge.
 * 
 * @author Paul Grogan
 */
public class SupplyPoint implements Comparable<SupplyPoint> {
	private SupplyEdge edge;
	
	public SupplyPoint(SupplyEdge edge) {
		this.edge = edge;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.0");
		return getNode() + " (" + format.format(getTime()) + ")";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SupplyPoint point) {
		if(getTime()==point.getTime()) 
			return getNode().compareTo(point.getNode());
		return new Double(getTime()).compareTo(point.getTime())*-1;
	}
	
	/**
	 * Gets the network node.
	 * 
	 * @return the node
	 */
	public Node getNode() {
		return edge.getDestination();
	}
	
	/**
	 * Gets the simulation time of the supply point.
	 * 
	 * @return the time
	 */
	public double getTime() {
		return edge.getEndTime();
	}
	
	/**
	 * Gets the associated supply edge.
	 * 
	 * @return the supply edge
	 */
	public SupplyEdge getEdge() {
		return edge;
	}
}