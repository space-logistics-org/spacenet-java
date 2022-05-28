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
package edu.mit.spacenet.scenario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.I_Transport;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The scenario class is used to store all information used in a simulation. It is the class that is
 * serialized to a file when saving.
 * 
 * @author Paul Grogan
 */
public class Scenario {
  private String name;
  private String description;
  private Date startDate;
  private String filePath;
  private String createdBy;
  private ScenarioType scenarioType;

  private I_DataSource dataSource;

  private Network network;

  private List<Mission> missionList;

  private Manifest manifest;

  /* Scenario Options */
  private double timePrecision, demandPrecision, massPrecision, volumePrecision;
  private boolean volumeConstrained, environmentConstrained;

  /* Demand Options */
  private ItemDiscretization itemDiscretization;
  private double itemAggregation;
  private boolean scavengeSpares;
  private Map<Mission, Set<RepairItem>> repairedItems;

  /* Simulation Options */
  private boolean detailedEva;
  private boolean detailedExploration;

  /**
   * The default constructor sets a default name and date, chooses the lunar scenario type, and
   * initializes the data structures.
   */
  public Scenario() {
    setName("New Scenario");
    setStartDate(new Date());
    setScenarioType(ScenarioType.LUNAR);
    network = new Network();
    missionList = new ArrayList<Mission>();
    manifest = new Manifest(this);
    detailedEva = true;
    detailedExploration = true;

    timePrecision = GlobalParameters.getSingleton().getTimePrecision();
    demandPrecision = GlobalParameters.getSingleton().getDemandPrecision();
    massPrecision = GlobalParameters.getSingleton().getMassPrecision();
    volumePrecision = GlobalParameters.getSingleton().getVolumePrecision();
    volumeConstrained = GlobalParameters.getSingleton().isVolumeConstrained();
    environmentConstrained = GlobalParameters.getSingleton().isEnvironmentConstrained();

    itemDiscretization = GlobalParameters.getSingleton().getItemDiscretization();
    itemAggregation = GlobalParameters.getSingleton().getItemAggregation();
    scavengeSpares = GlobalParameters.getSingleton().isScavengeSpares();
    repairedItems = new HashMap<Mission, Set<RepairItem>>();
  }

  /**
   * Gets a list of all the elements instantiated in the scenario.
   * 
   * @return the list of scenario elements
   */
  public List<I_Element> getElements() {
    ArrayList<I_Element> elements = new ArrayList<I_Element>();
    for (Mission m : missionList) {
      elements.addAll(m.getElements());
    }
    return elements;
  }

  /**
   * Gets a scenario element by its unique identifier.
   * 
   * @param uid the unique identifier
   * 
   * @return the element
   */
  public I_Element getElementByUid(int uid) {
    for (I_Element e : getElements()) {
      if (e.getUid() == uid)
        return e;
    }
    return null;
  }

  /**
   * Gets the scenario network.
   * 
   * @return the network
   */
  public Network getNetwork() {
    return this.network;
  }

  /**
   * Gets the scenario name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the scenario name.
   * 
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the scenario description.
   * 
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the scenario description.
   * 
   * @param description the scenario description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the scenario start date.
   * 
   * @return the start date
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Sets the scenario start date.
   * 
   * @param startDate the start date
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * Gets the created-by text.
   * 
   * @return the created-by text
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets the created-by text.
   * 
   * @param createdBy the created-by text
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Gets the scenario type.
   * 
   * @return the scenario type
   */
  public ScenarioType getScenarioType() {
    return scenarioType;
  }

  /**
   * Sets the scenario type.
   * 
   * @param scenarioType the scenario type
   */
  public void setScenarioType(ScenarioType scenarioType) {
    this.scenarioType = scenarioType;
  }

  /**
   * Gets the mission list.
   * 
   * @return the mission list
   */
  public List<Mission> getMissionList() {
    return this.missionList;
  }

  /**
   * Gets the manifest.
   * 
   * @return the manifest
   */
  public Manifest getManifest() {
    return manifest;
  }

