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
package edu.mit.spacenet.simulator;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Transport;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.simulator.moe.MoeCrewSurfaceDays;
import edu.mit.spacenet.simulator.moe.MoeCrewTime;
import edu.mit.spacenet.simulator.moe.MoeExplorationCapability;
import edu.mit.spacenet.simulator.moe.MoeExplorationMassDelivered;
import edu.mit.spacenet.simulator.moe.MoeLaunchMass;
import edu.mit.spacenet.simulator.moe.MoeMassCapacityUtilization;
import edu.mit.spacenet.util.SerializeUtil;

/**
 * A simulator that logs and generates measures of effectiveness.
 * 
 * @author Paul Grogan
 */
public class FullSimulator extends AbstractSimulator {
	private List<SimNetwork> networkHistory;
	private List<MoeCrewSurfaceDays> crewSurfaceDaysHistory;
	private List<MoeCrewTime> crewTimeHistory;
	private List<MoeExplorationMassDelivered> explorationMassDeliveredHistory;
	private List<MoeLaunchMass> launchMassHistory;
	private List<MoeMassCapacityUtilization> upMassCapacityUtilizationHistory, downMassCapacityUtilizationHistory;
	private List<MoeExplorationCapability> explorationCapabilityHistory;
	
	/**
	 * Instantiates a new full simulator.
	 * 
	 * @param scenario the scenario
	 */
	public FullSimulator(Scenario scenario) {
		super(scenario);
		setPackingDemandsAdded(false);
		setItemsRepaired(true);
		networkHistory = new ArrayList<SimNetwork>();
		crewSurfaceDaysHistory = new ArrayList<MoeCrewSurfaceDays>();
		crewTimeHistory = new ArrayList<MoeCrewTime>();
		explorationMassDeliveredHistory = new ArrayList<MoeExplorationMassDelivered>();
		launchMassHistory = new ArrayList<MoeLaunchMass>();
		upMassCapacityUtilizationHistory = new ArrayList<MoeMassCapacityUtilization>();
		downMassCapacityUtilizationHistory = new ArrayList<MoeMassCapacityUtilization>();
		explorationCapabilityHistory = new ArrayList<MoeExplorationCapability>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.simulator.I_Simulator#simulate()
	 */
	public void simulate() {
		initializeSimulation();
		networkHistory.clear();
		crewSurfaceDaysHistory.clear();
		crewTimeHistory.clear();
		explorationMassDeliveredHistory.clear();
		launchMassHistory.clear();
		upMassCapacityUtilizationHistory.clear();
		downMassCapacityUtilizationHistory.clear();
		explorationCapabilityHistory.clear();

		scheduleManifestEvents();
		
		// save initial conditions
		networkHistory.add(new SimNetwork(getTime(), (Network)SerializeUtil.deepClone(getScenario().getNetwork())));
		
		// simulate events, serializing and saving after each time step
		while(getEvents().peek() != null) {
			getNextEvent();
			
			// tabulate MOEs
			if(duration > 0) {
				for(I_Element element : getScenario().getNetwork().getRegistrar().values()) {
					if(element instanceof CrewMember) {
						crewTimeHistory.add(new MoeCrewTime(
								getTime(),
								element.getLocation(),
								MoeCrewTime.EXPLORATION,
								duration*24*((CrewMember)element).getAvailableTimeFraction()));
						crewTimeHistory.add(new MoeCrewTime(
								getTime(),
								element.getLocation(),
								MoeCrewTime.UNAVAILABLE,
								duration*24*(1-((CrewMember)element).getAvailableTimeFraction())));
						// if element location is a non-Earth surface node
						// OR a sun-orbiting node
						// OR a non-Earth surface edge
						if(((element.getLocation() instanceof SurfaceNode 
								&& !((SurfaceNode)element.getLocation()).getBody().equals(Body.EARTH)))
								|| (element.getLocation() instanceof OrbitalNode
										&& (((OrbitalNode)element.getLocation()).getBody().equals(Body.SUN)))
								|| (element.getLocation() instanceof SurfaceEdge 
										&& !((SurfaceEdge)element.getLocation()).getOrigin().getBody().equals(Body.EARTH))) {
							crewSurfaceDaysHistory.add(new MoeCrewSurfaceDays(
									getTime(), 
									element.getLocation(), 
									duration));
							explorationCapabilityHistory.add(new MoeExplorationCapability(
									getTime(), 
									element.getLocation(),
									element.getLocation().getTotalMass(ClassOfSupply.COS6, this)+element.getLocation().getTotalMass(ClassOfSupply.COS8, this),
									duration));
						}
					}
				}
			}
			// if event is a space transport OR flight transport
			// AND either the destination is a non-Earth surface node
			//     OR the destination is a sun-orbiting node
			if((event instanceof SpaceTransport || event instanceof FlightTransport)
					&& ((((I_Transport)event).getDestination() instanceof SurfaceNode 
							&& !((I_Transport)event).getDestination().getBody().equals(Body.EARTH))
							|| (((I_Transport)event).getDestination() instanceof OrbitalNode 
									&& (((OrbitalNode)((I_Transport)event).getDestination()).getBody().equals(Body.SUN))))) {
				double amount = 0;
				for(I_Element element : ((I_Transport)event).getElements()) {
					amount+=element.getTotalMass(ClassOfSupply.COS6);
					amount+=element.getTotalMass(ClassOfSupply.COS8);
				}
				explorationMassDeliveredHistory.add(new MoeExplorationMassDelivered(
						getTime()+((I_Transport)event).getDuration(),
						((I_Transport)event).getDestination(), 
						amount));
			}
			// if event is a space transport OR flight transport 
			// AND origin is an Earth surface node
			if((event instanceof SpaceTransport || event instanceof FlightTransport)
					&& ((I_Transport)event).getOrigin() instanceof SurfaceNode
					&& ((I_Transport)event).getOrigin().getBody().equals(Body.EARTH)) {
				double mass = 0;
				for(I_Element element : ((I_Transport)event).getElements()) {
					mass+= element.getTotalMass();
				}
				launchMassHistory.add(new MoeLaunchMass(
						getTime(), 
						((I_Transport)event).getOrigin(), 
						mass));
				
				double capacity = 0;
				if(event instanceof FlightTransport) {
					capacity = ((FlightTransport)event).getEdge().getMaxCargoMass();
				} else {
					for(I_Element element : ((I_Transport)event).getElements()) {
						if(element instanceof I_Carrier) {
							capacity += ((I_Carrier)element).getMaxCargoMass();
						}
					}
				}
				double amount = 0;
				for(I_Element element : ((I_Transport)event).getElements()) {
					if(element instanceof I_Carrier) {
						amount += ((I_Carrier)element).getCargoMass();
					}
				}
				upMassCapacityUtilizationHistory.add(new MoeMassCapacityUtilization(
						getTime(), 
						((I_Transport)event).getOrigin(), 
						amount, 
						capacity));
			// else if event is a space transport OR flight transport
			// AND destination is an Earth surface node
			} else if((event instanceof SpaceTransport || event instanceof FlightTransport)
					&& ((I_Transport)event).getDestination() instanceof SurfaceNode
					&& ((I_Transport)event).getDestination().getBody().equals(Body.EARTH)) {
				double capacity = 0;
				if(event instanceof FlightTransport) {
					capacity = ((FlightTransport)event).getEdge().getMaxCargoMass();
				} else {
					for(I_Element element : ((I_Transport)event).getElements()) {
						if(element instanceof I_Carrier) {
							capacity += ((I_Carrier)element).getMaxCargoMass();
						}
					}
				}
				double amount = 0;
				for(I_Element element : ((I_Transport)event).getElements()) {
					if(element instanceof I_Carrier) {
						amount += ((I_Carrier)element).getCargoMass();
					}
				}
				downMassCapacityUtilizationHistory.add(new MoeMassCapacityUtilization(
						getTime(), 
						((I_Transport)event).getDestination(), 
						amount,
						capacity));
			}
			
			handleDemands();
			
			executeEvent();
			
			if(getEvents().peek()==null||getEvents().peek().getTime() > getTime())
				networkHistory.add(new SimNetwork(getTime(), (Network)SerializeUtil.deepClone(getScenario().getNetwork())));
		}
	}
	
	/**
	 * Gets the network history.
	 * 
	 * @return the network history
	 */
	public List<SimNetwork> getNetworkHistory() {
		return networkHistory;
	}
	
	/**
	 * Gets the crew surface days history.
	 * 
	 * @return the crew surface days history
	 */
	public List<MoeCrewSurfaceDays> getCrewSurfaceDaysHistory() {
		return crewSurfaceDaysHistory;
	}
	
	/**
	 * Gets the crew time history.
	 * 
	 * @return the crew time history
	 */
	public List<MoeCrewTime> getCrewTimeHistory() {
		return crewTimeHistory;
	}
	
	/**
	 * Gets the exploration mass delivered history.
	 * 
	 * @return the exploration mass delivered history
	 */
	public List<MoeExplorationMassDelivered> getExplorationMassDeliveredHistory() {
		return explorationMassDeliveredHistory;
	}
	
	/**
	 * Gets the launch mass history.
	 * 
	 * @return the launch mass history
	 */
	public List<MoeLaunchMass> getLaunchMassHistory() {
		return launchMassHistory;
	}
	
	/**
	 * Gets the up mass capacity utilization history.
	 * 
	 * @return the up mass capacity utilization history
	 */
	public List<MoeMassCapacityUtilization> getUpMassCapacityUtilizationHistory() {
		return upMassCapacityUtilizationHistory;
	}
	
	/**
	 * Gets the down mass capacity utilization history.
	 * 
	 * @return the down mass capacity utilization history
	 */
	public List<MoeMassCapacityUtilization>  getDownMassCapacityUtilizationHistory() {
		return downMassCapacityUtilizationHistory;
	}
	
	/**
	 * Gets the exploration capability history.
	 * 
	 * @return the exploration capability history
	 */
	public List<MoeExplorationCapability> getExplorationCapabilityHistory() {
		return explorationCapabilityHistory;
	}
	
	/**
	 * Gets the total crew surface days.
	 * 
	 * @return the total crew surface days
	 */
	public double getTotalCrewSurfaceDays() {
		double amount = 0;
		for(MoeCrewSurfaceDays moe : crewSurfaceDaysHistory) {
			amount += moe.getAmount();
		}
		return amount;
	}
	
	/**
	 * Gets the total crew time.
	 * 
	 * @return the total crew time
	 */
	public double getTotalCrewTime() {
		double amount = 0;
		for(MoeCrewTime moe : crewTimeHistory) {
			amount += moe.getAmount();
		}
		return amount;
	}
	
	/**
	 * Gets the total exploration capability.
	 * 
	 * @return the total exploration capability
	 */
	public double getTotalExplorationCapability() {
		double amount = 0;
		for(MoeExplorationCapability moe : explorationCapabilityHistory) {
			amount += moe.getDuration()*moe.getMass();
		}
		return amount;
	}
	
	/**
	 * Gets the total exploration mass delivered.
	 * 
	 * @return the total exploration mass delivered
	 */
	public double getTotalExplorationMassDelivered() {
		double amount = 0;
		for(MoeExplorationMassDelivered moe : explorationMassDeliveredHistory) {
			amount += moe.getAmount();
		}
		return amount;
	}
	
	/**
	 * Gets the total launch mass.
	 * 
	 * @return the total launch mass
	 */
	public double getTotalLaunchMass() {
		double amount = 0;
		for(MoeLaunchMass moe : launchMassHistory) {
			amount += moe.getAmount();
		}
		return amount;
	}
	
	/**
	 * Gets the total up mass capacity utilization.
	 * 
	 * @return the total up mass capacity utilization
	 */
	public double getTotalUpMassCapacityUtilization() {
		double amount = 0;
		double capacity = 0;
		for(MoeMassCapacityUtilization moe : upMassCapacityUtilizationHistory) {
			amount += moe.getAmount();
			capacity += moe.getCapacity();
		}
		return amount/capacity;
	}
	
	/**
	 * Gets the total down mass capacity utilization.
	 * 
	 * @return the total down mass capacity utilization
	 */
	public double getTotalDownMassCapacityUtilization() {
		double amount = 0;
		double capacity = 0;
		for(MoeMassCapacityUtilization moe : downMassCapacityUtilizationHistory) {
			amount += moe.getAmount();
			capacity += moe.getCapacity();
		}
		return amount/capacity;
	}
	
	/** The Constant APOLLO_17_EXPLORATION_CAPABILITY. */
	public static final double APOLLO_17_EXPLORATION_CAPABILITY = 2594;
	
	/** The Constant APOLLO_17_LAUNCH_MASS. */
	public static final double APOLLO_17_LAUNCH_MASS = 2930000;
	
	/**
	 * Gets the total relative exploration capability.
	 * 
	 * @return the total relative exploration capability
	 */
	public double getTotalRelativeExplorationCapability() {
		return (getTotalExplorationCapability()/APOLLO_17_EXPLORATION_CAPABILITY)/(getTotalLaunchMass()/APOLLO_17_LAUNCH_MASS);
	}
}