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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.element.ResourceContainer;
import edu.mit.spacenet.domain.element.ResourceContainerFactory;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.scenario.SupplyEdge.SupplyPoint;
import edu.mit.spacenet.simulator.event.ManifestEvent;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The manifest represents the packing of resources into resource containers,
 * and the eventual movement of resource containers into carriers during
 * simulation.
 * 
 * @author Paul Grogan
 */
public class Manifest {
	private Scenario scenario;
	private SortedSet<SupplyEdge> supplyEdges;
	private SortedSet<SupplyPoint> supplyPoints;
	private SortedMap<SupplyPoint, DemandSet> aggregatedNodeDemands;
	private SortedMap<SupplyEdge, DemandSet> aggregatedEdgeDemands;
	private Map<Demand, Set<Demand>> demandsAsPacked;
	private Map<I_ResourceContainer, Set<Demand>> packedDemands;
	private SortedMap<SupplyPoint, Set<I_ResourceContainer>> cachedContainerDemands;
	private SortedMap<SupplyEdge, Map<I_Carrier, Set<I_ResourceContainer>>> manifestedContainers;
	
	/**
	 * Instantiates a new manifest.
	 * 
	 * @param scenario the scenario
	 */
	public Manifest(Scenario scenario) {
		this.scenario = scenario;
		supplyEdges = new TreeSet<SupplyEdge>();
		supplyPoints = new TreeSet<SupplyPoint>();
		aggregatedNodeDemands = new TreeMap<SupplyPoint, DemandSet>();
		aggregatedEdgeDemands = new TreeMap<SupplyEdge, DemandSet>();
		demandsAsPacked = new HashMap<Demand, Set<Demand>>();
		packedDemands = new HashMap<I_ResourceContainer, Set<Demand>>();
		cachedContainerDemands = new TreeMap<SupplyPoint, Set<I_ResourceContainer>>();
		manifestedContainers = new TreeMap<SupplyEdge, Map<I_Carrier, Set<I_ResourceContainer>>>();
	}
	
	/**
	 * Gets the remaining amount to pack for a given aggregated demand.
	 * 
	 * @param demand the demand to find the remaining amount to pack
	 * 
	 * @return the remaining amount to pack (units)
	 */
	public double getRemainingAmount(Demand demand) {
		return demand.getAmount()-getPackedAmount(demand);
	}
	
	/**
	 * Gets the cargo mass of a given resource container at the point in
	 * time referenced by a supply point.
	 * 
	 * @param container the container for which to find the cargo mass
	 * @param point the point in time to reference
	 * 
	 * @return the cargo mass (kilograms)
	 */
	public double getCargoMass(I_ResourceContainer container, SupplyPoint point) {
		double amount = 0;
		for(Demand demandAsPacked : getPackedDemands(container, point)) {
			amount+=demandAsPacked.getMass();
		}
		return amount;
	}
	
	/**
	 * Gets the cargo volume of a given resource container at a point in time
	 * referenced by a supply point.
	 * 
	 * @param container the container for which to find the cargo volume
	 * @param point the point in time to reference
	 * 
	 * @return the cargo volume (cubic meters)
	 */
	public double getCargoVolume(I_ResourceContainer container, SupplyPoint point) {
		double amount = 0;
		for(Demand demandAsPacked : getPackedDemands(container, point)) {
			amount+=demandAsPacked.getVolume();
		}
		return amount;
	}
	
	/**
	 * Gets the cargo mass of a given carrier at a point in time referenced by
	 * a supply point.
	 * 
	 * @param carrier the carrier for which to find the cargo volume
	 * @param point the point in time to reference
	 * 
	 * @return the cargo mass (kilograms)
	 */
	public double getCargoMass(I_Carrier carrier, SupplyPoint point) {
		double amount = carrier.getCargoMass();
		for(I_ResourceContainer container : getManifestedContainers(carrier, point)) {
			amount+=container.getMass() + getCargoMass(container, point);
		}
		return amount;
	}
	
	/**
	 * Gets the cargo volume of a given carrier at a point in time referenced by
	 * a supply point.
	 * 
	 * @param carrier the carrier for which to find the cargo volume
	 * @param point the point in time to reference
	 * 
	 * @return the cargo volume (cubic meters)
	 */
	public double getCargoVolume(I_Carrier carrier, SupplyPoint point) {
		double amount = carrier.getCargoVolume();
		for(I_ResourceContainer container : getManifestedContainers(carrier, point)) {
			amount+=container.getVolume();
		}
		return amount;
	}
	
	/**
	 * Get the packed amount of a given aggregated demand.
	 * 
	 * @param demand the aggregated demand
	 * 
	 * @return the packed amount (units)
	 */
	public double getPackedAmount(Demand demand) {
		if(getDemandsAsPacked().get(demand) == null) return 0;
		double amount = 0;
		for(Demand demandAsPacked : getDemandsAsPacked().get(demand)) {
			amount += demandAsPacked.getAmount();
		}
		return GlobalParameters.getRoundedDemand(amount);
	}
	
