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
import java.util.HashSet;
import java.util.Set;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A supply edge represents an abstraction of a transport along an edge that
 * has the capability to supply a location with resources.
 * 
 * @author Paul Grogan
 */
public class SupplyEdge implements Comparable<SupplyEdge> {
	private Edge edge;
	private boolean isReversed;
	private double startTime, endTime;
	private SupplyPoint point;
	private Set<I_Carrier> carriers;

	/**
	 * The constructor initializes the data members.
	 * 
	 * @param edge the associated network edge
	 * @param isReversed whether the edge is reversed or not (surface edges are reversible)
	 * @param startTime the simulation time of the start of the transport
	 * @param endTime the simulation time of the end of the transport
	 * @param carriers the list of carriers associated with the transport
	 */
	public SupplyEdge(Edge edge, boolean isReversed, double startTime, double endTime, Set<I_Carrier> carriers) {
		this.edge = edge;
		this.isReversed = isReversed;
		this.startTime = startTime;
		this.endTime = endTime;
		this.point = new SupplyPoint(this);
		this.carriers = carriers;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		DecimalFormat format = new DecimalFormat("0.0");
		return getOrigin() + " (" + format.format(startTime) + ") - " + getDestination() + " (" + format.format(endTime) + ")";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SupplyEdge edge) {
		if(endTime==edge.endTime)
			return getOrigin().compareTo(edge.getOrigin());
		else return new Double(endTime).compareTo(edge.endTime)*-1;
	}
	
	/**
	 * Gets the network edge of the transport.
	 * 
	 * @return the network edge
	 */
	public Edge getEdge() {
		return edge;
	}
	
	/**
	 * Gets the origin node of the network edge, taking into account possible
	 * reversing.
	 * 
	 * @return the origin node
	 */
	public Node getOrigin() {
		if(isReversed) return edge.getDestination();
		else return edge.getOrigin();
	}
	
	/**
	 * Gets the destination node of the network edge, taking into account
	 * possible reversing.
	 * 
	 * @return the destination node
	 */
	public Node getDestination() {
		if(isReversed) return edge.getOrigin();
		else return edge.getDestination();
	}
	
	/**
	 * Gets the simulation time of the start of the transport.
	 * 
	 * @return the start time
	 */
	public double getStartTime() {
		return GlobalParameters.getRoundedTime(startTime);
	}
	
	/**
	 * Gets the simulation time of the end of the transport.
	 * 
	 * @return the end time
	 */
	public double getEndTime() {
		return GlobalParameters.getRoundedTime(endTime);
	}
	
	/**
	 * Gets the capacity of the supply edge, measured by the excess cargo mass
	 * capacity of the carriers.
	 * 
	 * @return the capacity (kilograms)
	 */
	public double getCapacity() {
		double capacity = 0;
		for(I_Carrier carrier : getCarriers()) {
			capacity += carrier.getMaxCargoMass()-carrier.getCargoMass();
		}
		return capacity;
	}
	
	/**
	 * Gets the raw capacity of the supply edge, measured by the maximum cargo
	 * mass capacity of the carriers.
	 * 
	 * @return the raw capacity (kilograms)
	 */
	public double getRawCapacity() {
		double capacity = 0;
		for(I_Carrier carrier : getCarriers()) {
			capacity += carrier.getMaxCargoMass();
		}
		return capacity;
	}
	
	/**
	 * Gets the supply point being supplied by the supply edge.
	 * 
	 * @return the supply point
	 */
	public SupplyPoint getPoint() {
		return point;
	}
	
	/**
	 * Gets a set carriers associated with the supply edge.
	 * 
	 * @return the set of carriers
	 */
	public Set<I_Carrier> getCarriers() {
		return carriers;
	}
	
	/**
	 * Gets a set of all carriers (independent of nesting order) associated
	 * with the supply edge.
	 * 
	 * @return the set of carriers
	 */
	public Set<I_Carrier> getAllCarriers() {
		HashSet<I_Carrier> allCarriers = new HashSet<I_Carrier>();
		for(I_Carrier carrier : carriers) {
			recursiveAddCarrier(allCarriers, carrier);
		}
		return allCarriers;
	}
	
	private void recursiveAddCarrier(Set<I_Carrier> allCarriers, I_Carrier carrier) {
		allCarriers.add(carrier);
		for(I_Element element : carrier.getContents()) {
			if(element instanceof I_Carrier) {
				recursiveAddCarrier(allCarriers, (I_Carrier)element);
			}
		}
	}
}