  /**
   * Gets the data source.
   * 
   * @return the data source
   */
  public I_DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Sets the data source.
   * 
   * @param dataSource the data source
   */
  public void setDataSource(I_DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Prints the.
   */
  public void print() {
    System.out.println("Scenario: " + getName());
    System.out.println();
    System.out.println("Mission List");
    for (Mission m : missionList) {
      System.out.println("\t" + m.getName());
    }
    System.out.println();
    dataSource.print();
    System.out.println();
  }

  /**
   * Gets the scenario file path.
   * 
   * @return the file path, null if never saved
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * Gets the scenario file name.
   * 
   * @return the file name, empty string if never saved
   */
  public String getFileName() {
    if (filePath == null || filePath.length() == 0)
      return "";
    int lastIndex = Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\"));
    return filePath.substring(lastIndex + 1, filePath.length());
  }

  /**
   * Sets the scenario file path.
   * 
   * @param filePath the file path
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Gets whether a node is utilized somewhere in the scenario.
   * 
   * @param node the node
   * 
   * @return true if the node is used, false otherwise
   */
  public boolean isNodeUsed(Node node) {
    return getNodeUses(node).size() > 0;
  }

  /**
   * Gets a list of usages of a node.
   * 
   * @param node the node
   * 
   * @return a list of objects that use the node
   */
  public List<Object> getNodeUses(Node node) {
    ArrayList<Object> uses = new ArrayList<Object>();
    for (Mission m : getMissionList()) {
      if (node.equals(m.getOrigin()) || node.equals(m.getDestination())
          || node.equals(m.getReturnOrigin()) || node.equals(m.getReturnDestination()))
        uses.add(m);
      for (I_Event e : m.getEventList()) {
        if (e.getLocation().equals(node)
            || (e instanceof I_Transport && (node.equals(((I_Transport) e).getOrigin())
                || node.equals(((I_Transport) e).getDestination())))) {
          if (!uses.contains(m))
            uses.add(m);
          uses.add(e);
        }
      }
    }
    return uses;
  }

  /**
   * Gets whether an edge is utilized somewhere in the scenario.
   * 
   * @param edge the edge
   * 
   * @return true if the edge is used, false otherwise
   */
  public boolean isEdgeUsed(Edge edge) {
    return getEdgeUses(edge).size() > 0;
  }

  /**
   * Gets a list of usages of an edge.
   * 
   * @param edge the edge
   * 
   * @return a list of objects that use the edge
   */
  public List<Object> getEdgeUses(Edge edge) {
    ArrayList<Object> uses = new ArrayList<Object>();
    for (Mission m : getMissionList()) {
      for (I_Event e : m.getEventList()) {
        if (e instanceof I_Transport && edge.equals(((I_Transport) e).getEdge())) {
          uses.add(m);
          uses.add(e);
        }
      }
    }
    return uses;
  }

  /**
   * Gets whether detailed EVAs should be used in simulation. Detailed EVAs generate Move,
   * Reconfigure, and Demand events and can bog down large simulations.
   * 
   * @return whether detailed EVAs are used
   */
  public boolean isDetailedEva() {
    return detailedEva;
  }

  /**
   * Sets whether detailed EVAs should be used in simulation. Detailed EVAs generate Move,
   * Reconfigure, and Demand events and can bog down large simulations.
   * 
   * @param detailedEva whether detailed EVAs are used
   */
  public void setDetailedEva(boolean detailedEva) {
    this.detailedEva = detailedEva;
  }

  /**
   * Sets whether detailed explorations should be used in simulation. Detailed explorations generate
   * EVA events and can bog down large simulations.
   * 
   * @param detailedExploration whether detailed explorations are used
   */
  public void setDetailedExploration(boolean detailedExploration) {
    this.detailedExploration = detailedExploration;
  }

  /**
   * Gets whether detailed explorations should be used in simulation. Detailed explorations generate
   * EVA events and can bog down large simulations.
   * 
   * @return whether detailed explorations are used
   */
  public boolean isDetailedExploration() {
    return detailedExploration;
  }

  /**
   * Gets the time precision (smallest unit of time).
   * 
   * @return the time precision (days)
   */
  public double getTimePrecision() {
    return timePrecision;
  }

  /**
   * Sets the time precision (smallest unit of time).
   * 
   * @param timePrecision the time precision (days)
   */
  public void setTimePrecision(double timePrecision) {
    this.timePrecision = timePrecision;
    GlobalParameters.getSingleton().setTimePrecision(timePrecision);
  }

  /**
   * Gets the demand precision (smallest unit of resource amount).
   * 
   * @return the demand precision (units)
   */
  public double getDemandPrecision() {
    return demandPrecision;
  }

  /**
   * Sets the demand precision (smallest unit of resource amount).
   * 
   * @param demandPrecision the demand precision (units)
   */
  public void setDemandPrecision(double demandPrecision) {
    this.demandPrecision = demandPrecision;
    GlobalParameters.getSingleton().setDemandPrecision(demandPrecision);
  }

  /**
   * Gets the mass precision (smallest unit of mass).
   * 
   * @return the mass precision (kilograms)
   */
  public double getMassPrecision() {
    return massPrecision;
  }

  /**
   * Sets the mass precision (smallest unit of mass).
   * 
   * @param massPrecision the mass precision (kilograms)
   */
  public void setMassPrecision(double massPrecision) {
    this.massPrecision = massPrecision;
    GlobalParameters.getSingleton().setMassPrecision(massPrecision);
  }

  /**
   * Gets the volume precision (smallest unit of volume).
   * 
   * @return the volume precision (cubic meters)
   */
  public double getVolumePrecision() {
    return volumePrecision;
  }

  /**
   * Sets the volume precision (smallest unit of volume).
   * 
   * @param volumePrecision the volume precision (cubic meters)
   */
  public void setVolumePrecision(double volumePrecision) {
    this.volumePrecision = volumePrecision;
    GlobalParameters.getSingleton().setVolumePrecision(volumePrecision);
  }

  /**
   * Gets whether volume constraints should be enforced.
   * 
   * @return volume constraints enforcement
   */
  public boolean isVolumeConstrained() {
    return volumeConstrained;
  }

  /**
   * Sets whether volume constraints should be enforced.
   * 
   * @param volumeConstrained volume constraints enforcement
   */
  public void setVolumeConstrained(boolean volumeConstrained) {
    this.volumeConstrained = volumeConstrained;
    GlobalParameters.getSingleton().setVolumeConstrained(volumeConstrained);
  }

  /**
   * Gets whether environment constraints should be enforced.
   * 
   * @return environment constraints enforcement
   */
  public boolean isEnvironmentConstrained() {
    return environmentConstrained;
  }

  /**
   * Sets whether environment constraints should be enforced.
   * 
   * @param environmentConstrained environment constraints enforcement
   */
  public void setEnvironmentConstrained(boolean environmentConstrained) {
    this.environmentConstrained = environmentConstrained;
    GlobalParameters.getSingleton().setEnvironmentConstrained(environmentConstrained);
  }

  /**
   * Gets the item discretization scheme.
   * 
   * @return the item discretization
   */
  public ItemDiscretization getItemDiscretization() {
    return itemDiscretization;
  }

  /**
   * Sets the item discretization scheme.
   * 
   * @param itemDiscretization the item discretization
   */
  public void setItemDiscretization(ItemDiscretization itemDiscretization) {
    this.itemDiscretization = itemDiscretization;
    GlobalParameters.getSingleton().setItemDiscretization(itemDiscretization);
  }

  /**
   * Gets whether spares scavenging is allowed.
   * 
   * @return spares scavenging allowed
   */
  public boolean isScavengeSpares() {
    return scavengeSpares;
  }

  /**
   * Sets whether spares scavenging is allowed.
   * 
   * @param scavengeSpares spares scavenging allowed
   */
  public void setScavengeSpares(boolean scavengeSpares) {
    this.scavengeSpares = scavengeSpares;
    GlobalParameters.getSingleton().setScavengeSpares(scavengeSpares);
  }

  /**
   * Gets the item aggregation point.
   * 
   * @return the item aggregation (0=pre-aggregation, 1=post-aggregation)
   */
  public double getItemAggregation() {
    return itemAggregation;
  }

  /**
   * Sets the item aggregation point. param itemAggregation the item aggregation (0=pre-aggregation,
   * 1=post-aggregation)
   * 
   * @param itemAggregation the item aggregation
   */
  public void setItemAggregation(double itemAggregation) {
    this.itemAggregation = itemAggregation;
    GlobalParameters.getSingleton().setItemAggregation(itemAggregation);
  }

  /**
   * Gets the list of repair tasks.
   * 
   * @return the repaired items
   */
  public Map<Mission, Set<RepairItem>> getRepairedItems() {
    return repairedItems;
  }
}
