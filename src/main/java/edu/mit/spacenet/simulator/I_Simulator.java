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
package edu.mit.spacenet.simulator;

import java.util.List;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * Interface for a simulator that executes events and updates a network of
 * objects.
 * 
 * @author Paul Grogan
 */
public interface I_Simulator {
	
	/**
	 * Runs the simulation.
	 */
	public void simulate();
	
	/**
	 * Gets the current simulation time.
	 * 
	 * @return the current simulation time (days)
	 */
	public double getTime();
	
	/**
	 * Schedules an event.
	 * 
	 * @param event the event
	 */
	public void schedule(I_Event event);
	
	/**
	 * Gets a list of warnings generated during simulation.
	 * 
	 * @return the list of warnings
	 */
	public List<SimWarning> getWarnings();
	
	/**
	 * Gets a list of errors generated during simulation.
	 * 
	 * @return the list of errors
	 */
	public List<SimError> getErrors();
	
	/**
	 * Gets a list of spatial errors generated during simulation.
	 * 
	 * @return a list of errors.
	 */
	public List<SimSpatialError> getSpatialErrors();
	
	/**
	 * Gets a list of demand errors generated during simulation.
	 * 
	 * @return a list of errors.
	 */
	public List<SimDemand> getUnsatisfiedDemands();
	
	/**
	 * Gets a list of the scavenging operations.
	 * 
	 * @return the list of scavenging operations
	 */
	public List<SimScavenge> getScavengedParts();
	
	/**
	 * Gets a list of the repairing operations.
	 * 
	 * @return the list of the repairing operations
	 */
	public List<SimRepair> getRepairedParts();
	
	/**
	 * Gets the demand history for an element.
	 * 
	 * @param element the element
	 * 
	 * @return the demand history
	 */
	public List<SimDemand> getDemandHistory(I_Element element);
	
	/**
	 * Gets the demand history for a mission.
	 * 
	 * @param mission the mission
	 * 
	 * @return the demand history
	 */
	public List<SimDemand> getDemandHistory(Mission mission);
	
	/**
	 * Gets the demand history at a location.
	 * 
	 * @param location the location
	 * 
	 * @return the demand history
	 */
	public List<SimDemand> getDemandHistory(Location location);
	
	/**
	 * Gets the aggregated set of demands associated with a mission.
	 * 
	 * @param mission the mission
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(Mission mission);
	
	/**
	 * Gets the aggregated set of demands associated with a mission, filtered
	 * by a class of supply.
	 * 
	 * @param mission the mission
	 * @param cos the class of supply
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(Mission mission, ClassOfSupply cos);
	
	/**
	 * Gets the aggregated set of demands associated with an element during a
	 * mission.
	 * 
	 * @param mission the mission
	 * @param element the element
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(Mission mission, I_Element element);
	
	/**
	 * Gets the aggregated set of demands associated with an element during a
	 * mission, filtered by a class of supply.
	 * 
	 * @param mission the mission
	 * @param element the element
	 * @param cos the class of supply
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(Mission mission, I_Element element, ClassOfSupply cos);
	
	/**
	 * Gets the aggregated set of demands associated with an element, filtered
	 * by a class of supply.
	 * 
	 * @param element the element
	 * @param cos the class of supply
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(I_Element element, ClassOfSupply cos);
	
	/**
	 * Gets the aggregated set of demands from a location, filtered by a class
	 * of supply.
	 * 
	 * @param location the location
	 * @param cos the class of supply
	 * 
	 * @return the set of demands
	 */
	public DemandSet getDemands(Location location, ClassOfSupply cos);
	
	/**
	 * Gets the scenario being simulated.
	 * 
	 * @return the scenario
	 */
	public Scenario getScenario();
	
	/**
	 * Gets whether estimated packing demands are added to the demands.
	 * 
	 * @return whether estimated packing demands are included
	 */
	public boolean isPackingDemandsAdded();
	
	/**
	 * Sets whether estimated packing demands are added to the demands.
	 * 
	 * @param packingDemandsAdded whether estimated packing demands are included
	 */
	public void setPackingDemandsAdded(boolean packingDemandsAdded);
	
	/**
	 * Checks if is resources consumed.
	 * 
	 * @return true, if is resources consumed
	 */
	public boolean isDemandsSatisfied();
	
	/**
	 * Sets the resources consumed.
	 * 
	 * @param demandsSatisfied the new resources consumed
	 */
	public void setDemandsSatisfied(boolean demandsSatisfied);
}
