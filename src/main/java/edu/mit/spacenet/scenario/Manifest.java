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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import edu.mit.spacenet.simulator.DemandSimulator;
import edu.mit.spacenet.simulator.event.ManifestEvent;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The manifest represents the packing of resources into resource containers, and the eventual
 * movement of resource containers into carriers during simulation.
 * 
 * @author Paul Grogan
 */
public class Manifest {
  private Scenario scenario;
  private Set<SupplyEdge> supplyEdges;
  private Set<SupplyPoint> supplyPoints;
  private Map<SupplyPoint, DemandSet> aggregatedNodeDemands;
  private Map<SupplyEdge, DemandSet> aggregatedEdgeDemands;
  private Map<Demand, Set<Demand>> demandsAsPacked;
  private Map<I_ResourceContainer, Set<Demand>> packedDemands;
  private Map<SupplyEdge, Map<I_Carrier, Set<I_ResourceContainer>>> manifestedContainers;

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
    return demand.getAmount() - getPackedAmount(demand);
  }

  /**
   * Gets the cargo mass of a given resource container at the point in time referenced by a supply
   * point.
   * 
   * @param container the container for which to find the cargo mass
   * @param point the point in time to reference
   * 
   * @return the cargo mass (kilograms)
   */
  public double getCargoMass(I_ResourceContainer container, SupplyPoint point) {
    double amount = 0;
    for (Demand demandAsPacked : getPackedDemands(container, point)) {
      amount += demandAsPacked.getMass();
    }
    return amount;
  }

  /**
   * Gets the cargo volume of a given resource container at a point in time referenced by a supply
   * point.
   * 
   * @param container the container for which to find the cargo volume
   * @param point the point in time to reference
   * 
   * @return the cargo volume (cubic meters)
   */
  public double getCargoVolume(I_ResourceContainer container, SupplyPoint point) {
    double amount = 0;
    for (Demand demandAsPacked : getPackedDemands(container, point)) {
      amount += demandAsPacked.getVolume();
    }
    return amount;
  }

  /**
   * Gets the cargo mass of a given carrier at a point in time referenced by a supply point.
   * 
   * @param carrier the carrier for which to find the cargo volume
   * @param point the point in time to reference
   * 
   * @return the cargo mass (kilograms)
   */
  public double getCargoMass(I_Carrier carrier, SupplyPoint point) {
    double amount = carrier.getCargoMass();
    for (I_ResourceContainer container : getManifestedContainers(carrier, point)) {
      amount += container.getMass() + getCargoMass(container, point);
    }
    return amount;
  }

  /**
   * Gets the cargo volume of a given carrier at a point in time referenced by a supply point.
   * 
   * @param carrier the carrier for which to find the cargo volume
   * @param point the point in time to reference
   * 
   * @return the cargo volume (cubic meters)
   */
  public double getCargoVolume(I_Carrier carrier, SupplyPoint point) {
    double amount = carrier.getCargoVolume();
    for (I_ResourceContainer container : getManifestedContainers(carrier, point)) {
      amount += container.getVolume();
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
    if (getDemandsAsPacked().get(demand) == null)
      return 0;
    double amount = 0;
    for (Demand demandAsPacked : getDemandsAsPacked().get(demand)) {
      amount += demandAsPacked.getAmount();
    }
    return GlobalParameters.getSingleton().getRoundedDemand(amount);
  }

  /**
   * Get the supply point associated with a given aggregated demand.
   * 
   * @param demand the demand to find the supply point
   * 
   * @return the supply point
   */
  public SupplyPoint getSupplyPoint(Demand demand) {
    for (SupplyPoint p : supplyPoints) {
      for (Demand d : aggregatedNodeDemands.get(p)) {
        if (d.equals(demand))
          return p;
      }
      for (Demand d : aggregatedEdgeDemands.get(p.getEdge())) {
        if (d.equals(demand))
          return p;
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
    if (edge.getOrigin() instanceof SurfaceNode && edge.getOrigin().getBody() == Body.EARTH)
      return null;

    SupplyPoint point = null;
    for (SupplyPoint p : supplyPoints) {
      if (p.getNode().equals(edge.getOrigin()) && p.getTime() <= edge.getStartTime()
          && (point == null || p.getTime() > point.getTime())) {
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
    for (Demand demand : demandsAsPacked.keySet()) {
      for (Demand d : demandsAsPacked.get(demand)) {
        if (d.equals(demandAsPacked))
          return demand;
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
    for (SupplyEdge e : supplyEdges) {
      for (I_Carrier c : e.getCarriers()) {
        // must use == because carriers are repeated on edges
        if (c == carrier)
          return e;
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
    for (SupplyEdge edge : supplyEdges) {
      if (edge.getDestination().equals(point.getNode()) && edge.getEndTime() <= point.getTime()) {
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
   * @return the initial supply point
   */
  public SupplyPoint getInitialSupplyPoint(I_ResourceContainer container) {
    SupplyEdge edge = null;
    for (SupplyEdge e : supplyEdges) {
      for (I_Carrier carrier : e.getCarriers()) {
        if (manifestedContainers.get(e).get(carrier).contains(container)
            && (edge == null || e.getEndTime() > edge.getEndTime())) {
          edge = e;
        }
      }
    }
    SupplyPoint point = edge == null ? null : edge.getPoint();
    for (Demand demandAsPacked : packedDemands.get(container)) {
      SupplyPoint p = getSupplyPoint(getDemand(demandAsPacked));
      if (edge == null || p.getTime() >= edge.getEndTime()) {
        if (point == null || (p.getTime() < point.getTime())) {
          point = p;
        }
      }
    }
    return point;
  }

  /**
   * Gets the current supply point in the manifesting process that a resource container is assigned
   * to.
   * 
   * @param container the resource container
   * 
   * @return the current supply point
   */
  private SupplyPoint getCurrentSupplyPoint(I_ResourceContainer container) {
    SupplyPoint point = getInitialSupplyPoint(container);
    if (point == null) {
      return null;
    }
    for (SupplyEdge edge : supplyEdges) {
      if (edge.getEndTime() <= point.getTime()) {
        for (I_Carrier carrier : edge.getAllCarriers()) {
          if (manifestedContainers.get(edge).get(carrier).contains(container)) {
            SupplyPoint p = getNextSupplyPoint(edge);
            if (p != null && (p.getTime() < point.getTime())) {
              point = p;
            }
          }
        }
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
    HashSet<I_ResourceContainer> emptyContainers = new HashSet<I_ResourceContainer>();
    Set<I_ResourceContainer> containers = packedDemands.keySet();
    for (I_ResourceContainer container : containers) {
      if (packedDemands.get(container).size() == 0 && getInitialSupplyPoint(container) == null)
        emptyContainers.add(container);
    }
    return Collections.unmodifiableSet(emptyContainers);
  }

  /**
   * Checks whether a demand is associated with an aggregated edge demand.
   * 
   * @param demand the demand
   * 
   * @return whether a demand is an edge demand
   */
  public boolean isEdgeDemand(Demand demand) {
    for (Demand d : aggregatedEdgeDemands.get(getSupplyPoint(demand).getEdge())) {
      if (d.equals(demand))
        return true;
    }
    return false;
  }

  /**
   * Checks whether a resource container contains any demands associated with an aggregated edge
   * demand.
   * 
   * @param container the resource container
   * 
   * @return whether the resource container contains any edge demands
   */
  public boolean isEdgeDemand(I_ResourceContainer container) {
    if (packedDemands.get(container) != null) {
      for (Demand demandAsPacked : packedDemands.get(container)) {
        Demand demand = getDemand(demandAsPacked);
        if (isEdgeDemand(demand)) {
          boolean isManifested = false;
          SupplyPoint point = getSupplyPoint(demand);
          for (I_Carrier carrier : point.getEdge().getAllCarriers()) {
            if (manifestedContainers.get(point.getEdge()).get(carrier).contains(container))
              isManifested = true;
          }
          if (!isManifested)
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Automatically packs a demand into an appropriate choice of resource container.
   * 
   * @param d the aggregated demand to pack
   */
  public void autoPackDemand(Demand d) {
    if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS1)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS1)) {
      // TODO: temporarily use liquid tanks
      while (getRemainingAmount(d) > 0) {
        I_ResourceContainer container;
        if (getRemainingAmount(d) * d.getResource().getUnitMass() > GlobalParameters.getSingleton()
            .getSmallLiquidTankMaxMass()
            || (GlobalParameters.getSingleton().isVolumeConstrained()
                && getRemainingAmount(d) * d.getResource().getUnitVolume() > GlobalParameters
                    .getSingleton().getSmallLiquidTankMaxVolume())) {
          container = ResourceContainerFactory.createLT();
        } else {
          container = ResourceContainerFactory.createLTD();
        }
        container.setEnvironment(d.getResource().getEnvironment());
        container.setCargoEnvironment(d.getResource().getEnvironment());
        addContainer(container);
        packDemand(d, container);
      }
    } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS203)) {
      // use gas tank and gas tank derivative
      for (I_ResourceContainer container : packedDemands.keySet()) {
        if (container.getTid() == ResourceContainerFactory.GT_TID
            || container.getTid() == ResourceContainerFactory.GTD_TID) {
          if (canPackDemand(d, container))
            packDemand(d, container);
        }
      }
      while (getRemainingAmount(d) > 0) {
        I_ResourceContainer container;
        if (getRemainingAmount(d) * d.getResource().getUnitMass() > GlobalParameters.getSingleton()
            .getSmallGasTankMaxMass()
            || (GlobalParameters.getSingleton().isVolumeConstrained()
                && getRemainingAmount(d) * d.getResource().getUnitVolume() > GlobalParameters
                    .getSingleton().getSmallGasTankMaxVolume())) {
          container = ResourceContainerFactory.createGT();
        } else {
          container = ResourceContainerFactory.createGTD();
        }
        container.setEnvironment(d.getResource().getEnvironment());
        container.setCargoEnvironment(d.getResource().getEnvironment());
        addContainer(container);
        packDemand(d, container);
      }
    } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS201)) {
      // use liquid tank and liquid tank derivative
      for (I_ResourceContainer container : packedDemands.keySet()) {
        if (container.getTid() == ResourceContainerFactory.LT_TID
            || container.getTid() == ResourceContainerFactory.LTD_TID) {
          if (canPackDemand(d, container))
            packDemand(d, container);
        }
      }
      while (getRemainingAmount(d) > 0) {
        I_ResourceContainer container;
        if (getRemainingAmount(d) * d.getResource().getUnitMass() > GlobalParameters.getSingleton()
            .getSmallLiquidTankMaxMass()
            || (GlobalParameters.getSingleton().isVolumeConstrained()
                && getRemainingAmount(d) * d.getResource().getUnitVolume() > GlobalParameters
                    .getSingleton().getSmallLiquidTankMaxVolume())) {
          container = ResourceContainerFactory.createLT();
        } else {
          container = ResourceContainerFactory.createLTD();
        }
        container.setEnvironment(d.getResource().getEnvironment());
        container.setCargoEnvironment(d.getResource().getEnvironment());
        addContainer(container);
        packDemand(d, container);
      }
    } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS2)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS2)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS3)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS3)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS4)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS4)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS7)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS7)) {
      // use ctb
      // try existing ctb's
      for (I_ResourceContainer container : packedDemands.keySet()) {
        if (container.getTid() == ResourceContainerFactory.CTB_TID) {
          if (canPackDemand(d, container))
            packDemand(d, container);
        }
      }
      // create new ctb's
      while (getRemainingAmount(d) > 0) {
        I_ResourceContainer container = ResourceContainerFactory.createCTB();
        container.setEnvironment(d.getResource().getEnvironment());
        container.setCargoEnvironment(d.getResource().getEnvironment());
        addContainer(container);
        packDemand(d, container);
      }
    } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS5)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS5)) {
      // ignore demand -- only an estimate of stowage/restraint
    } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS6)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS6)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS8)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS8)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS9)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS9)
        || d.getResource().getClassOfSupply().equals(ClassOfSupply.COS10)
        || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS10)) {
      // use custom resource container
      I_ResourceContainer container = new ResourceContainer();
      container.setMaxCargoMass(d.getMass());
      container.setVolume(d.getVolume());
      container.setMaxCargoVolume(d.getVolume());
      if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS6)
          || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS6)) {
        container.setName("Science " + container.getUid());
      } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS8)
          || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS8)) {
        container.setName("Infrastructure " + container.getUid());
      } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS9)
          || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS9)) {
        container.setName("Transportation " + container.getUid());
      } else if (d.getResource().getClassOfSupply().equals(ClassOfSupply.COS10)
          || d.getResource().getClassOfSupply().isSubclassOf(ClassOfSupply.COS10)) {
        container.setName("Miscellaneous " + container.getUid());
      }
      container.setMass(d.getMass() * d.getResource().getPackingFactor());
      container.setEnvironment(d.getResource().getEnvironment());
      container.setCargoEnvironment(d.getResource().getEnvironment());
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

    if (containerPoint == null || (demandPoint.getNode().equals(containerPoint.getNode())
        && demandPoint.getTime() <= containerPoint.getTime())) {
      double remainingAmountMass = demand.getResource().getUnitMass() == 0 ? Double.MAX_VALUE
          : GlobalParameters.getSingleton().getRoundedDemand(
              (container.getMaxCargoMass() - getCargoMass(container, containerPoint))
                  / demand.getResource().getUnitMass());
      double remainingAmountVolume = (!GlobalParameters.getSingleton().isVolumeConstrained()
          || demand.getResource().getUnitVolume() == 0)
              ? Double.MAX_VALUE
              : GlobalParameters.getSingleton().getRoundedDemand(
                  (container.getMaxCargoVolume() - getCargoVolume(container, containerPoint))
                      / demand.getResource().getUnitVolume());

      double amount = GlobalParameters.getSingleton().isEnvironmentConstrained()
          && demand.getResource().getEnvironment() == Environment.PRESSURIZED
          && container.getCargoEnvironment() == Environment.UNPRESSURIZED ? 0
              : Math.min(getRemainingAmount(demand),
                  Math.min(remainingAmountMass, remainingAmountVolume));
      if (amount > 0) {
        Demand demandAsPacked = new Demand(demand.getResource(), amount);
        demandsAsPacked.get(demand).add(demandAsPacked);
        packedDemands.get(container).add(demandAsPacked);
      }
    }
  }

  /**
   * Unpacks an aggregated demand.
   * 
   * @param demand the aggregated demand
   */
  public void unpackDemand(Demand demand) {
    if (canUnpackDemand(demand)) {
      for (Demand demandAsPacked : demandsAsPacked.get(demand)) {
        for (I_ResourceContainer container : packedDemands.keySet()) {
          if (packedDemands.get(container).contains(demandAsPacked)) {
            packedDemands.get(container).remove(demandAsPacked);
          }
        }
      }
      demandsAsPacked.get(demand).clear();
    }
  }

  /**
   * Unpacks a packed demand from a resource container.
   * 
   * @param demandAsPacked the packed demand
   * @param container the resourceContainer
   */
  public void unpackDemand(Demand demandAsPacked, I_ResourceContainer container) {
    if (canUnpackDemand(demandAsPacked, container)) {
      packedDemands.get(container).remove(demandAsPacked);
      for (Demand d : demandsAsPacked.keySet()) {
        demandsAsPacked.get(d).remove(demandAsPacked);
      }
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
    if (demand == null)
      return false;
    else if (getPackedAmount(demand) == 0)
      return false;
    else
      return true;
  }

  /**
   * Gets whether an aggregated demand can be auto-packed.
   * 
   * @param demand the aggregated demand
   * 
   * @return whether the demand can be auto-packed
   */
  public boolean canAutoPackDemand(Demand demand) {
    if (demand == null)
      return false;
    else if (getRemainingAmount(demand) == 0)
      return false;
    else
      return true;
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
    if (demandAsPacked == null || container == null)
      return false;
    else if (packedDemands.get(container).contains(demandAsPacked))
      return true;
    else
      return false;
  }

  /**
   * Gets whether a container has been manifested on a carrier to arrive at a given supply point. If
   * false, the container has no present way of arriving at the supply point.
   * 
   * @param container the resource container
   * @param point the supply point
   * @return whether the container has been manifested
   */
  private boolean isManifested(I_ResourceContainer container, SupplyPoint point) {
    if (container == null || point == null)
      return false;
    for (SupplyEdge edge : supplyEdges) {
      if (edge.getPoint().getNode().equals(point.getNode())
          && edge.getPoint().getTime() <= point.getTime()) {
        for (I_Carrier carrier : edge.getAllCarriers()) {
          if (manifestedContainers.get(edge).get(carrier).contains(container))
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
    if (demand == null || container == null)
      return false;
    else if (getRemainingAmount(demand) > 0
        && container.canAdd(demand.getResource(), demand.getAmount())
        && getCargoMass(container, getSupplyPoint(demand))
            - container.getMaxCargoMass() < GlobalParameters.getSingleton().getMassPrecision() / 2d
        && (!GlobalParameters.getSingleton().isVolumeConstrained()
            || getCargoVolume(container, getSupplyPoint(demand)) - container
                .getMaxCargoVolume() < GlobalParameters.getSingleton().getVolumePrecision() / 2d)) {
      SupplyPoint containerPoint = getCurrentSupplyPoint(container);
      SupplyPoint demandPoint = getSupplyPoint(demand);
      if (containerPoint == null && !isManifested(container, containerPoint)) {
        return true;
      } else if (isEdgeDemand(demand)) {
        if (demandPoint.equals(containerPoint) && !isManifested(container, containerPoint)) {
          return true;
        } else
          return false;
      } else {
        if (demandPoint.getNode().equals(containerPoint.getNode())
            && demandPoint.getTime() <= containerPoint.getTime()
            && !isManifested(container, containerPoint)) {
          return true;
        } else
          return false;
      }
    } else
      return false;
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
    if (container == null || edge == null)
      return false;
    for (I_Carrier carrier : manifestedContainers.get(edge).keySet()) {
      if (manifestedContainers.get(edge).get(carrier).contains(container))
        return true;
    }
    return false;
  }

  /**
   * Gets whether a resource container can be manifested onto a supply edge on a carrier.
   * 
   * @param container the resource container
   * @param edge the supply edge
   * @param carrier the carrier
   * 
   * @return whether the container can be manifested on the supply edge in the carrier
   */
  public boolean canManifestContainer(I_ResourceContainer container, SupplyEdge edge,
      I_Carrier carrier) {
    if (container == null || edge == null || carrier == null)
      return false;

    SupplyPoint point = getCurrentSupplyPoint(container);

    if (isEdgeDemand(container)) {
      if (point != null && edge.equals(point.getEdge())
          && manifestedContainers.get(edge).keySet().contains(carrier)
          && getCargoMass(carrier, point) + container.getMass() + getCargoMass(container, point)
              - carrier.getMaxCargoMass() < GlobalParameters.getSingleton().getMassPrecision() / 2d
          && (!GlobalParameters.getSingleton().isVolumeConstrained()
              || getCargoVolume(carrier, point) + container.getVolume()
                  - carrier.getMaxCargoVolume() < GlobalParameters.getSingleton()
                      .getVolumePrecision() / 2d)
          && (!GlobalParameters.getSingleton().isEnvironmentConstrained()
              || !(container.getEnvironment() == Environment.PRESSURIZED
                  && carrier.getCargoEnvironment() == Environment.UNPRESSURIZED))
          && !isManifested(container, point)) {
        return true;
      } else
        return false;
    } else if (point != null && edge.getDestination().equals(point.getNode())
        && edge.getEndTime() <= point.getTime()
        && manifestedContainers.get(edge).keySet().contains(carrier)
        && getCargoMass(carrier, point) + container.getMass() + getCargoMass(container, point)
            - carrier.getMaxCargoMass() < GlobalParameters.getSingleton().getMassPrecision() / 2d
        && (!GlobalParameters.getSingleton().isVolumeConstrained()
            || getCargoVolume(carrier, point) + container.getVolume() - carrier
                .getMaxCargoVolume() < GlobalParameters.getSingleton().getVolumePrecision() / 2d)
        && (!GlobalParameters.getSingleton().isEnvironmentConstrained()
            || !(container.getEnvironment() == Environment.PRESSURIZED
                && carrier.getCargoEnvironment() == Environment.UNPRESSURIZED))
        && !isManifested(container, point)) {
      return true;
    } else
      return false;
  }

  /**
   * Manifests a resource container onto a supply edge in a carrier.
   * 
   * @param container the resource container
   * @param edge the supply edge
   * @param carrier the carrier
   */
  public void manifestContainer(I_ResourceContainer container, SupplyEdge edge, I_Carrier carrier) {
    if (canManifestContainer(container, edge, carrier)) {
      manifestedContainers.get(edge).get(carrier).add(container);
    }
  }

  /**
   * Unmanifests a resource container from a supply edge.
   * 
   * @param container the resource container
   * @param edge the supple edge
   */
  public void unmanifestContainer(I_ResourceContainer container, SupplyEdge edge) {
    if (canUnmanifestContainer(container, edge)) {
      for (I_Carrier carrier : manifestedContainers.get(edge).keySet()) {
        manifestedContainers.get(edge).get(carrier).remove(container);
      }
      for (SupplyEdge e : supplyEdges) {
        // clean up earlier manifests... not guaranteed anymore
        if (e.getEndTime() < edge.getEndTime()) {
          for (I_Carrier carrier : manifestedContainers.get(e).keySet()) {
            manifestedContainers.get(e).get(carrier).remove(container);
          }
        }
      }
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
    for (Demand demandAsPacked : packedDemands.get(container)) {
      for (DemandSet demands : aggregatedNodeDemands.values()) {
        for (Demand demand : demands) {
          demandsAsPacked.get(demand).remove(demandAsPacked);
        }
      }
      for (DemandSet demands : aggregatedEdgeDemands.values()) {
        for (Demand demand : demands) {
          demandsAsPacked.get(demand).remove(demandAsPacked);
        }
      }
    }
    packedDemands.remove(container);
    for (SupplyEdge edge : supplyEdges) {
      for (I_Carrier carrier : edge.getAllCarriers()) {
        manifestedContainers.get(edge).get(carrier).remove(container);
      }
    }
  }

  /**
   * Imports demands from a demand simulator.
   * 
   * @param supplyEdges the set of supply edges
   */
  public void importDemands(DemandSimulator simulator) {
    reset();
    supplyPoints.addAll(simulator.getSupplyPoints());
    supplyEdges.addAll(simulator.getSupplyEdges());
    for (SupplyEdge edge : supplyEdges) {
      manifestedContainers.put(edge, new TreeMap<I_Carrier, Set<I_ResourceContainer>>());
      for (I_Carrier carrier : edge.getAllCarriers()) {
        manifestedContainers.get(edge).put(carrier, new HashSet<I_ResourceContainer>());
      }
    }
    aggregatedNodeDemands.putAll(simulator.getAggregatedNodeDemands());
    for (DemandSet demands : aggregatedNodeDemands.values()) {
      for (Demand demand : demands) {
        demandsAsPacked.put(demand, new HashSet<Demand>());
      }
    }
    aggregatedEdgeDemands.putAll(simulator.getAggregatedEdgeDemands());
    for (DemandSet demands : aggregatedEdgeDemands.values()) {
      for (Demand demand : demands) {
        demandsAsPacked.put(demand, new HashSet<Demand>());
      }
    }
  }

  /**
   * Gets the set of supply edges.
   * 
   * @return the set of supply edges
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
   * Gets the set of demands aggregated to a supply point.
   * 
   * @param point the supply point
   * 
   * @return the set of aggregated demands
   */
  public DemandSet getAggregatedNodeDemands(SupplyPoint point) {
    return aggregatedNodeDemands.get(point);
  }

  /**
   * Gets the set of demands aggregated to a supply edge.
   * 
   * @param edge the supply edge
   * 
   * @return the set of aggregated demands
   */
  public DemandSet getAggregatedEdgeDemands(SupplyEdge edge) {
    return aggregatedEdgeDemands.get(edge);
  }

  /**
   * Gets the set of packed demands from aggregated demands.
   * 
   * @return the set of packed demands
   */
  public Map<Demand, Set<Demand>> getDemandsAsPacked() {
    return Collections.unmodifiableMap(demandsAsPacked);
  }

  /**
   * Gets the set of packed demands in resource containers.
   * 
   * @return the set of packed demands
   */
  public Map<I_ResourceContainer, Set<Demand>> getPackedDemands() {
    return Collections.unmodifiableMap(packedDemands);
  }

  /**
   * Gets the set of packed demands in a container at a point in time referenced by a supply point.
   * 
   * @param container the resource container
   * @param point the supply point
   * 
   * @return the set of packed demands
   */
  public Set<Demand> getPackedDemands(I_ResourceContainer container, SupplyPoint point) {
    HashSet<Demand> demands = new HashSet<Demand>();
    if (packedDemands.get(container) != null) {
      for (Demand demand : packedDemands.get(container)) {
        if (getSupplyPoint(getDemand(demand)).getTime() >= point.getTime())
          demands.add(demand);
      }
    }
    return Collections.unmodifiableSet(demands);
  }

  /**
   * Gets the set of resource containers manifested to a supply edge on a carrier.
   * 
   * @param edge the supply edge
   * @param carrier the carrier
   * 
   * @return the set of resource containers
   */
  public Set<I_ResourceContainer> getManifestedContainers(SupplyEdge edge, I_Carrier carrier) {
    return Collections.unmodifiableSet(manifestedContainers.get(edge).get(carrier));
  }

  /**
   * Gets the set of resource containers manifested to a carrier at a point in time referenced by a
   * supply point.
   * 
   * @param carrier the carrier
   * @param point the supply point
   * 
   * @return the set of resource containers
   */
  public Set<I_ResourceContainer> getManifestedContainers(I_Carrier carrier, SupplyPoint point) {
    HashSet<I_ResourceContainer> containers = new HashSet<I_ResourceContainer>();
    for (SupplyEdge edge : supplyEdges) {
      if (manifestedContainers.get(edge).keySet().contains(carrier)
          && edge.getEndTime() <= point.getTime()) {
        for (I_ResourceContainer container : manifestedContainers.get(edge).get(carrier)) {
          containers.add(container);
        }
      }
    }
    return Collections.unmodifiableSet(containers);
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
    manifestedContainers.clear();
  }

  /**
   * Gets the set of all resource containers.
   * 
   * @return the set of resource containers
   */
  public Set<I_ResourceContainer> getContainers() {
    return Collections.unmodifiableSet(packedDemands.keySet());
  }

  /**
   * Gets the set of demands packed in a container.
   * 
   * @param container the container
   * 
   * @return the set of demands
   */
  public Set<Demand> getPackedDemands(I_ResourceContainer container) {
    return Collections.unmodifiableSet(packedDemands.get(container));
  }

  /**
   * Generates a set of manifest events used at simulation time.
   * 
   * @return the set of manifest events
   */
  public Set<ManifestEvent> generateManifestEvents() {
    HashSet<ManifestEvent> events = new HashSet<ManifestEvent>();
    for (I_ResourceContainer container : packedDemands.keySet()) {
      container.getContents().clear();
      for (Demand demand : packedDemands.get(container)) {
        if (!container.add(demand.getResource(), demand.getAmount())) {
          System.out.println(demand + " could not be added to " + container);
          // throw new RuntimeException("ERROR!");
        }
      }
    }
    for (SupplyEdge edge : supplyEdges) {
      for (I_Carrier carrier : edge.getAllCarriers()) {
        if (manifestedContainers.get(edge).get(carrier).size() > 0) {
          ManifestEvent event = new ManifestEvent();
          event.setName("Manifest Event");
          event.setPriority(0);
          event.setTime(edge.getStartTime());
          event.setLocation(edge.getOrigin());
          for (I_Element element : getScenario().getElements()) {
            if (element.equals(carrier)) {
              event.setCarrier((I_Carrier) element);
              break;
            }
          }
          for (I_ResourceContainer container : manifestedContainers.get(edge).get(carrier)) {
            event.getContainers().add(container);
          }
          events.add(event);
        }
      }
    }
    return Collections.unmodifiableSet(events);
  }

  /**
   * Runs an auto-manifesting algorithm.
   */
  public void autoManifest() {
    for (SupplyPoint point : supplyPoints) {
      // pack edge demands first
      for (Demand demand : aggregatedEdgeDemands.get(point.getEdge())) {
        autoPackDemand(demand);
      }
      // then pack node demands
      for (Demand demand : aggregatedNodeDemands.get(point)) {
        autoPackDemand(demand);
      }
      // manifest edge demands first
      for (I_ResourceContainer container : packedDemands.keySet()) {
        if (point.equals(getCurrentSupplyPoint(container)) && isEdgeDemand(container)) {
          boolean isManifested = false;
          for (SupplyEdge edge : supplyEdges) {
            for (I_Carrier carrier : edge.getCarriers()) {
              isManifested = canManifestContainer(container, edge, carrier);
              manifestContainer(container, edge, carrier);
              if (isManifested)
                break;
            }
            if (isManifested)
              break;
          }
        }
      }
      // then manifest node demands
      for (I_ResourceContainer container : packedDemands.keySet()) {
        if (point.equals(getCurrentSupplyPoint(container)) && !isEdgeDemand(container)) {
          boolean isManifested = false;
          for (SupplyEdge edge : supplyEdges) {
            for (I_Carrier carrier : edge.getCarriers()) {
              if (canManifestContainer(container, edge, carrier)) {
                isManifested = canManifestContainer(container, edge, carrier);
                manifestContainer(container, edge, carrier);
                if (isManifested)
                  break;
              }
            }
            if (isManifested)
              break;
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
