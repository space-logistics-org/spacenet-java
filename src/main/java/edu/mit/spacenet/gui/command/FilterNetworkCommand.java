/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.mit.spacenet.gui.command;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.scenario.Scenario;

/**
 * The command to filter the scenario network based on its scenario type. Any nodes or edges that
 * are in use will not be removed by this operation.
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

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.command.I_Command#execute()
   */
  public void execute() {
    List<Location> forAddition = new ArrayList<Location>();
    List<Location> forRemoval = new ArrayList<Location>();
    for (Node node : scenario.getDataSource().getNodeLibrary()) {
      if (!scenario.getNetwork().getNodes().contains(node)
          && scenario.getScenarioType().isNodeVisible(node)) {
        forAddition.add(node);
      } else if (scenario.getNetwork().getNodes().contains(node) && !scenario.isNodeUsed(node)
          && !scenario.getScenarioType().isNodeVisible(node)) {
        forRemoval.add(node);
      }
    }
    for (Edge edge : scenario.getDataSource().getEdgeLibrary()) {
      if (!scenario.getNetwork().getEdges().contains(edge)
          && scenario.getScenarioType().isEdgeVisible(edge)) {
        forAddition.add(edge);
      } else if (scenario.getNetwork().getEdges().contains(edge) && !scenario.isEdgeUsed(edge)
          && !scenario.getScenarioType().isEdgeVisible(edge)) {
        forRemoval.add(edge);
      }
    }
    for (Location location : forAddition) {
      scenario.getNetwork().add(location);
    }
    for (Location location : forRemoval) {
      scenario.getNetwork().remove(location);
    }
  }
}