	/**
	 * Get the supply point associated with a given aggregated demand.
	 * 
	 * @param demand the demand to find the supply point
	 * 
	 * @return the supply point
	 */
	public SupplyPoint getSupplyPoint(Demand demand) {
		for(SupplyPoint p : supplyPoints) {
			for(Demand d : aggregatedNodeDemands.get(p)) {
				if(d.equals(demand)) return p;
			}
			for(Demand d : aggregatedEdgeDemands.get(p.getEdge())) {
				if(d.equals(demand)) return p;
			}
		}
		return null;
	}
	
	/**
	 * Get the supply point (earlier in time) that services a supply edge.
	 * 
	 * @param edge the edge to find the supply point for
	 * 
	 * @return the supply point to service the supply edge
	 */
	public SupplyPoint getNextSupplyPoint(SupplyEdge edge) {
		if(edge.getOrigin() instanceof SurfaceNode 
				&& edge.getOrigin().getBody()==Body.EARTH) 
			return null;
		
		SupplyPoint point = null;
		for(SupplyPoint p : supplyPoints) {
			if(p.getNode().equals(edge.getOrigin())
					&& p.getTime() <= edge.getStartTime()
					&& (point==null||p.getTime()>point.getTime())) {
				point = p;
			}
		}
		return point;
	}
	
	/**
	 * Gets the aggregated demand for a given packed demand.
	 * 
	 * @param demandAsPacked the packed demand
	 * 
	 * @return the aggregated demand
	 */
	public Demand getDemand(Demand demandAsPacked) {
		for(Demand demand : demandsAsPacked.keySet()) {
			for(Demand d : demandsAsPacked.get(demand)) {
				if(d.equals(demandAsPacked)) return demand;
			}
		}
		return null;
	}
	
	/**
	 * Gets the supply edge associated with a carrier.
	 * 
	 * @param carrier the carrier
	 * 
	 * @return the supply edge associated with the carrier
	 */
	public SupplyEdge getSupplyEdge(I_Carrier carrier) {
		for(SupplyEdge e : supplyEdges) {
			for(I_Carrier c : e.getCarriers()) {
				// must use == because carriers are repeated on edges
				if(c==carrier) return e; 
			}
		}
		return null;
	}
	
	/**
	 * Gets all of the supply edges that can supply a supply point.
	 * 
	 * @param point the supply point
	 * 
	 * @return a set of supply edges that can supply the supply point
	 */
	public Set<SupplyEdge> getSupplyEdges(SupplyPoint point) {
		HashSet<SupplyEdge> edges = new HashSet<SupplyEdge>();
		for(SupplyEdge edge : supplyEdges) {
			if(edge.getDestination().equals(point.getNode())
					&& edge.getEndTime() <= point.getTime()) {
				edges.add(edge);
			}
		}
		return edges;
	}
	
	/**
	 * Gets the initial supply point that a resource container is assigned to.
	 * 
	 * @param container the resource container
	 * 
	 * @return the intial supply point
	 */
	public SupplyPoint getInitialSupplyPoint(I_ResourceContainer container) {
		SupplyEdge edge = null;
		for(SupplyEdge e : supplyEdges) {
			for(I_Carrier carrier : e.getCarriers()) {
				if(manifestedContainers.get(e).get(carrier).contains(container)
						&& (edge==null||e.getEndTime()>edge.getEndTime())) {
					edge = e;
				}
			}
		}
		SupplyPoint point = edge==null?null:edge.getPoint();
		for(Demand demandAsPacked : packedDemands.get(container)) {
			SupplyPoint p = getSupplyPoint(getDemand(demandAsPacked));
			if(edge==null||p.getTime()>=edge.getEndTime()) {
				if(point==null||(p.getTime()<point.getTime())) {
					point = p;
				}
			}
		}
		return point;
	}
	
	/**
	 * Gets the current supply point in the manifesting process that a resource
	 * container is assigned to.
	 * 
	 * @param container the resource container
	 * 
	 * @return the current supply point
	 */
	public SupplyPoint getCurrentSupplyPoint(I_ResourceContainer container) {
		SupplyPoint point = null;
		for(SupplyPoint p : supplyPoints) {
			if(cachedContainerDemands.get(p).contains(container)
					&& (point==null||p.getTime()<point.getTime())) {
				point = p;
			}
		}
		return point;
	}
	
	/**
	 * Gets the set of empty resource containers.
	 * 
	 * @return the set of empty resource containers
	 */
	public Set<I_ResourceContainer> getEmptyContainers() {
		HashSet<I_ResourceContainer> containers = new HashSet<I_ResourceContainer>();
		for(I_ResourceContainer container : packedDemands.keySet()) {
			if(packedDemands.get(container).size()==0
					&& getInitialSupplyPoint(container)==null)
				containers.add(container);
		}
		return containers;
	}
	
