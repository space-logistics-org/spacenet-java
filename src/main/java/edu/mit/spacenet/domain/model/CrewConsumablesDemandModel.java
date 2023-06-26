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
package edu.mit.spacenet.domain.model;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * A mission-level demand model that estimates the resources needed to sustain a crew during a
 * mission.
 */
public class CrewConsumablesDemandModel extends AbstractDemandModel {
  private Mission mission;

  private double reservesDuration, waterRecoveryRate, clothingLifetime;
  private boolean transitDemandsOmitted;
  private double waterRate, evaWaterRate, foodSupportRate, ambientFoodRate, rfFoodRate, oxygenRate,
      evaOxygenRate, nitrogenRate, hygieneRate, hygieneKit, clothingRate, personalItems;
  private double officeEquipment, evaSuit, evaLithiumHydroxide, healthEquipment, healthConsumables,
      safetyEquipment, commEquipment, computerEquipment;
  private double trashBagRate, wasteContainmentRate;

  /**
   * Instantiates a new crew consumables demand model.
   * 
   * @param mission the mission for which to generate demands
   */
  public CrewConsumablesDemandModel(Mission mission) {
    super();
    this.mission = mission;
    setReservesDuration(0);
    setWaterRecoveryRate(0.42);
    setClothingLifetime(4);
    resetDefaultRates();
  }

