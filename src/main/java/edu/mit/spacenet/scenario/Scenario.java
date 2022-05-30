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

  /* (Override) Packing Factor Options */
  private Double genericPackingFactorGas = null;
  private Double genericPackingFactorLiquid = null;
  private Double genericPackingFactorPressurized = null;
  private Double genericPackingFactorUnpressurized = null;

  /* (Override) Manifested Resource Containers */
  private Double smallGasTankMass = null;
  private Double smallGasTankVolume = null;
  private Double smallGasTankMaxMass = null;
  private Double smallGasTankMaxVolume = null;
  private Double largeGasTankMass = null;
  private Double largeGasTankVolume = null;
  private Double largeGasTankMaxMass = null;
  private Double largeGasTankMaxVolume = null;
  private Double smallLiquidTankMass = null;
  private Double smallLiquidTankVolume = null;
  private Double smallLiquidTankMaxMass = null;
  private Double smallLiquidTankMaxVolume = null;
  private Double largeLiquidTankMass = null;
  private Double largeLiquidTankVolume = null;
  private Double largeLiquidTankMaxMass = null;
  private Double largeLiquidTankMaxVolume = null;
  private Double cargoTransferBagMass = null;
  private Double cargoTransferBagVolume = null;
  private Double cargoTransferBagMaxMass = null;
  private Double cargoTransferBagMaxVolume = null;

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

    setGenericPackingFactorGas(GlobalParameters.getSingleton().getGenericPackingFactorGas());
    setGenericPackingFactorLiquid(GlobalParameters.getSingleton().getGenericPackingFactorLiquid());
    setGenericPackingFactorPressurized(
        GlobalParameters.getSingleton().getGenericPackingFactorPressurizedInternal());
    setGenericPackingFactorUnpressurized(
        GlobalParameters.getSingleton().getGenericPackingFactorUnpressurized());

    setSmallGasTankMass(GlobalParameters.getSingleton().getSmallGasTankMass());
    setSmallGasTankVolume(GlobalParameters.getSingleton().getSmallGasTankVolume());
    setSmallGasTankMaxMass(GlobalParameters.getSingleton().getSmallGasTankMaxMass());
    setSmallGasTankMaxVolume(GlobalParameters.getSingleton().getSmallGasTankMaxVolume());

    setLargeGasTankMass(GlobalParameters.getSingleton().getLargeGasTankMass());
    setLargeGasTankVolume(GlobalParameters.getSingleton().getLargeGasTankVolume());
    setLargeGasTankMaxMass(GlobalParameters.getSingleton().getLargeGasTankMaxMass());
    setLargeGasTankMaxVolume(GlobalParameters.getSingleton().getLargeGasTankMaxVolume());

    setSmallLiquidTankMass(GlobalParameters.getSingleton().getSmallLiquidTankMass());
    setSmallLiquidTankVolume(GlobalParameters.getSingleton().getSmallLiquidTankVolume());
    setSmallLiquidTankMaxMass(GlobalParameters.getSingleton().getSmallLiquidTankMaxMass());
    setSmallLiquidTankMaxVolume(GlobalParameters.getSingleton().getSmallLiquidTankMaxVolume());

    setLargeLiquidTankMass(GlobalParameters.getSingleton().getLargeLiquidTankMass());
    setLargeLiquidTankVolume(GlobalParameters.getSingleton().getLargeLiquidTankVolume());
    setLargeLiquidTankMaxMass(GlobalParameters.getSingleton().getLargeLiquidTankMaxMass());
    setLargeLiquidTankMaxVolume(GlobalParameters.getSingleton().getLargeLiquidTankMaxVolume());

    setCargoTransferBagMass(GlobalParameters.getSingleton().getCargoTransferBagMass());
    setCargoTransferBagVolume(GlobalParameters.getSingleton().getCargoTransferBagVolume());
    setCargoTransferBagMaxMass(GlobalParameters.getSingleton().getCargoTransferBagMaxMass());
    setCargoTransferBagMaxVolume(GlobalParameters.getSingleton().getCargoTransferBagMaxVolume());
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

  /**
   * Gets the generic packing factor gas.
   *
   * @return the generic packing factor gas
   */
  public Double getGenericPackingFactorGas() {
    if (genericPackingFactorGas == null) {
      genericPackingFactorGas = GlobalParameters.getSingleton().getGenericPackingFactorGas();
    }
    return genericPackingFactorGas;
  }

  /**
   * Sets the generic packing factor gas.
   *
   * @param genericPackingFactorGas the new generic packing factor gas
   */
  public void setGenericPackingFactorGas(Double genericPackingFactorGas) {
    this.genericPackingFactorGas = genericPackingFactorGas;
    GlobalParameters.getSingleton().setGenericPackingFactorGas(genericPackingFactorGas);
  }

  /**
   * Gets the generic packing factor liquid.
   *
   * @return the generic packing factor liquid
   */
  public Double getGenericPackingFactorLiquid() {
    if (genericPackingFactorLiquid == null) {
      genericPackingFactorLiquid = GlobalParameters.getSingleton().getGenericPackingFactorLiquid();
    }
    return genericPackingFactorLiquid;
  }

  /**
   * Sets the generic packing factor liquid.
   *
   * @param genericPackingFactorLiquid the new generic packing factor liquid
   */
  public void setGenericPackingFactorLiquid(Double genericPackingFactorLiquid) {
    this.genericPackingFactorLiquid = genericPackingFactorLiquid;
    GlobalParameters.getSingleton().setGenericPackingFactorLiquid(genericPackingFactorLiquid);
  }

  /**
   * Gets the generic packing factor pressurized.
   *
   * @return the generic packing factor pressurized
   */
  public Double getGenericPackingFactorPressurized() {
    if (genericPackingFactorPressurized == null) {
      genericPackingFactorPressurized =
          GlobalParameters.getSingleton().getGenericPackingFactorPressurizedInternal();
    }
    return genericPackingFactorPressurized;
  }

  /**
   * Sets the generic packing factor pressurized.
   *
   * @param genericPackingFactorPressurized the new generic packing factor pressurized
   */
  public void setGenericPackingFactorPressurized(Double genericPackingFactorPressurized) {
    this.genericPackingFactorPressurized = genericPackingFactorPressurized;
    GlobalParameters.getSingleton()
        .setGenericPackingFactorPressurizedInternal(genericPackingFactorPressurized);
  }

  /**
   * Gets the generic packing factor unpressurized.
   *
   * @return the generic packing factor unpressurized
   */
  public Double getGenericPackingFactorUnpressurized() {
    if (genericPackingFactorUnpressurized == null) {
      genericPackingFactorUnpressurized =
          GlobalParameters.getSingleton().getGenericPackingFactorUnpressurized();
    }
    return genericPackingFactorUnpressurized;
  }

  /**
   * Sets the generic packing factor unpressurized.
   *
   * @param genericPackingFactorUnpressurized the new generic packing factor unpressurized
   */
  public void setGenericPackingFactorUnpressurized(Double genericPackingFactorUnpressurized) {
    this.genericPackingFactorUnpressurized = genericPackingFactorUnpressurized;
    GlobalParameters.getSingleton()
        .setGenericPackingFactorUnpressurized(genericPackingFactorUnpressurized);
  }

  /**
   * Gets the small gas tank mass.
   *
   * @return the small gas tank mass
   */
  public Double getSmallGasTankMass() {
    if (smallGasTankMass == null) {
      smallGasTankMass = GlobalParameters.getSingleton().getSmallGasTankMass();
    }
    return smallGasTankMass;
  }

  /**
   * Sets the small gas tank mass.
   *
   * @param smallGasTankMass the new small gas tank mass
   */
  public void setSmallGasTankMass(Double smallGasTankMass) {
    this.smallGasTankMass = smallGasTankMass;
    GlobalParameters.getSingleton().setSmallGasTankMass(smallGasTankMass);
  }

  /**
   * Gets the small gas tank volume.
   *
   * @return the small gas tank volume
   */
  public Double getSmallGasTankVolume() {
    if (smallGasTankVolume == null) {
      smallGasTankVolume = GlobalParameters.getSingleton().getSmallGasTankVolume();
    }
    return smallGasTankVolume;
  }

  /**
   * Sets the small gas tank volume.
   *
   * @param smallGasTankVolume the new small gas tank volume
   */
  public void setSmallGasTankVolume(Double smallGasTankVolume) {
    this.smallGasTankVolume = smallGasTankVolume;
    GlobalParameters.getSingleton().setSmallGasTankVolume(smallGasTankVolume);
  }

  /**
   * Gets the small gas tank max mass.
   *
   * @return the small gas tank max mass
   */
  public Double getSmallGasTankMaxMass() {
    if (smallGasTankMaxMass == null) {
      smallGasTankMaxMass = GlobalParameters.getSingleton().getSmallGasTankMaxMass();
    }
    return smallGasTankMaxMass;
  }

  /**
   * Sets the small gas tank max mass.
   *
   * @param smallGasTankMaxMass the new small gas tank max mass
   */
  public void setSmallGasTankMaxMass(Double smallGasTankMaxMass) {
    this.smallGasTankMaxMass = smallGasTankMaxMass;
    GlobalParameters.getSingleton().setSmallGasTankMaxMass(smallGasTankMaxMass);
  }

  /**
   * Gets the small gas tank max volume.
   *
   * @return the small gas tank max volume
   */
  public Double getSmallGasTankMaxVolume() {
    if (smallGasTankMaxVolume == null) {
      smallGasTankMaxVolume = GlobalParameters.getSingleton().getSmallGasTankMaxVolume();
    }
    return smallGasTankMaxVolume;
  }

  /**
   * Sets the small gas tank max volume.
   *
   * @param smallGasTankMaxVolume the new small gas tank max volume
   */
  public void setSmallGasTankMaxVolume(Double smallGasTankMaxVolume) {
    this.smallGasTankMaxVolume = smallGasTankMaxVolume;
    GlobalParameters.getSingleton().setSmallGasTankMaxVolume(smallGasTankMaxVolume);
  }

  /**
   * Gets the large gas tank mass.
   *
   * @return the large gas tank mass
   */
  public Double getLargeGasTankMass() {
    if (largeGasTankMass == null) {
      largeGasTankMass = GlobalParameters.getSingleton().getLargeGasTankMass();
    }
    return largeGasTankMass;
  }

  /**
   * Sets the large gas tank mass.
   *
   * @param largeGasTankMass the new large gas tank mass
   */
  public void setLargeGasTankMass(Double largeGasTankMass) {
    this.largeGasTankMass = largeGasTankMass;
    GlobalParameters.getSingleton().setLargeGasTankMass(largeGasTankMass);
  }

  /**
   * Gets the large gas tank volume.
   *
   * @return the large gas tank volume
   */
  public Double getLargeGasTankVolume() {
    if (largeGasTankVolume == null) {
      largeGasTankVolume = GlobalParameters.getSingleton().getLargeGasTankVolume();
    }
    return largeGasTankVolume;
  }

  /**
   * Sets the large gas tank volume.
   *
   * @param largeGasTankVolume the new large gas tank volume
   */
  public void setLargeGasTankVolume(Double largeGasTankVolume) {
    this.largeGasTankVolume = largeGasTankVolume;
    GlobalParameters.getSingleton().setLargeGasTankVolume(largeGasTankVolume);
  }

  /**
   * Gets the large gas tank max mass.
   *
   * @return the large gas tank max mass
   */
  public Double getLargeGasTankMaxMass() {
    if (largeGasTankMaxMass == null) {
      largeGasTankMaxMass = GlobalParameters.getSingleton().getLargeGasTankMaxMass();
    }
    return largeGasTankMaxMass;
  }

  /**
   * Sets the large gas tank max mass.
   *
   * @param largeGasTankMaxMass the new large gas tank max mass
   */
  public void setLargeGasTankMaxMass(Double largeGasTankMaxMass) {
    this.largeGasTankMaxMass = largeGasTankMaxMass;
    GlobalParameters.getSingleton().setLargeGasTankMaxMass(largeGasTankMaxMass);
  }

  /**
   * Gets the large gas tank max volume.
   *
   * @return the large gas tank max volume
   */
  public Double getLargeGasTankMaxVolume() {
    if (largeGasTankMaxVolume == null) {
      largeGasTankMaxVolume = GlobalParameters.getSingleton().getLargeGasTankMaxVolume();
    }
    return largeGasTankMaxVolume;
  }

  /**
   * Sets the large gas tank max volume.
   *
   * @param largeGasTankMaxVolume the new large gas tank max volume
   */
  public void setLargeGasTankMaxVolume(Double largeGasTankMaxVolume) {
    this.largeGasTankMaxVolume = largeGasTankMaxVolume;
    GlobalParameters.getSingleton().setLargeGasTankMaxVolume(largeGasTankMaxVolume);
  }

  /**
   * Gets the small liquid tank mass.
   *
   * @return the small liquid tank mass
   */
  public Double getSmallLiquidTankMass() {
    if (smallLiquidTankMass == null) {
      smallLiquidTankMass = GlobalParameters.getSingleton().getSmallLiquidTankMass();
    }
    return smallLiquidTankMass;
  }

  /**
   * Sets the small liquid tank mass.
   *
   * @param smallLiquidTankMass the new small liquid tank mass
   */
  public void setSmallLiquidTankMass(Double smallLiquidTankMass) {
    this.smallLiquidTankMass = smallLiquidTankMass;
    GlobalParameters.getSingleton().setSmallLiquidTankMass(smallLiquidTankMass);
  }

  /**
   * Gets the small liquid tank volume.
   *
   * @return the small liquid tank volume
   */
  public Double getSmallLiquidTankVolume() {
    if (smallLiquidTankVolume == null) {
      smallLiquidTankVolume = GlobalParameters.getSingleton().getSmallLiquidTankVolume();
    }
    return smallLiquidTankVolume;
  }

  /**
   * Sets the small liquid tank volume.
   *
   * @param smallLiquidTankVolume the new small liquid tank volume
   */
  public void setSmallLiquidTankVolume(Double smallLiquidTankVolume) {
    this.smallLiquidTankVolume = smallLiquidTankVolume;
    GlobalParameters.getSingleton().setSmallLiquidTankVolume(smallLiquidTankVolume);
  }

  /**
   * Gets the small liquid tank max mass.
   *
   * @return the small liquid tank max mass
   */
  public Double getSmallLiquidTankMaxMass() {
    if (smallLiquidTankMaxMass == null) {
      smallLiquidTankMaxMass = GlobalParameters.getSingleton().getSmallLiquidTankMaxMass();
    }
    return smallLiquidTankMaxMass;
  }

  /**
   * Sets the small liquid tank max mass.
   *
   * @param smallLiquidTankMaxMass the new small liquid tank max mass
   */
  public void setSmallLiquidTankMaxMass(Double smallLiquidTankMaxMass) {
    this.smallLiquidTankMaxMass = smallLiquidTankMaxMass;
    GlobalParameters.getSingleton().setSmallLiquidTankMaxMass(smallLiquidTankMaxMass);
  }

  /**
   * Gets the small liquid tank max volume.
   *
   * @return the small liquid tank max volume
   */
  public Double getSmallLiquidTankMaxVolume() {
    if (smallLiquidTankMaxVolume == null) {
      smallLiquidTankMaxVolume = GlobalParameters.getSingleton().getSmallLiquidTankMaxVolume();
    }
    return smallLiquidTankMaxVolume;
  }

  /**
   * Sets the small liquid tank max volume.
   *
   * @param smallLiquidTankMaxVolume the new small liquid tank max volume
   */
  public void setSmallLiquidTankMaxVolume(Double smallLiquidTankMaxVolume) {
    this.smallLiquidTankMaxVolume = smallLiquidTankMaxVolume;
    GlobalParameters.getSingleton().setSmallLiquidTankMaxVolume(smallLiquidTankMaxVolume);
  }

  /**
   * Gets the large liquid tank mass.
   *
   * @return the large liquid tank mass
   */
  public Double getLargeLiquidTankMass() {
    if (largeLiquidTankMass == null) {
      largeLiquidTankMass = GlobalParameters.getSingleton().getLargeLiquidTankMass();
    }
    return largeLiquidTankMass;
  }

  /**
   * Sets the large liquid tank mass.
   *
   * @param largeLiquidTankMass the new large liquid tank mass
   */
  public void setLargeLiquidTankMass(Double largeLiquidTankMass) {
    this.largeLiquidTankMass = largeLiquidTankMass;
    GlobalParameters.getSingleton().setLargeLiquidTankMass(largeLiquidTankMass);
  }

  /**
   * Gets the large liquid tank volume.
   *
   * @return the large liquid tank volume
   */
  public Double getLargeLiquidTankVolume() {
    if (largeLiquidTankVolume == null) {
      largeLiquidTankVolume = GlobalParameters.getSingleton().getLargeLiquidTankVolume();
    }
    return largeLiquidTankVolume;
  }

  /**
   * Sets the large liquid tank volume.
   *
   * @param largeLiquidTankVolume the new large liquid tank volume
   */
  public void setLargeLiquidTankVolume(Double largeLiquidTankVolume) {
    this.largeLiquidTankVolume = largeLiquidTankVolume;
    GlobalParameters.getSingleton().setLargeLiquidTankVolume(largeLiquidTankVolume);
  }

  /**
   * Gets the large liquid tank max mass.
   *
   * @return the large liquid tank max mass
   */
  public Double getLargeLiquidTankMaxMass() {
    if (largeLiquidTankMaxMass == null) {
      largeLiquidTankMaxMass = GlobalParameters.getSingleton().getLargeLiquidTankMaxMass();
    }
    return largeLiquidTankMaxMass;
  }

  /**
   * Sets the large liquid tank max mass.
   *
   * @param largeLiquidTankMaxMass the new large liquid tank max mass
   */
  public void setLargeLiquidTankMaxMass(Double largeLiquidTankMaxMass) {
    this.largeLiquidTankMaxMass = largeLiquidTankMaxMass;
    GlobalParameters.getSingleton().setLargeLiquidTankMaxMass(largeLiquidTankMaxMass);
  }

  /**
   * Gets the large liquid tank max volume.
   *
   * @return the large liquid tank max volume
   */
  public Double getLargeLiquidTankMaxVolume() {
    if (largeLiquidTankMaxVolume == null) {
      largeLiquidTankMaxVolume = GlobalParameters.getSingleton().getLargeLiquidTankMaxVolume();
    }
    return largeLiquidTankMaxVolume;
  }

  /**
   * Sets the large liquid tank max volume.
   *
   * @param largeLiquidTankMaxVolume the new large liquid tank max volume
   */
  public void setLargeLiquidTankMaxVolume(Double largeLiquidTankMaxVolume) {
    this.largeLiquidTankMaxVolume = largeLiquidTankMaxVolume;
    GlobalParameters.getSingleton().setLargeLiquidTankMaxVolume(largeLiquidTankMaxVolume);
  }

  /**
   * Gets the cargo transfer bag mass.
   *
   * @return the cargo transfer bag mass
   */
  public Double getCargoTransferBagMass() {
    if (cargoTransferBagMass == null) {
      cargoTransferBagMass = GlobalParameters.getSingleton().getCargoTransferBagMass();
    }
    return cargoTransferBagMass;
  }

  /**
   * Sets the cargo transfer bag mass.
   *
   * @param cargoTransferBagMass the new cargo transfer bag mass
   */
  public void setCargoTransferBagMass(Double cargoTransferBagMass) {
    this.cargoTransferBagMass = cargoTransferBagMass;
    GlobalParameters.getSingleton().setCargoTransferBagMass(cargoTransferBagMass);
  }

  /**
   * Gets the cargo transfer bag volume.
   *
   * @return the cargo transfer bag volume
   */
  public Double getCargoTransferBagVolume() {
    if (cargoTransferBagVolume == null) {
      cargoTransferBagVolume = GlobalParameters.getSingleton().getCargoTransferBagVolume();
    }
    return cargoTransferBagVolume;
  }

  /**
   * Sets the cargo transfer bag volume.
   *
   * @param cargoTransferBagVolume the new cargo transfer bag volume
   */
  public void setCargoTransferBagVolume(Double cargoTransferBagVolume) {
    this.cargoTransferBagVolume = cargoTransferBagVolume;
    GlobalParameters.getSingleton().setCargoTransferBagVolume(cargoTransferBagVolume);
  }

  /**
   * Gets the cargo transfer bag max mass.
   *
   * @return the cargo transfer bag max mass
   */
  public Double getCargoTransferBagMaxMass() {
    if (cargoTransferBagMaxMass == null) {
      cargoTransferBagMaxMass = GlobalParameters.getSingleton().getCargoTransferBagMaxMass();
    }
    return cargoTransferBagMaxMass;
  }

  /**
   * Sets the cargo transfer bag max mass.
   *
   * @param cargoTransferBagMaxMass the new cargo transfer bag max mass
   */
  public void setCargoTransferBagMaxMass(Double cargoTransferBagMaxMass) {
    this.cargoTransferBagMaxMass = cargoTransferBagMaxMass;
    GlobalParameters.getSingleton().setCargoTransferBagMaxMass(cargoTransferBagMaxMass);
  }

  /**
   * Gets the cargo transfer bag max volume.
   *
   * @return the cargo transfer bag max volume
   */
  public Double getCargoTransferBagMaxVolume() {
    if (cargoTransferBagMaxVolume == null) {
      cargoTransferBagMaxVolume = GlobalParameters.getSingleton().getCargoTransferBagMaxVolume();
    }
    return cargoTransferBagMaxVolume;
  }

  /**
   * Sets the cargo transfer bag max volume.
   *
   * @param cargoTransferBagMaxVolume the new cargo transfer bag max volume
   */
  public void setCargoTransferBagMaxVolume(Double cargoTransferBagMaxVolume) {
    this.cargoTransferBagMaxVolume = cargoTransferBagMaxVolume;
    GlobalParameters.getSingleton().setCargoTransferBagMaxVolume(cargoTransferBagMaxVolume);
  }
}