	/**
	 * Checks whether a demand is associated with an aggregated edge demand.
	 * 
	 * @param demand the demand
	 * 
	 * @return whether a demand is an edge demand
	 */
	public boolean isEdgeDemand(Demand demand) {
		for(Demand d : aggregatedEdgeDemands.get(getSupplyPoint(demand).getEdge())) {
			if(d.equals(demand)) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether a resource container contains any demands associated with
	 * an aggregated edge demand.
	 * 
	 * @param container the resource container
	 * 
	 * @return whether the resource container contains any edge demands
	 */
	public boolean isEdgeDemand(I_ResourceContainer container) {
		if(packedDemands.get(container)!=null) {
			for(Demand demandAsPacked : packedDemands.get(container)) {
				Demand demand = getDemand(demandAsPacked);
				if(isEdgeDemand(demand)) {
					boolean isManifested = false;
					SupplyPoint point = getSupplyPoint(demand);
					for(I_Carrier carrier : point.getEdge().getAllCarriers()) {
						if(manifestedContainers.get(point.getEdge()).get(carrier).contains(container))
							isManifested = true;
					}
					if(!isManifested) return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Automatically packs a demand into an appropriate choice of resource
	 * container.
	 * 
	 * @param d the aggregated demand to pack
	 */
	public void autoPackDemand(Demand d) {
		if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS1)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS1)) {
			// TODO: temporarily use liquid tanks
			while(getRemainingAmount(d) > 0) {
				I_ResourceContainer container;
				if(getRemainingAmount(d)*d.getResource().getUnitMass() > ResourceContainerFactory.LTD_MAX_MASS
						|| (GlobalParameters.isVolumeConstrained()&&getRemainingAmount(d)*d.getResource().getUnitVolume() > ResourceContainerFactory.LTD_MAX_VOLUME)) {
					container = ResourceContainerFactory.createLT();
				} else {
					container = ResourceContainerFactory.createLTD();
				}
				addContainer(container);
				packDemand(d, container);
			}
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS203)) {
			// use gas tank and gas tank derivative
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if(container.getTid()==ResourceContainerFactory.GT_TID
						|| container.getTid()==ResourceContainerFactory.GTD_TID) {
					if(canPackDemand(d, container)) packDemand(d, container);
				}
			}
			while(getRemainingAmount(d) > 0) {
				I_ResourceContainer container;
				if(getRemainingAmount(d)*d.getResource().getUnitMass() > ResourceContainerFactory.GTD_MAX_MASS
						|| (GlobalParameters.isVolumeConstrained()&&getRemainingAmount(d)*d.getResource().getUnitVolume() > ResourceContainerFactory.GTD_MAX_VOLUME)) {
					container = ResourceContainerFactory.createGT();
				} else {
					container = ResourceContainerFactory.createGTD();
				}
				addContainer(container);
				packDemand(d, container);
			}
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS201)) {
			// use liquid tank and liquid tank derivative
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if(container.getTid()==ResourceContainerFactory.LT_TID
						|| container.getTid()==ResourceContainerFactory.LTD_TID) {
					if(canPackDemand(d, container)) packDemand(d, container);
				}
			}
			while(getRemainingAmount(d) > 0) {
				I_ResourceContainer container;
				if(getRemainingAmount(d)*d.getResource().getUnitMass() > ResourceContainerFactory.LTD_MAX_MASS
						|| (GlobalParameters.isVolumeConstrained()&&getRemainingAmount(d)*d.getResource().getUnitVolume() > ResourceContainerFactory.LTD_MAX_VOLUME)) {
					container = ResourceContainerFactory.createLT();
				} else {
					container = ResourceContainerFactory.createLTD();
				}
				addContainer(container);
				packDemand(d, container);
			}
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS2)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS2)
				|| d.getResource().getClassOfSupply().equals(ClassOfSupply.COS3)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS3)
				|| d.getResource().getClassOfSupply().equals(ClassOfSupply.COS7)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS7)){
			// use ctb
			// try existing ctb's
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if(container.getTid()==ResourceContainerFactory.CTB_TID
						||container.getTid()==ResourceContainerFactory.HCTB_TID) {
					if(canPackDemand(d, container)) packDemand(d, container);
				}
			}
			// create new ctb's
			while(getRemainingAmount(d) > 0) {
				I_ResourceContainer container;
				if(getRemainingAmount(d)*d.getResource().getUnitMass() > ResourceContainerFactory.HCTB_MAX_MASS
						|| (GlobalParameters.isVolumeConstrained()&&getRemainingAmount(d)*d.getResource().getUnitVolume() > ResourceContainerFactory.HCTB_MAX_VOLUME)) {
					container = ResourceContainerFactory.createCTB();
				} else {
					container = ResourceContainerFactory.createHCTB();
				}
				addContainer(container);
				packDemand(d, container);
			}
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS4)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS4)) {
			// try existing shoss boxes
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if((d.getResource().getEnvironment().equals(Environment.UNPRESSURIZED)
						&& container.getTid()==ResourceContainerFactory.SHOSS_TID)
						|| (d.getResource().getEnvironment().equals(Environment.PRESSURIZED)
						&& container.getTid()==ResourceContainerFactory.PSHOSS_TID)) {
					if(canPackDemand(d, container)) packDemand(d, container);
				}
			}
			// create new shoss boxes
			while(getRemainingAmount(d) > 0) {
				I_ResourceContainer container;
				if(d.getResource().getEnvironment().equals(Environment.UNPRESSURIZED)) {
					container = ResourceContainerFactory.createShoss();
				} else {
					container = ResourceContainerFactory.createPressShoss();
				}
				addContainer(container);
				packDemand(d, container);
			}
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS5)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS5)) {
			// ignore demand -- only an estimate of stowage/restraint
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS6)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS6)) {
			// use custom resource container
			I_ResourceContainer container;
			container = new ResourceContainer();
			container.setMaxCargoMass(d.getMass()*1.1);
			container.setVolume(d.getVolume()*1.1);
			container.setMaxCargoVolume(d.getVolume()*1.1);
			container.setName("Science " + container.getUid());
			if(d.getResource().getEnvironment().equals(Environment.UNPRESSURIZED)) {
				container.setMass(d.getMass()*d.getResource().getPackingFactor());
			} else {
				container.setMass(d.getMass()*d.getResource().getPackingFactor());
				container.setCargoEnvironment(Environment.PRESSURIZED);
			}
			addContainer(container);
			packDemand(d, container);
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS8)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS8)
				|| d.getResource().getClassOfSupply().equals(ClassOfSupply.COS9)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS9)) {
			throw new RuntimeException("COS8, COS9 demands not yet handled");
		} else if(d.getResource().getClassOfSupply().equals(ClassOfSupply.COS10)
				|| d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS10)) {
			// use custom resource container
			I_ResourceContainer container;
			container = new ResourceContainer();
			container.setMaxCargoMass(d.getMass()*1.1);
			container.setVolume(d.getVolume()*1.1);
			container.setMaxCargoVolume(d.getVolume()*1.1);
			container.setName("Miscellaneous " + container.getUid());
			if(d.getResource().getEnvironment().equals(Environment.UNPRESSURIZED)) {
				container.setMass(d.getMass()*d.getResource().getPackingFactor());
			} else {
				container.setMass(d.getMass()*d.getResource().getPackingFactor());
				container.setCargoEnvironment(Environment.PRESSURIZED);
			}
			addContainer(container);
			packDemand(d, container);
		}
	}
	
	/**
	 * Packs a demand into a resource container.
	 * 
	 * @param demand the aggregated demand
	 * @param container the resource container
	 */
	public void packDemand(Demand demand, I_ResourceContainer container) {
		SupplyPoint demandPoint = getSupplyPoint(demand);
		SupplyPoint containerPoint = getCurrentSupplyPoint(container);
		
		if(containerPoint==null 
				|| (demandPoint.getNode().equals(containerPoint.getNode())
				&& demandPoint.getTime() <= containerPoint.getTime())) {
			double remainingAmountMass = demand.getResource().getUnitMass()==0?
					Double.MAX_VALUE:GlobalParameters.getRoundedDemand((container.getMaxCargoMass() - getCargoMass(container, containerPoint))/demand.getResource().getUnitMass());
			double remainingAmountVolume = (!GlobalParameters.isVolumeConstrained()||demand.getResource().getUnitVolume()==0)?
					Double.MAX_VALUE:GlobalParameters.getRoundedDemand((container.getMaxCargoVolume() - getCargoVolume(container, containerPoint))/demand.getResource().getUnitVolume());
			
			double amount = GlobalParameters.isEnvironmentConstrained() 
				&& demand.getResource().getEnvironment()==Environment.PRESSURIZED 
				&& container.getCargoEnvironment()==Environment.UNPRESSURIZED?
					0:Math.min(getRemainingAmount(demand), Math.min(remainingAmountMass, remainingAmountVolume));
			if(amount > 0) {
				Demand demandAsPacked = new Demand(demand.getResource(), amount);
				demandsAsPacked.get(demand).add(demandAsPacked);
				packedDemands.get(container).add(demandAsPacked);
				rebuildCachedContainerDemands();
			}
		}
	}
	
	/**
	 * Unpacks an aggregated demand.
	 * 
	 * @param demand the aggregated demand
	 */
	public void unpackDemand(Demand demand) {
		if(canUnpackDemand(demand)) {
			for(Demand demandAsPacked : demandsAsPacked.get(demand)) {
				for(I_ResourceContainer container : packedDemands.keySet()) {
					if(packedDemands.get(container).contains(demandAsPacked)) {
						packedDemands.get(container).remove(demandAsPacked);
					}
				}
			}
			demandsAsPacked.get(demand).clear();
			rebuildCachedContainerDemands();
		}
	}
	
	/**
	 * Unpacks a packed demand from a resource container.
	 * 
	 * @param demandAsPacked the packed demand
	 * @param container the resourceContainer
	 */
	public void unpackDemand(Demand demandAsPacked, I_ResourceContainer container) {
		if(canUnpackDemand(demandAsPacked, container)) {
			packedDemands.get(container).remove(demandAsPacked);
			for(Demand d : demandsAsPacked.keySet()) {
				demandsAsPacked.get(d).remove(demandAsPacked);
			}
			rebuildCachedContainerDemands();
		}
	}
	
	/**
	 * Gets whether an aggregated demand can be unpacked.
	 * 
	 * @param demand the aggregated demand
	 * 
	 * @return whether the demand can be unpacked
	 */
	public boolean canUnpackDemand(Demand demand) {
		if(demand==null) return false;
		else if(getPackedAmount(demand)==0) return false;
		else return true;
	}
	
	/**
	 * Gets whether an aggregated demand can be auto-packed.
	 * 
	 * @param demand the aggregated demand
	 * 
	 * @return whether the demand can be auto-packed
	 */
	public boolean canAutoPackDemand(Demand demand) {
		if(demand==null) return false;
		else if(getRemainingAmount(demand)==0) return false;
		else return true;
	}
	
	/**
	 * Gets whether a packed demand can be unpacked from a resource container.
	 * 
	 * @param demandAsPacked the packed demand
	 * @param container the resource container
	 * 
	 * @return whether the packed demand can be unpacked from the container
	 */
	public boolean canUnpackDemand(Demand demandAsPacked, I_ResourceContainer container) {
		if(demandAsPacked==null || container==null) return false;
		else if(packedDemands.get(container).contains(demandAsPacked)) return true;
		else return false;
	}
	/**
	 * Gets whether a container has been manifested on a carrier to arrive at
	 * a given supply point. If false, the container has no present way of 
	 * arriving at the supply point.
	 * @param container	the resource container
	 * @param point	the supply point
	 * @return	whether the container has been manifested
	 */
	private boolean isManifested(I_ResourceContainer container, SupplyPoint point) {
		if(container==null || point==null) return false;
		for(SupplyEdge edge : supplyEdges) {
			if(edge.getPoint().getNode().equals(point.getNode())
					&& edge.getPoint().getTime() <= point.getTime()) {
				for(I_Carrier carrier : edge.getAllCarriers()) {
					if(manifestedContainers.get(edge).get(carrier).contains(container))
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets whether an aggregated demand can be packed in a resource container.
	 * 
	 * @param demand the aggregated demand
	 * @param container the resource container
	 * 
	 * @return whether the demand can be packed in the resource container
	 */
	public boolean canPackDemand(Demand demand, I_ResourceContainer container) {
		if(demand==null || container==null) return false;
		else if(getRemainingAmount(demand)>0 
				&& getCargoMass(container, getSupplyPoint(demand))-container.getMaxCargoMass()<GlobalParameters.getMassPrecision()/2d
				&& (!GlobalParameters.isVolumeConstrained()||getCargoVolume(container, getSupplyPoint(demand))-container.getMaxCargoVolume()<GlobalParameters.getVolumePrecision()/2d)) {
			SupplyPoint containerPoint = getCurrentSupplyPoint(container);
			SupplyPoint demandPoint = getSupplyPoint(demand);
			if(containerPoint==null
					&& !isManifested(container, containerPoint)) {
				return true;
			} else if(isEdgeDemand(demand)) {
				if(demandPoint.equals(containerPoint) 
							&& !isManifested(container, containerPoint)) {
					return true;
				} else return false;
			} else {
				if(demandPoint.getNode().equals(containerPoint.getNode()) 
						&& demandPoint.getTime() <= containerPoint.getTime()
						&& !isManifested(container, containerPoint)) {
					return true;
				} else return false;
			}
		} else return false;
	}
	
	/**
	 * Gets whether a resource container can be unmanifested from a supply edge.
	 * 
	 * @param container the resource container
	 * @param edge the supply edge
	 * 
	 * @return whether the container can be unmanifested from the edge
	 */
	public boolean canUnmanifestContainer(I_ResourceContainer container, SupplyEdge edge) {
		if(container==null || edge==null) return false;
		for(I_Carrier carrier : manifestedContainers.get(edge).keySet()) {
			if(manifestedContainers.get(edge).get(carrier).contains(container)) return true;
		}
		return false;
	}
	
	/**
	 * Gets whether a resource container can be manifested onto a supply edge on
	 * a carrier.
	 * 
	 * @param container the resource container
	 * @param edge the supply edge
	 * @param carrier the carrier
	 * 
	 * @return whether the container can be manifested on the supply edge in the carrier
	 */
	public boolean canManifestContainer(I_ResourceContainer container, SupplyEdge edge, I_Carrier carrier) {
		if(container==null || edge==null || carrier==null) return false;
		
		SupplyPoint point = getCurrentSupplyPoint(container);
		
		if(isEdgeDemand(container)) {
			if(point!=null
				&& edge.equals(point.getEdge())
				&& manifestedContainers.get(edge).keySet().contains(carrier)
				&& getCargoMass(carrier, point)+container.getMass()+getCargoMass(container, point)-carrier.getMaxCargoMass()<GlobalParameters.getMassPrecision()/2d
				&& (!GlobalParameters.isVolumeConstrained()||getCargoVolume(carrier, point)+container.getVolume()-carrier.getMaxCargoVolume()<GlobalParameters.getVolumePrecision()/2d)
				&& (!GlobalParameters.isEnvironmentConstrained()||!(container.getEnvironment()==Environment.PRESSURIZED&&carrier.getCargoEnvironment()==Environment.UNPRESSURIZED))
				&& !isManifested(container, point)) {
				return true;
			} else return false;
		} else if(point!=null 
				&& edge.getDestination().equals(point.getNode()) 
				&& edge.getEndTime() <= point.getTime()
				&& manifestedContainers.get(edge).keySet().contains(carrier)
				&& getCargoMass(carrier, point)+container.getMass()+getCargoMass(container, point)-carrier.getMaxCargoMass()<GlobalParameters.getMassPrecision()/2d
				&& (!GlobalParameters.isVolumeConstrained()||getCargoVolume(carrier, point)+container.getVolume()-carrier.getMaxCargoVolume()<GlobalParameters.getVolumePrecision()/2d)
				&& (!GlobalParameters.isEnvironmentConstrained()||!(container.getEnvironment()==Environment.PRESSURIZED&&carrier.getCargoEnvironment()==Environment.UNPRESSURIZED))
				&& !isManifested(container, point)) {
			return true;
		} else return false;
	}
	
	/**
	 * Manifests a resource container onto a supply edge in a carrier.
	 * 
	 * @param container the resource container
	 * @param edge the supply edge
	 * @param carrier the carrier
	 */
	public void manifestContainer(I_ResourceContainer container, SupplyEdge edge, I_Carrier carrier) {
		if(canManifestContainer(container, edge, carrier)) {
			manifestedContainers.get(edge).get(carrier).add(container);
			rebuildCachedContainerDemands();
		}
	}
	
	/**
	 * Unmanifests a resource container from a supply edge.
	 * 
	 * @param container the resource container
	 * @param edge the supple edge
	 */
	public void unmanifestContainer(I_ResourceContainer container, SupplyEdge edge) {
		if(canUnmanifestContainer(container, edge)) {
			for(I_Carrier carrier : manifestedContainers.get(edge).keySet()) {
				manifestedContainers.get(edge).get(carrier).remove(container);
			}
			for(SupplyEdge e : supplyEdges) {
				// clean up earlier manifests... not guaranteed anymore
				if(e.getEndTime()<edge.getEndTime()) {
					for(I_Carrier carrier : manifestedContainers.get(e).keySet()) {
						manifestedContainers.get(e).get(carrier).remove(container);
					}
				}
			}
			rebuildCachedContainerDemands();
		}
	}
	
	/**
	 * Adds a resource container to the manifest.
	 * 
	 * @param container the resource container
	 */
	public void addContainer(I_ResourceContainer container) {
		packedDemands.put(container, new HashSet<Demand>());
	}
	
	/**
	 * Removes a resource container from the manifest.
	 * 
	 * @param container the resource container
	 */
	public void removeContainer(I_ResourceContainer container) {
		for(Demand demandAsPacked : packedDemands.get(container)) {
			for(DemandSet demands : aggregatedNodeDemands.values()) {
				for(Demand demand : demands) {
					demandsAsPacked.get(demand).remove(demandAsPacked);
				}
			}
			for(DemandSet demands : aggregatedEdgeDemands.values()) {
				for(Demand demand : demands) {
					demandsAsPacked.get(demand).remove(demandAsPacked);
				}
			}
		}
		packedDemands.remove(container);
		for(SupplyPoint point : supplyPoints) {
			cachedContainerDemands.get(point).remove(container);
		}
		for(SupplyEdge edge : supplyEdges) {
			for(I_Carrier carrier : edge.getAllCarriers()) {
				manifestedContainers.get(edge).get(carrier).remove(container);
			}
		}
	}
	
	/**
	 * Gets the set of supply edges.
	 * 
	 * @return the set of supply edges
	 */
	public SortedSet<SupplyEdge> getSupplyEdges() {
		return supplyEdges;
	}
	
	/**
	 * Sets the set of supply edges.
	 * 
	 * @param supplyEdges the set of supply edges
	 */
	public void setSupplyEdges(SortedSet<SupplyEdge> supplyEdges) {
		this.supplyEdges = supplyEdges;
		for(SupplyEdge edge : supplyEdges) {
			manifestedContainers.put(edge, new HashMap<I_Carrier, Set<I_ResourceContainer>>());
			for(I_Carrier carrier : edge.getAllCarriers()) {
				manifestedContainers.get(edge).put(carrier, new HashSet<I_ResourceContainer>());
			}
		}
	}
	
	/**
	 * Gets the set of supply points.
	 * 
	 * @return the set of supply points
	 */
	public SortedSet<SupplyPoint> getSupplyPoints() {
		return supplyPoints;
	}
	
	/**
	 * Sets the set of supply points.
	 * 
	 * @param supplyPoints the set of supply points
	 */
	public void setSupplyPoints(SortedSet<SupplyPoint> supplyPoints) {
		this.supplyPoints = supplyPoints;
		for(SupplyPoint point : supplyPoints) {
			cachedContainerDemands.put(point, new HashSet<I_ResourceContainer>());
		}
	}
	
	/**
	 * Gets the set of demands aggregated to supply points.
	 * 
	 * @return the set of aggregated demands
	 */
	public SortedMap<SupplyPoint, DemandSet> getAggregatedNodeDemands() {
		return aggregatedNodeDemands;
	}
	
	/**
	 * Sets the set of demands aggregated to supply points.
	 * 
	 * @param aggregatedNodeDemands the set of aggregated demands
	 */
	public void setAggregatedNodeDemands(SortedMap<SupplyPoint, DemandSet> aggregatedNodeDemands) {
		this.aggregatedNodeDemands = aggregatedNodeDemands;
		for(DemandSet demands : aggregatedNodeDemands.values()) {
			for(Demand demand : demands) {
				demandsAsPacked.put(demand, new HashSet<Demand>());
			}
		}
	}
	
	/**
	 * Gets the set of demands aggregated to supply edges.
	 * 
	 * @return the set of aggregated demands
	 */
	public SortedMap<SupplyEdge, DemandSet> getAggregatedEdgeDemands() {
		return aggregatedEdgeDemands;
	}
	
	/**
	 * Sets the set of demands aggregated to supply edges.
	 * 
	 * @param aggregatedEdgeDemands the set of aggregated demands
	 */
	public void setAggregatedEdgeDemands(SortedMap<SupplyEdge, DemandSet> aggregatedEdgeDemands) {
		this.aggregatedEdgeDemands = aggregatedEdgeDemands;
		for(DemandSet demands : aggregatedEdgeDemands.values()) {
			for(Demand demand : demands) {
				demandsAsPacked.put(demand, new HashSet<Demand>());
			}
		}
	}
	
	/**
	 * Gets the set of packed demands from aggregated demands.
	 * 
	 * @return the set of packed demands
	 */
	public Map<Demand, Set<Demand>> getDemandsAsPacked() {
		return demandsAsPacked;
	}
	
	/**
	 * Gets the set of packed demands in resource containers.
	 * 
	 * @return the set of packed demands
	 */
	public Map<I_ResourceContainer, Set<Demand>> getPackedDemands() {
		return packedDemands;
	}
	
	/**
	 * Gets the set of packed demands in a container at a point in time
	 * referenced by a supply point.
	 * 
	 * @param container the resource container
	 * @param point the supply point
	 * 
	 * @return the set of packed demands
	 */
	public Set<Demand> getPackedDemands(I_ResourceContainer container, SupplyPoint point) {
		HashSet<Demand> demands = new HashSet<Demand>();
		for(Demand demand : packedDemands.get(container)) { // got null pointer here ?
			if(getSupplyPoint(getDemand(demand)).getTime()>=point.getTime())
				demands.add(demand);
		}
		return demands;
	}
	
	/**
	 * Gets the set of resource containers at supply points.
	 * 
	 * @return the set of resource containers
	 */
	public SortedMap<SupplyPoint, Set<I_ResourceContainer>> getCachedContainerDemands() {
		return cachedContainerDemands;
	}
	
	/**
	 * Gets the set of resource containers manifested to a supply edge.
	 * 
	 * @return the set of resource containers
	 */
	public SortedMap<SupplyEdge, Map<I_Carrier, Set<I_ResourceContainer>>> getManifestedContainers() {
		return manifestedContainers;
	}
	
	/**
	 * Gets the set of resource containers manifested to a carrier at a point
	 * in time referenced by a supply point.
	 * 
	 * @param carrier the carrier
	 * @param point the supply point
	 * 
	 * @return the set of resource containers
	 */
	public Set<I_ResourceContainer> getManifestedContainers(I_Carrier carrier, SupplyPoint point) {
		HashSet<I_ResourceContainer> containers = new HashSet<I_ResourceContainer>();
		for(SupplyEdge edge : supplyEdges) {
			if(manifestedContainers.get(edge).keySet().contains(carrier)
					&& edge.getEndTime() <= point.getTime()) {
				for(I_ResourceContainer container : manifestedContainers.get(edge).get(carrier)) {
					containers.add(container);
				}
			}
		}
		return containers;
	}
	private void rebuildCachedContainerDemands() {
		for(SupplyPoint point : supplyPoints) {
			cachedContainerDemands.get(point).clear();
		}
		
		for(I_ResourceContainer container : packedDemands.keySet()) {
			SupplyEdge firstEdge = null;
			for(SupplyEdge edge : supplyEdges) {
				for(I_Carrier carrier : edge.getAllCarriers()) {
					if(manifestedContainers.get(edge).get(carrier).contains(container)) {
						SupplyPoint point = getNextSupplyPoint(edge);
						if(point != null) cachedContainerDemands.get(point).add(container);
						if(firstEdge==null||edge.getStartTime()>firstEdge.getStartTime()) {
							firstEdge = edge;
						}
					}
				}
			}
			SupplyPoint firstPoint = firstEdge==null?null:firstEdge.getPoint();
			for(Demand demandAsPacked : packedDemands.get(container)) {
				SupplyPoint point = getSupplyPoint(getDemand(demandAsPacked));
				if(firstPoint==null||(point.getTime()<firstPoint.getTime())) {
					firstPoint = point;
				}
			}
			if(firstPoint!=null) {
				cachedContainerDemands.get(firstPoint).add(container);
			}
		}
	}
	
	/**
	 * Resets the manifest and all data members.
	 */
	public void reset() {
		supplyEdges.clear();
		supplyPoints.clear();
		aggregatedNodeDemands.clear();
		aggregatedEdgeDemands.clear();
		demandsAsPacked.clear();
		packedDemands.clear();
		cachedContainerDemands.clear();
		manifestedContainers.clear();
	}
	
	/**
	 * Gets the set of all resource containers.
	 * 
	 * @return the set of resource containers
	 */
	public Set<I_ResourceContainer> getContainers() {
		return packedDemands.keySet();
	}
	
	/**
	 * Generates a set of manifest events used at simulation time.
	 * 
	 * @return the set of manifest events
	 */
	public Set<ManifestEvent> generateManifestEvents() {
		HashSet<ManifestEvent> events = new HashSet<ManifestEvent>();
		for(I_ResourceContainer container : packedDemands.keySet()) {
			container.getContents().clear();
			for(Demand demand : packedDemands.get(container)) {
				if(!container.add(demand.getResource(), demand.getAmount())) {
					System.out.println(demand + " could not be added to " + container);
					//throw new RuntimeException("ERROR!");
				}
			}
		}
		for(SupplyEdge edge : supplyEdges) {
			for(I_Carrier carrier : edge.getAllCarriers()) {
				if(manifestedContainers.get(edge).get(carrier).size()>0) {
					ManifestEvent event = new ManifestEvent();
					event.setName("Manifest Event");
					event.setPriority(0);
					event.setTime(edge.getStartTime());
					event.setLocation(edge.getOrigin());
					for(I_Element element : getScenario().getElements()) {
						if(element.equals(carrier)) {
							event.setCarrier((I_Carrier)element);
							break;
						}
					}
					for(I_ResourceContainer container : manifestedContainers.get(edge).get(carrier)) {
						event.getContainers().add(container);
					}
					events.add(event);
				}
			}
		}
		return events;
	}
	
	/**
	 * Runs an auto-manifesting algorithm.
	 */
	public void autoManifest() {
		for(SupplyPoint point : supplyPoints) {
			// pack edge demands first
			for(Demand demand : aggregatedEdgeDemands.get(point.getEdge())) {
				autoPackDemand(demand);
			}
			// then pack node demands
			for(Demand demand : aggregatedNodeDemands.get(point)) {
				autoPackDemand(demand);
			}
			// manifest edge demands first
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if(point.equals(getCurrentSupplyPoint(container))
						&& isEdgeDemand(container)) {
					boolean isManifested = false;
					for(SupplyEdge edge : supplyEdges) {
						for(I_Carrier carrier : edge.getCarriers()) {
							isManifested = canManifestContainer(container, edge, carrier);
							manifestContainer(container, edge, carrier);
							if(isManifested) break;
						}
						if(isManifested) break;
					}
				}
			}
			// then manifest node demands
			for(I_ResourceContainer container : packedDemands.keySet()) {
				if(point.equals(getCurrentSupplyPoint(container))
						&& !isEdgeDemand(container)) {
					boolean isManifested = false;
					for(SupplyEdge edge : supplyEdges) {
						for(I_Carrier carrier : edge.getCarriers()) {
							if(canManifestContainer(container, edge, carrier)) {
								isManifested = canManifestContainer(container, edge, carrier);
								manifestContainer(container, edge, carrier);
								if(isManifested) break;
							}
						}
						if(isManifested) break;
					}
				}
			}
		}
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}
}