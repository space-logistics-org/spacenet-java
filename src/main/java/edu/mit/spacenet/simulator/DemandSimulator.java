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
package edu.mit.spacenet.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.RepairItem;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.scenario.SupplyPoint;
import edu.mit.spacenet.simulator.event.I_Transport;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.util.DateFunctions;
import edu.mit.spacenet.util.SerializeUtil;

/**
 * A simulator that logs detailed demand information.
 * 
 * @author Paul Grogan
 */
public class DemandSimulator extends AbstractSimulator {
  private Map<Integer, ArrayList<RepairItem>> sortedRepairItems;
  private Map<Integer, ArrayList<RepairItem>> unsortedRepairItems;

  private SortedSet<SupplyEdge> supplyEdges;
  private SortedSet<SupplyPoint> supplyPoints;
  private SortedMap<SupplyPoint, DemandSet> aggregatedNodeDemands;
  private SortedMap<SupplyEdge, DemandSet> aggregatedEdgeDemands;

  /**
   * The constructor sets the scenario and initializes the data structures.
   * 
   * @param scenario the scenario
   */
  public DemandSimulator(Scenario scenario) {
    super(scenario);

    sortedRepairItems = new HashMap<Integer, ArrayList<RepairItem>>();
    unsortedRepairItems = new HashMap<Integer, ArrayList<RepairItem>>();

    supplyEdges = new TreeSet<SupplyEdge>();
    supplyPoints = new TreeSet<SupplyPoint>();
    aggregatedNodeDemands = new TreeMap<SupplyPoint, DemandSet>();
    aggregatedEdgeDemands = new TreeMap<SupplyEdge, DemandSet>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#simulate()
   */
  public void simulate() {
    initializeSimulation();

    sortedRepairItems.clear();
    unsortedRepairItems.clear();

    for (Mission mission : getScenario().getMissionList()) {
      sortedRepairItems.put(getScenario().getMissionList().indexOf(mission),
          new ArrayList<RepairItem>());
      unsortedRepairItems.put(getScenario().getMissionList().indexOf(mission),
          new ArrayList<RepairItem>());
    }

    supplyEdges.clear();
    supplyPoints.clear();
    aggregatedNodeDemands.clear();
    aggregatedEdgeDemands.clear();

    // simulate events, serializing and saving after each time step
    while (getEvents().peek() != null) {
      getNextEvent();

      // find supply edges and supply points
      if (event instanceof I_Transport) {
        HashSet<I_Carrier> carriers = new HashSet<I_Carrier>();
        for (I_Element element : ((I_Transport) event).getElements()) {
          if (element instanceof I_Carrier) {
            carriers.add((I_Carrier) SerializeUtil.deepClone(element));
          }
        }

        boolean isReversed = false;
        if (event instanceof SurfaceTransport)
          isReversed = ((SurfaceTransport) event).isReversed();

        SupplyEdge edge = new SupplyEdge(((I_Transport) event).getEdge(), isReversed,
            ((I_Transport) event).getTime(),
            ((I_Transport) event).getTime() + ((I_Transport) event).getDuration(), carriers);
        supplyEdges.add(edge);
        aggregatedEdgeDemands.put(edge, new DemandSet());
        supplyPoints.add(edge.getPoint());
        aggregatedNodeDemands.put(edge.getPoint(), new DemandSet());
      }

      handleDemands();

      executeEvent();
    }
    tabulateRepairableDemands();
    aggregateDemands();
  }

  private void tabulateRepairableDemands() {
    double lastEndTime = 0;
    for (int index = 0; index < getScenario().getMissionList().size(); index++) {
      Mission mission = getScenario().getMissionList().get(index);
      if (mission.isCrewed()) {
        double startTime = lastEndTime;
        double endTime =
            DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate())
                + mission.getDuration();
        lastEndTime = endTime;

        for (SimDemand simDemand : getUnsatisfiedDemands()) {
          if (simDemand.getTime() > startTime && simDemand.getTime() <= endTime
              && simDemand.getElement() != null) {
            for (Demand demand : simDemand.getDemands()) {
              if (demand.getResource() instanceof Item) {
                for (PartApplication app : simDemand.getElement().getParts()) {
                  if (app.getPart().equals(demand.getResource()) && app.getMeanTimeToRepair() > 0) {
                    RepairItem i = new RepairItem(demand, simDemand.getElement());
                    sortedRepairItems.get(index).add(i);
                    break;
                  }
                }
              }
            }
          }
        }

        for (RepairItem i : sortedRepairItems.get(index)) {
          unsortedRepairItems.get(index).add(i);
        }

        Collections.sort(sortedRepairItems.get(index));
      }
    }
  }

  private void aggregateDemands() {
    for (SimDemand demand : getUnsatisfiedDemands()) {
      if (demand.getLocation() instanceof Node) {
        SupplyPoint point = null;
        for (SupplyPoint p : supplyPoints) {
          if (p.getNode().equals(demand.getLocation()) && p.getTime() <= demand.getTime()
              && (point == null || p.getTime() > point.getTime())) {
            point = p;
          }
        }
        if (point == null && demand.getLocation() instanceof SurfaceNode
            && ((SurfaceNode) demand.getLocation()).getBody() == Body.EARTH) {
          // ignore demands at earth surface nodes
        } else if (point == null) {
          System.out.println("No supply point found to satisfy demands! " + demand.getTime() + " "
              + demand.getDemands());
        } else {
          aggregatedNodeDemands.get(point).addAll(demand.getDemands());
        }
      } else if (demand.getLocation() instanceof Edge) {
        SupplyEdge edge = null;
        for (SupplyEdge e : supplyEdges) {
          if (e.getEdge().equals((demand.getLocation())) && demand.getTime() >= e.getStartTime()
              && demand.getTime() <= e.getEndTime()) {
            edge = e;
          }
        }
        if (edge == null) {
          System.out.println("No supply edge found to satisfy demands! " + demand.getTime() + " "
              + demand.getDemands());
        } else {
          aggregatedEdgeDemands.get(edge).addAll(demand.getDemands());
        }
      }
    }
  }

  /**
   * Gets the repairable items, unsorted (in order of occurrence in simulation).
   * 
   * @return the set of repairable items
   */
  public Map<Integer, ArrayList<RepairItem>> getUnsortedRepairItems() {
    return Collections.unmodifiableMap(unsortedRepairItems);
  }

  /**
   * Gets the repairable items, sorted by decreasing value.
   * 
   * @return the set of repairable items
   */
  public Map<Integer, ArrayList<RepairItem>> getSortedRepairItems() {
    return Collections.unmodifiableMap(sortedRepairItems);
  }

  /**
   * Gets the set of supply edges.
   * 
   * @return the set supply edges
   */
  public Set<SupplyEdge> getSupplyEdges() {
    return Collections.unmodifiableSet(supplyEdges);
  }

  /**
   * Gets the set of supply points.
   * 
   * @return the set of supply points
   */
  public Set<SupplyPoint> getSupplyPoints() {
    return Collections.unmodifiableSet(supplyPoints);
  }

  /**
   * Gets the demands aggregated to supply points (nodes).
   * 
   * @return the aggregated set of demands
   */
  public Map<SupplyPoint, DemandSet> getAggregatedNodeDemands() {
    return Collections.unmodifiableMap(aggregatedNodeDemands);
  }

  /**
   * Gets the demands aggregated to supply edges (edges).
   * 
   * @return the aggregated set of demands
   */
  public Map<SupplyEdge, DemandSet> getAggregatedEdgeDemands() {
    return Collections.unmodifiableMap(aggregatedEdgeDemands);
  }
}
