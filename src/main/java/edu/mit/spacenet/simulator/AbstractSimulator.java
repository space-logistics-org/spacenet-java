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
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.scenario.ItemDiscretization;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.RepairItem;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.DemandEvent;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.ManifestEvent;
import edu.mit.spacenet.simulator.event.MissionEvent;
import edu.mit.spacenet.simulator.moe.MoeCrewTime;
import edu.mit.spacenet.util.DateFunctions;
import edu.mit.spacenet.util.SerializeUtil;

/**
 * An abstract implementation of the simulator interface.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractSimulator implements I_Simulator {
  private Scenario scenario;
  private Scenario clone;
  private PriorityQueue<I_Event> events;
  private double time;
  private List<SimSpatialError> spatialErrors;
  private List<SimDemand> unsatisfiedDemands;
  private List<SimWarning> warnings;
  private List<SimScavenge> scavengedParts;
  private List<SimRepair> repairedParts;
  private Map<I_Element, Map<Item, Double>> itemDemandsByElement;
  private Map<Item, Double> itemDemandsByScenario;
  private Map<Location, Map<Item, Double>> itemDemandsByLocation;
  private boolean packingDemandsAdded, demandsSatisfied;
  private boolean itemsRepaired;

  protected double duration;
  protected I_Event event;

  /**
   * The constructor sets the scenario and initializes the data structures.
   * 
   * @param scenario the scenario
   */
  public AbstractSimulator(Scenario scenario) {
    this.scenario = scenario;
    events = new PriorityQueue<I_Event>();
    spatialErrors = new ArrayList<SimSpatialError>();
    unsatisfiedDemands = new ArrayList<SimDemand>();
    warnings = new ArrayList<SimWarning>();
    scavengedParts = new ArrayList<SimScavenge>();
    repairedParts = new ArrayList<SimRepair>();
    itemDemandsByElement = new HashMap<I_Element, Map<Item, Double>>();
    itemDemandsByLocation = new HashMap<Location, Map<Item, Double>>();
    itemDemandsByScenario = new HashMap<Item, Double>();
    packingDemandsAdded = false;
    demandsSatisfied = true;
    itemsRepaired = false;
  }

  protected PriorityQueue<I_Event> getEvents() {
    return events;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#schedule(edu.mit.spacenet.domain.event.I_Event)
   */
  public void schedule(I_Event event) {
    events.add(event);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getScenario()
   */
  public Scenario getScenario() {
    return clone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getTime()
   */
  public double getTime() {
    return time;
  }

  /**
   * Sets the time.
   * 
   * @param time the new time
   */
  public void setTime(double time) {
    this.time = time;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getWarnings()
   */
  public List<SimWarning> getWarnings() {
    return warnings;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getErrors()
   */
  public List<SimError> getErrors() {
    ArrayList<SimError> errors = new ArrayList<SimError>();
    errors.addAll(spatialErrors);
    errors.addAll(unsatisfiedDemands);
    Collections.sort(errors);
    return errors;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getSpatialErrors()
   */
  public List<SimSpatialError> getSpatialErrors() {
    return spatialErrors;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getUnsatisfiedDemands()
   */
  public List<SimDemand> getUnsatisfiedDemands() {
    return unsatisfiedDemands;
  }

  protected void initializeSimulation() {
    events.clear();
    spatialErrors.clear();
    unsatisfiedDemands.clear();
    warnings.clear();
    scavengedParts.clear();
    repairedParts.clear();
    time = 0;

    // serialize scenario for deep cloning
    clone = (Scenario) SerializeUtil.deepClone(scenario);
    for (Mission m : clone.getMissionList()) {
      MissionEvent p = new MissionEvent();
      p.setMission(m);
      schedule(p);
    }

    if (clone.getItemDiscretization() == ItemDiscretization.BY_ELEMENT) {
      for (I_Element element : clone.getElements()) {
        itemDemandsByElement.put(element, new HashMap<Item, Double>());
      }
    } else if (clone.getItemDiscretization() == ItemDiscretization.BY_LOCATION) {
      for (Location location : clone.getNetwork().getLocations()) {
        itemDemandsByLocation.put(location, new HashMap<Item, Double>());
      }
    }
  }

  protected void scheduleManifestEvents() {
    for (ManifestEvent event : getScenario().getManifest().generateManifestEvents()) {
      schedule(event);
    }
  }

  protected void getNextEvent() {
    event = getEvents().poll();
    duration = event.getTime() - getTime();
    time = event.getTime();
  }

  protected void handleDemands() {
    if (duration > 0) {
      for (I_Element element : getScenario().getNetwork().getRegistrar().values()) {
        DemandEvent d = new DemandEvent();
        d.setTime(time);
        d.setName("Demand Event for " + element);
        d.setElement(element);
        d.setLocation(element.getLocation());
        d.setDemands(repairDemands(discretizeDemands(element.generateDemands(duration, this),
            element, element.getLocation()), element));
        if (d.getDemands().size() > 0) {
          try {
            d.execute(this);
          } catch (SimError error) {
            if (error instanceof SimSpatialError) {
              spatialErrors.add((SimSpatialError) error);
            } else if (error instanceof SimDemand) {
              /*
               * TODO double-counting? see DemandEvent.execute if(isPackingDemandsAdded()) { Demand
               * packingDemand = new Demand(new GenericResource(ClassOfSupply.COS5), 0); for(Demand
               * demand : ((SimDemand)error).getDemands()) { if(demand.getAmount()>0 &&
               * demand.getResource().getPackingFactor()>0)
               * packingDemand.setAmount(packingDemand.getAmount() +
               * demand.getAmount()*demand.getResource().getPackingFactor()); }
               * ((SimDemand)error).getDemands().add(packingDemand); }
               */
              unsatisfiedDemands.add((SimDemand) error);
            }
          }
        }
      }
    }
    if (event instanceof MissionEvent) {
      Mission mission = ((MissionEvent) event).getMission();

      DemandEvent d = new DemandEvent();
      d.setTime(time + mission.getTransitDuration());
      d.setPriority(Integer.MAX_VALUE);
      d.setName("Demand Event for " + mission);
      d.setElement(null);
      d.setLocation(mission.getDestination());
      d.setDemands(
          discretizeDemands(mission.generateDemands(this), null, mission.getDestination()));
      schedule(d);
    }
  }

  private DemandSet discretizeDemands(DemandSet demands, I_Element element, Location location) {
    if (getScenario().getItemDiscretization() == ItemDiscretization.BY_ELEMENT && element != null) {
      for (Demand demand : demands) {
        if (demand.getResource() instanceof Item) {
          Item item = (Item) demand.getResource();
          if (!itemDemandsByElement.get(element).containsKey(item)) {
            itemDemandsByElement.get(element).put(item, new Double(0));
          }
          itemDemandsByElement.get(element).put(item,
              itemDemandsByElement.get(element).get(item) + demand.getAmount());
          demand.setAmount(0);
          while (itemDemandsByElement.get(element).get(item) >= getScenario()
              .getItemAggregation()) {
            demand.setAmount(demand.getAmount() + 1);
            itemDemandsByElement.get(element).put(item,
                itemDemandsByElement.get(element).get(item) - 1);
          }
        }
      }
    } else if (getScenario().getItemDiscretization() == ItemDiscretization.BY_LOCATION) {
      for (Demand demand : demands) {
        if (demand.getResource() instanceof Item) {
          Item item = (Item) demand.getResource();
          if (!itemDemandsByLocation.get(location).containsKey(item)) {
            itemDemandsByLocation.get(location).put(item, new Double(0));
          }
          itemDemandsByLocation.get(location).put(item,
              itemDemandsByLocation.get(location).get(item) + demand.getAmount());
          demand.setAmount(0);
          while (itemDemandsByLocation.get(location).get(item) >= getScenario()
              .getItemAggregation()) {
            demand.setAmount(demand.getAmount() + 1);
            itemDemandsByLocation.get(location).put(item,
                itemDemandsByLocation.get(location).get(item) - 1);
          }
        }
      }
    } else if (getScenario().getItemDiscretization() == ItemDiscretization.BY_SCENARIO) {
      for (Demand demand : demands) {
        if (demand.getResource() instanceof Item) {
          Item item = (Item) demand.getResource();
          if (!itemDemandsByScenario.containsKey(item)) {
            itemDemandsByScenario.put(item, new Double(0));
          }
          itemDemandsByScenario.put(item, itemDemandsByScenario.get(item) + demand.getAmount());
          demand.setAmount(0);
          while (itemDemandsByScenario.get(item) >= getScenario().getItemAggregation()) {
            demand.setAmount(demand.getAmount() + 1);
            itemDemandsByScenario.put(item, itemDemandsByScenario.get(item) - 1);
          }
        }
      }
    }
    demands.clean();
    return demands;
  }

  private DemandSet repairDemands(DemandSet demands, I_Element element) {
    if (itemsRepaired) {
      double lastTime = 0;
      for (Mission mission : getScenario().getMissionList()) {
        if (getScenario().getRepairedItems().get(mission) != null
            && getTime() < mission.getDuration()
                + DateFunctions.getDaysBetween(mission.getStartDate(), getScenario().getStartDate())
            && getTime() >= lastTime) {
          if (mission.isCrewed())
            lastTime = mission.getDuration() + DateFunctions.getDaysBetween(mission.getStartDate(),
                getScenario().getStartDate());

          for (RepairItem item : getScenario().getRepairedItems().get(mission)) {
            if (item.getElement().equals(element)) {
              for (Demand demand : demands) {
                if (item.getDemand().getResource().equals(demand.getResource())
                    && item.getDemand().getResource() instanceof I_Item
                    && item.getDemand().getAmount() > 0) {
                  double amountRepaired =
                      Math.min(demand.getAmount(), item.getDemand().getAmount());
                  if (this instanceof FullSimulator) {
                    ((FullSimulator) this).getCrewTimeHistory()
                        .add(new MoeCrewTime(
                            DateFunctions.getDaysBetween(mission.getStartDate(),
                                getScenario().getStartDate()),
                            element.getLocation(), MoeCrewTime.CORRECTIVE_MAINTENANCE,
                            item.getUnitMeanRepairTime() * amountRepaired));
                    ((FullSimulator) this).getCrewTimeHistory().add(new MoeCrewTime(
                        DateFunctions.getDaysBetween(mission.getStartDate(),
                            getScenario().getStartDate()),
                        element.getLocation(), MoeCrewTime.EXPLORATION,
                        -item.getUnitMeanRepairTime() * amountRepaired));
                  }
                  getRepairedParts().add(new SimRepair(getTime(), (I_Item) demand.getResource(),
                      amountRepaired, amountRepaired * item.getUnitMeanRepairTime(),
                      amountRepaired * item.getUnitMassToRepair(), element.getLocation(), element));
                  demand.setAmount(demand.getAmount() - amountRepaired);
                  demands.add(new Demand(new GenericResource(ClassOfSupply.COS4),
                      item.getUnitMassToRepair() * amountRepaired));
                  item.getDemand().setAmount(item.getDemand().getAmount() - amountRepaired);
                }
              }
            }
          }
        }
      }
    }
    demands.clean();
    return demands;
  }

  protected void executeEvent() {
    try {
      event.execute(this);
    } catch (SimError error) {
      if (error instanceof SimSpatialError) {
        spatialErrors.add((SimSpatialError) error);
      } else if (error instanceof SimDemand) {
        unsatisfiedDemands.add((SimDemand) error);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemandHistory(edu.mit.spacenet.domain.element.
   * I_Element)
   */
  public List<SimDemand> getDemandHistory(I_Element element) {
    ArrayList<SimDemand> demands = new ArrayList<SimDemand>();
    for (SimDemand d : getUnsatisfiedDemands()) {
      if (element.equals(d.getElement())) {
        demands.add(d);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemandHistory(edu.mit.spacenet.scenario.Mission)
   */
  public List<SimDemand> getDemandHistory(Mission mission) {
    ArrayList<SimDemand> demands = new ArrayList<SimDemand>();
    double startTime =
        DateFunctions.getDaysBetween(mission.getStartDate(), getScenario().getStartDate());
    double endTime = startTime + mission.getDuration();
    for (SimDemand s : getUnsatisfiedDemands()) {
      if (s.getTime() > startTime && s.getTime() <= endTime) {
        demands.add(s);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemandHistory(edu.mit.spacenet.domain.network.
   * Location)
   */
  public List<SimDemand> getDemandHistory(Location location) {
    ArrayList<SimDemand> demands = new ArrayList<SimDemand>();
    for (SimDemand d : getUnsatisfiedDemands()) {
      if (location.equals(d.getLocation())) {
        demands.add(d);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.scenario.Mission)
   */
  public DemandSet getDemands(Mission mission) {
    DemandSet demands = new DemandSet();
    for (SimDemand s : getDemandHistory(mission)) {
      demands.addAll(s.getDemands());
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.scenario.Mission,
   * edu.mit.spacenet.domain.ClassOfSupply)
   */
  public DemandSet getDemands(Mission mission, ClassOfSupply cos) {
    DemandSet demands = new DemandSet();
    for (Demand demand : getDemands(mission)) {
      if (demand.getResource().getClassOfSupply().equals(cos)
          || demand.getResource().getClassOfSupply().isSubclassOf(cos)) {
        demands.add(demand);
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.scenario.Mission,
   * edu.mit.spacenet.domain.element.I_Element)
   */
  public DemandSet getDemands(Mission mission, I_Element element) {
    DemandSet demands = new DemandSet();
    for (SimDemand s : getDemandHistory(mission)) {
      if ((element == null && s.getElement() == null)
          || (s.getElement() != null && s.getElement().equals(element)))
        demands.addAll(s.getDemands());
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.scenario.Mission,
   * edu.mit.spacenet.domain.element.I_Element, edu.mit.spacenet.domain.ClassOfSupply)
   */
  public DemandSet getDemands(Mission mission, I_Element element, ClassOfSupply cos) {
    DemandSet demands = new DemandSet();
    for (Demand demand : getDemands(mission, element)) {
      if (demand.getResource().getClassOfSupply().equals(cos)
          || demand.getResource().getClassOfSupply().isSubclassOf(cos))
        demands.add(demand);
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.domain.element.I_Element,
   * edu.mit.spacenet.domain.ClassOfSupply)
   */
  public DemandSet getDemands(I_Element element, ClassOfSupply cos) {
    DemandSet demands = new DemandSet();
    for (SimDemand d : getUnsatisfiedDemands()) {
      for (Demand demand : d.getDemands()) {
        if (element.equals(d.getElement()) && (demand.getResource().getClassOfSupply().equals(cos)
            || demand.getResource().getClassOfSupply().isSubclassOf(cos))) {
          demands.add(demand);
        }
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.simulator.I_Simulator#getDemands(edu.mit.spacenet.domain.network.Location,
   * edu.mit.spacenet.domain.ClassOfSupply)
   */
  public DemandSet getDemands(Location location, ClassOfSupply cos) {
    DemandSet demands = new DemandSet();
    for (SimDemand d : getUnsatisfiedDemands()) {
      for (Demand demand : d.getDemands()) {
        if (location.equals(d.getLocation()) && (demand.getResource().getClassOfSupply().equals(cos)
            || demand.getResource().getClassOfSupply().isSubclassOf(cos))) {
          demands.add(demand);
        }
      }
    }
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#isPackingDemandsAdded()
   */
  public boolean isPackingDemandsAdded() {
    return packingDemandsAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#setPackingDemandsAdded(boolean)
   */
  public void setPackingDemandsAdded(boolean packingDemandsAdded) {
    this.packingDemandsAdded = packingDemandsAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#isResourcesConsumed()
   */
  public boolean isDemandsSatisfied() {
    return demandsSatisfied;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#setResourcesConsumed(boolean)
   */
  public void setDemandsSatisfied(boolean demandsSatisfied) {
    this.demandsSatisfied = demandsSatisfied;
  }

  /**
   * Checks if is items repaired.
   * 
   * @return true, if is items repaired
   */
  public boolean isItemsRepaired() {
    return itemsRepaired;
  }

  /**
   * Sets the items repaired.
   * 
   * @param itemsRepaired the new items repaired
   */
  public void setItemsRepaired(boolean itemsRepaired) {
    this.itemsRepaired = itemsRepaired;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getScavengedParts()
   */
  public List<SimScavenge> getScavengedParts() {
    return scavengedParts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.simulator.I_Simulator#getRepairedParts()
   */
  public List<SimRepair> getRepairedParts() {
    return repairedParts;
  }
}
