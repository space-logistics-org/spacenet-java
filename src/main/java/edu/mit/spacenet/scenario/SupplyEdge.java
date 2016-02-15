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
	
	/**
	 * A supply point is the point in time and space that is supplied by a
	 * supply edge.
	 * 
	 * @author Paul Grogan
	 */
	public class SupplyPoint implements Comparable<SupplyPoint> {
		private SupplyEdge edge;
		
		private SupplyPoint(SupplyEdge edge) {
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
}