  public CrewConsumablesDemandModel() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Reset all rates to default values (based on SpaceNet 1.3).
   */
  public void resetDefaultRates() {
    setWaterRate(3.6);
    setEvaWaterRate(0.6875);
    setFoodSupportRate(0.05556);
    setAmbientFoodRate(0.76389);
    setRfFoodRate(1.61667);
    setOxygenRate(3.85714);
    setEvaOxygenRate(0.07875);
    setNitrogenRate(2.21429);
    setHygieneRate(0.27778);
    setHygieneKit(1.8);
    setClothingRate(2.3);
    setPersonalItems(10);

    setOfficeEquipment(5);
    setEvaSuit(107);
    setEvaLithiumHydroxide(0.3625);
    setHealthEquipment(20);
    setHealthConsumables(0.1);
    setSafetyEquipment(25);
    setCommEquipment(20);
    setComputerEquipment(5);

    setTrashBagRate(0.05);
    setWasteContainmentRate(0.05);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    DemandSet demands = new DemandSet();

    demands.add(new Demand(new GenericResource(ClassOfSupply.COS201),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getWaterRate() * getMissionCrewSize() * (1 - getWaterRecoveryRate())));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS201),
        getMissionEvaCrewTime() * getEvaWaterRate()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS202),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getFoodSupportRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS202),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getAmbientFoodRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS202),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getRfFoodRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS203),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getOxygenRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS203),
        getMissionEvaCrewTime() * getEvaOxygenRate()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS203),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getNitrogenRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS204),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getHygieneRate() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS204),
        getHygieneKit() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS205),
        (getMissionExplorationDuration() + getMissionTransitDuration()) * getClothingRate()
            * getMissionCrewSize() / getClothingLifetime()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS206),
        getPersonalItems() * getMissionCrewSize()));

    demands.add(new Demand(new GenericResource(ClassOfSupply.COS301),
        getOfficeEquipment() * getMissionCrewSize()));
    demands.add(
        new Demand(new GenericResource(ClassOfSupply.COS302), getEvaSuit() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS302),
        getEvaLithiumHydroxide() * getMissionEvaCrewTime()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS303), getHealthEquipment()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS303),
        getHealthConsumables() * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS304), getSafetyEquipment()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS305), getCommEquipment()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS306),
        getComputerEquipment() * getMissionCrewSize()));

    demands.add(new Demand(new GenericResource(ClassOfSupply.COS701),
        (getMissionExplorationDuration() + getMissionTransitDuration()) * getTrashBagRate()
            * getMissionCrewSize()));
    demands.add(new Demand(new GenericResource(ClassOfSupply.COS702),
        (getMissionExplorationDuration() + getMissionTransitDuration() + getReservesDuration())
            * getWasteContainmentRate() * getMissionCrewSize()));
    return demands;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
   */
  public DemandModelType getDemandModelType() {
    return DemandModelType.CREW_CONSUMABLES;
  }

  /**
   * Gets the mission.
   * 
   * @return the mission
   */
  public Mission getMission() {
    return mission;
  }

  /**
   * Gets the mission crew size.
   * 
   * @return the crew size
   */
  public int getMissionCrewSize() {
    return mission.getCrewSize();
  }

  /**
   * Gets the mission EVA crew time.
   * 
   * @return the mission EVA crew time (crew-hour)
   */
  public double getMissionEvaCrewTime() {
    return mission.getEvaCrewTime();
  }

  /**
   * Gets the mission exploration duration.
   * 
   * @return the exploration duration (days)
   */
  public double getMissionExplorationDuration() {
    return mission.getDestinationDuration();
  }

  /**
   * Gets the mission transit duration.
   * 
   * @return the mission transit duration (days)
   */
  public double getMissionTransitDuration() {
    if (isTransitDemandsOmitted()) {
      return 0;
    } else {
      return mission.getTransitDuration() + mission.getReturnTransitDuration();
    }
  }

  /**
   * Gets the reserves duration.
   * 
   * @return the reserves duration (days)
   */
  public double getReservesDuration() {
    return reservesDuration;
  }

  /**
   * Sets the reserves duration.
   * 
   * @param reservesDuration the new reserves duration (days)
   */
  public void setReservesDuration(double reservesDuration) {
    this.reservesDuration = reservesDuration;
  }

  /**
   * Gets the water recovery rate.
   * 
   * @return the water recovery rate ([0:1])
   */
  public double getWaterRecoveryRate() {
    return waterRecoveryRate;
  }

  /**
   * Sets the water recovery rate.
   * 
   * @param waterRecoveryRate the new water recovery rate ([0:1])
   */
  public void setWaterRecoveryRate(double waterRecoveryRate) {
    this.waterRecoveryRate = Math.max(0, Math.min(waterRecoveryRate, 1));
  }

  /**
   * Gets the EVA water rate.
   * 
   * @return the EVA water rate (kilograms/crew/hour)
   */
  public double getEvaWaterRate() {
    return evaWaterRate;
  }

  /**
   * Sets the EVA water rate.
   * 
   * @param evaWaterRate the new EVA water rate (kilograms/crew/hour)
   */
  public void setEvaWaterRate(double evaWaterRate) {
    this.evaWaterRate = evaWaterRate;
  }

  /**
   * Gets the clothing lifetime.
   * 
   * @return the clothing lifetime (days)
   */
  public double getClothingLifetime() {
    return clothingLifetime;
  }

  /**
   * Sets the clothing lifetime (minimum 1 day).
   * 
   * @param clothingLifetime the new clothing lifetime (days)
   */
  public void setClothingLifetime(double clothingLifetime) {
    this.clothingLifetime = Math.max(1, clothingLifetime);
  }

  /**
   * Gets the water rate.
   * 
   * @return the water rate (kilograms/crew/day)
   */
  public double getWaterRate() {
    return waterRate;
  }

  /**
   * Sets the water rate.
   * 
   * @param waterRate the new water rate (kilograms/crew/day)
   */
  public void setWaterRate(double waterRate) {
    this.waterRate = waterRate;
  }

  /**
   * Gets the food support rate.
   * 
   * @return the food support rate (kilograms/crew/day)
   */
  public double getFoodSupportRate() {
    return foodSupportRate;
  }

  /**
   * Sets the food support rate.
   * 
   * @param foodSupportRate the new food support rate (kilograms/crew/day)
   */
  public void setFoodSupportRate(double foodSupportRate) {
    this.foodSupportRate = foodSupportRate;
  }

  /**
   * Gets the ambient food rate.
   * 
   * @return the ambient food rate (kilograms/crew/day)
   */
  public double getAmbientFoodRate() {
    return ambientFoodRate;
  }

  /**
   * Sets the ambient food rate.
   * 
   * @param ambientFoodRate the new ambient food rate (kilograms/crew/day)
   */
  public void setAmbientFoodRate(double ambientFoodRate) {
    this.ambientFoodRate = ambientFoodRate;
  }

  /**
   * Gets the R/F food rate.
   * 
   * @return the R/F food rate (kilograms/crew/day)
   */
  public double getRfFoodRate() {
    return rfFoodRate;
  }

  /**
   * Sets the R/F food rate.
   * 
   * @param rfFoodRate the new R/F food rate (kilograms/crew/day)
   */
  public void setRfFoodRate(double rfFoodRate) {
    this.rfFoodRate = rfFoodRate;
  }

  /**
   * Gets the oxygen rate.
   * 
   * @return the oxygen rate (kilograms/crew/day)
   */
  public double getOxygenRate() {
    return oxygenRate;
  }

  /**
   * Sets the oxygen rate.
   * 
   * @param oxygenRate the new oxygen rate (kilograms/crew/day)
   */
  public void setOxygenRate(double oxygenRate) {
    this.oxygenRate = oxygenRate;
  }

  /**
   * Gets the EVA oxygen rate.
   * 
   * @return the EVA oxygen rate (kilograms/crew/hour)
   */
  public double getEvaOxygenRate() {
    return evaOxygenRate;
  }

  /**
   * Sets the EVA oxygen rate.
   * 
   * @param evaOxygenRate the new EVA oxygen rate (kilograms/crew/hour)
   */
  public void setEvaOxygenRate(double evaOxygenRate) {
    this.evaOxygenRate = evaOxygenRate;
  }

  /**
   * Gets the nitrogen rate.
   * 
   * @return the nitrogen rate (kilograms/crew/day)
   */
  public double getNitrogenRate() {
    return nitrogenRate;
  }

  /**
   * Sets the nitrogen rate.
   * 
   * @param nitrogenRate the new nitrogen rate (kilograms/crew/day)
   */
  public void setNitrogenRate(double nitrogenRate) {
    this.nitrogenRate = nitrogenRate;
  }

  /**
   * Gets the hygiene rate.
   * 
   * @return the hygiene rate (kilograms/crew/day)
   */
  public double getHygieneRate() {
    return hygieneRate;
  }

  /**
   * Sets the hygiene rate.
   * 
   * @param hygieneRate the new hygiene rate (kilograms/crew/day)
   */
  public void setHygieneRate(double hygieneRate) {
    this.hygieneRate = hygieneRate;
  }

  /**
   * Gets the hygiene kit.
   * 
   * @return the hygiene kit (kilograms/crew)
   */
  public double getHygieneKit() {
    return hygieneKit;
  }

  /**
   * Sets the hygiene kit.
   * 
   * @param hygieneKit the new hygiene kit (kilograms/crew)
   */
  public void setHygieneKit(double hygieneKit) {
    this.hygieneKit = hygieneKit;
  }

  /**
   * Gets the clothing rate.
   * 
   * @return the clothing rate (kilograms/crew/change)
   */
  public double getClothingRate() {
    return clothingRate;
  }

  /**
   * Sets the clothing rate.
   * 
   * @param clothingRate the new clothing rate (kilograms/crew/change)
   */
  public void setClothingRate(double clothingRate) {
    this.clothingRate = clothingRate;
  }

  /**
   * Gets the personal items.
   * 
   * @return the personal items (kilograms/crew)
   */
  public double getPersonalItems() {
    return personalItems;
  }

  /**
   * Sets the personal items.
   * 
   * @param personalItems the new personal items (kilograms/crew)
   */
  public void setPersonalItems(double personalItems) {
    this.personalItems = personalItems;
  }

  /**
   * Gets the office equipment.
   * 
   * @return the office equipment (kilograms/crew)
   */
  public double getOfficeEquipment() {
    return officeEquipment;
  }

  /**
   * Sets the office equipment.
   * 
   * @param officeEquipment the new office equipment (kilograms/crew)
   */
  public void setOfficeEquipment(double officeEquipment) {
    this.officeEquipment = officeEquipment;
  }

  /**
   * Gets the eva suit.
   * 
   * @return the eva suit (kilograms/crew)
   */
  public double getEvaSuit() {
    return evaSuit;
  }

  /**
   * Sets the eva suit.
   * 
   * @param evaSuit the new eva suit (kilograms/crew)
   */
  public void setEvaSuit(double evaSuit) {
    this.evaSuit = evaSuit;
  }

  /**
   * Gets the EVA lithium hydroxide.
   * 
   * @return the EVA lithium hydroxide (kilograms/crew/hour)
   */
  public double getEvaLithiumHydroxide() {
    return evaLithiumHydroxide;
  }

  /**
   * Sets the EVA lithium hydroxide.
   * 
   * @param evaLithiumHydroxide the new EVA lithium hydroxide (kilograms/crew/hour)
   */
  public void setEvaLithiumHydroxide(double evaLithiumHydroxide) {
    this.evaLithiumHydroxide = evaLithiumHydroxide;
  }

  /**
   * Gets the health equipment.
   * 
   * @return the health equipment (kilograms)
   */
  public double getHealthEquipment() {
    return healthEquipment;
  }

  /**
   * Sets the health equipment.
   * 
   * @param healthEquipment the new health equipment (kilograms)
   */
  public void setHealthEquipment(double healthEquipment) {
    this.healthEquipment = healthEquipment;
  }

  /**
   * Gets the health consumables.
   * 
   * @return the health consumables (kilograms/crew/day)
   */
  public double getHealthConsumables() {
    return healthConsumables;
  }

  /**
   * Sets the health consumables.
   * 
   * @param healthConsumables the new health consumables (kilograms/crew/day)
   */
  public void setHealthConsumables(double healthConsumables) {
    this.healthConsumables = healthConsumables;
  }

  /**
   * Gets the safety equipment.
   * 
   * @return the safety equipment (kilograms)
   */
  public double getSafetyEquipment() {
    return safetyEquipment;
  }

  /**
   * Sets the safety equipment.
   * 
   * @param safetyEquipment the new safety equipment (kilograms)
   */
  public void setSafetyEquipment(double safetyEquipment) {
    this.safetyEquipment = safetyEquipment;
  }

  /**
   * Gets the communications equipment.
   * 
   * @return the communications equipment (kilograms)
   */
  public double getCommEquipment() {
    return commEquipment;
  }

  /**
   * Sets the communications equipment.
   * 
   * @param commEquipment the new communications equipment (kilograms)
   */
  public void setCommEquipment(double commEquipment) {
    this.commEquipment = commEquipment;
  }

  /**
   * Gets the computer equipment.
   * 
   * @return the computer equipment (kilograms)
   */
  public double getComputerEquipment() {
    return computerEquipment;
  }

  /**
   * Sets the computer equipment.
   * 
   * @param computerEquipment the new computer equipment (kilograms)
   */
  public void setComputerEquipment(double computerEquipment) {
    this.computerEquipment = computerEquipment;
  }

  /**
   * Gets the trashBag rate.
   * 
   * @return the trashBag rate (kilograms/crew/day)
   */
  public double getTrashBagRate() {
    return trashBagRate;
  }

  /**
   * Sets the trash bag rate.
   * 
   * @param trashBagRate the new trash bag rate (kilograms/crew/day)
   */
  public void setTrashBagRate(double trashBagRate) {
    this.trashBagRate = trashBagRate;
  }

  /**
   * Gets the waste containment rate.
   * 
   * @return the waste containment rate (kilograms/crew/day)
   */
  public double getWasteContainmentRate() {
    return wasteContainmentRate;
  }

  /**
   * Sets the waste containment rate.
   * 
   * @param wasteContainmentRate the new waste containment rate (kilograms/crew/day)
   */
  public void setWasteContainmentRate(double wasteContainmentRate) {
    this.wasteContainmentRate = wasteContainmentRate;
  }

  /**
   * Checks if is transit demands are omitted.
   * 
   * @return true, if is transit demands omitted
   */
  public boolean isTransitDemandsOmitted() {
    return transitDemandsOmitted;
  }

  /**
   * Sets if the transit demands are omitted.
   * 
   * @param transitDemandsOmitted whether the transit demands are omitted
   */
  public void setTransitDemandsOmitted(boolean transitDemandsOmitted) {
    this.transitDemandsOmitted = transitDemandsOmitted;
  }

  @Override
  public CrewConsumablesDemandModel clone() throws CloneNotSupportedException {
    CrewConsumablesDemandModel m = new CrewConsumablesDemandModel(getMission());
    m.setTid(getTid());
    m.setName(getName());
    m.setDescription(getDescription());
    m.setReservesDuration(getReservesDuration());
    m.setWaterRecoveryRate(getWaterRecoveryRate());
    m.setClothingLifetime(getClothingLifetime());
    m.setTransitDemandsOmitted(isTransitDemandsOmitted());
    m.setWaterRate(getWaterRate());
    m.setEvaWaterRate(getEvaWaterRate());
    m.setFoodSupportRate(getFoodSupportRate());
    m.setAmbientFoodRate(getAmbientFoodRate());
    m.setRfFoodRate(getRfFoodRate());
    m.setOxygenRate(getOxygenRate());
    m.setEvaOxygenRate(getEvaOxygenRate());
    m.setNitrogenRate(getNitrogenRate());
    m.setHygieneRate(getHygieneRate());
    m.setHygieneKit(getHygieneKit());
    m.setClothingRate(getClothingRate());
    m.setPersonalItems(getPersonalItems());
    m.setOfficeEquipment(getOfficeEquipment());
    m.setEvaSuit(getEvaSuit());
    m.setEvaLithiumHydroxide(getEvaLithiumHydroxide());
    m.setHealthEquipment(getHealthEquipment());
    m.setHealthConsumables(getHealthConsumables());
    m.setSafetyEquipment(getSafetyEquipment());
    m.setCommEquipment(getCommEquipment());
    m.setComputerEquipment(getComputerEquipment());
    m.setTrashBagRate(getTrashBagRate());
    m.setWasteContainmentRate(getWasteContainmentRate());
    return m;
  }
}
