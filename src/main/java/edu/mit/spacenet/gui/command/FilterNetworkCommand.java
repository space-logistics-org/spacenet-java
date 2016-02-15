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
package edu.mit.spacenet.gui.command;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.scenario.Scenario;

/**
 * The command to filter the scenario network based on its scenario type. Any
 * nodes or edges that are in use will not be removed by this operation.
 * 
 * @author Paul Grogan
 */
public class FilterNetworkCommand implements I_Command {
	private Scenario scenario;
	
	/**
	 * The constructor.
	 * 
	 * @param scenarioPanel the scenario panel component
	 */
	public FilterNetworkCommand(Scenario scenario) {
		this.scenario = scenario;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		List<Location> forAddition = new ArrayList<Location>();
		List<Location> forRemoval = new ArrayList<Location>();
		for(Node node : scenario.getDataSource().getNodeLibrary()) {
			if(!scenario.getNetwork().getNodes().contains(node)
					&& scenario.getScenarioType().isNodeVisible(node)) {
				forAddition.add(node);
			} else if(scenario.getNetwork().getNodes().contains(node)
					&& !scenario.isNodeUsed(node)
					&& !scenario.getScenarioType().isNodeVisible(node)) {
				forRemoval.add(node);
			}
		}
		for(Edge edge : scenario.getDataSource().getEdgeLibrary()) {
			if(!scenario.getNetwork().getEdges().contains(edge)
					&& scenario.getScenarioType().isEdgeVisible(edge)) {
				forAddition.add(edge);
			} else if(scenario.getNetwork().getEdges().contains(edge)
					&& !scenario.isEdgeUsed(edge)
					&& !scenario.getScenarioType().isEdgeVisible(edge)) {
				forRemoval.add(edge);
			}
		}
		for(Location location : forAddition) {
			scenario.getNetwork().add(location);
		}
		for(Location location : forRemoval) {
			scenario.getNetwork().remove(location);
		}
	}
}
