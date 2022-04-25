package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

public class ConsumablesDemandModel extends DemandModel {	
	public UUID mission; // TODO remove
	public double reservesDuration;
	public double waterRecoveryRate;
	public double clothingLifetime;
	public boolean transitDemandsOmitted;
	public double waterRate;
	public double evaWaterRate;
	public double foodSupportRate;
	public double ambientFoodRate;
	public double rfFoodRate;
	public double oxygenRate;
	public double evaOxygenRate;
	public double nitrogenRate;
	public double hygieneRate;
	public double hygieneKit;
	public double clothingRate;
	public double personalItems;
	public double officeEquipment;
	public double evaSuit;
	public double evaLithiumHydroxide;
	public double healthEquipment;
	public double healthConsumables;
	public double safetyEquipment;
	public double commEquipment;
	public double computerEquipment;
	public double trashBagRate;
	public double wasteContainmentRate;

	public static ConsumablesDemandModel createFrom(edu.mit.spacenet.domain.model.CrewConsumablesDemandModel demandModel, Context context) {
		ConsumablesDemandModel m = new ConsumablesDemandModel();
		m.id = context.getUUID(demandModel);
		m.name = demandModel.getName();
		m.description = demandModel.getDescription();
		m.mission = context.getUUID(demandModel.getMission());
		m.reservesDuration = demandModel.getReservesDuration();
		m.waterRecoveryRate = demandModel.getWaterRecoveryRate();
		m.clothingLifetime = demandModel.getClothingLifetime();
		m.transitDemandsOmitted = demandModel.isTransitDemandsOmitted();
		m.waterRate = demandModel.getWaterRate();
		m.evaWaterRate = demandModel.getEvaWaterRate();
		m.foodSupportRate = demandModel.getFoodSupportRate();
		m.ambientFoodRate = demandModel.getAmbientFoodRate();
		m.rfFoodRate = demandModel.getRfFoodRate();
		m.oxygenRate = demandModel.getOxygenRate();
		m.evaOxygenRate = demandModel.getEvaOxygenRate();
		m.nitrogenRate = demandModel.getNitrogenRate();
		m.hygieneRate = demandModel.getHygieneRate();
		m.hygieneKit = demandModel.getHygieneKit();
		m.clothingRate = demandModel.getClothingRate();
		m.personalItems = demandModel.getPersonalItems();
		m.officeEquipment = demandModel.getOfficeEquipment();
		m.evaSuit = demandModel.getEvaSuit();
		m.evaLithiumHydroxide = demandModel.getEvaLithiumHydroxide();
		m.healthEquipment = demandModel.getHealthEquipment();
		m.healthConsumables = demandModel.getHealthConsumables();
		m.safetyEquipment = demandModel.getSafetyEquipment();
		m.commEquipment = demandModel.getCommEquipment();
		m.computerEquipment = demandModel.getComputerEquipment();
		m.trashBagRate = demandModel.getTrashBagRate();
		m.wasteContainmentRate = demandModel.getWasteContainmentRate();
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.CrewConsumablesDemandModel toSpaceNet(Context context) {
		edu.mit.spacenet.domain.model.CrewConsumablesDemandModel m = new edu.mit.spacenet.domain.model.CrewConsumablesDemandModel((edu.mit.spacenet.scenario.Mission) context.getObject(mission));
		m.setTid(context.getId(id, m));
		m.setName(name);
		m.setDescription(description);
		m.setReservesDuration(reservesDuration);
		m.setWaterRecoveryRate(waterRecoveryRate);
		m.setClothingLifetime(clothingLifetime);
		m.setTransitDemandsOmitted(transitDemandsOmitted);
		m.setWaterRate(waterRate);
		m.setEvaWaterRate(evaWaterRate);
		m.setFoodSupportRate(foodSupportRate);
		m.setAmbientFoodRate(ambientFoodRate);
		m.setRfFoodRate(rfFoodRate);
		m.setOxygenRate(oxygenRate);
		m.setEvaOxygenRate(evaOxygenRate);
		m.setNitrogenRate(nitrogenRate);
		m.setHygieneRate(hygieneRate);
		m.setHygieneKit(hygieneKit);
		m.setClothingRate(clothingRate);
		m.setPersonalItems(personalItems);
		m.setOfficeEquipment(officeEquipment);
		m.setEvaSuit(evaSuit);
		m.setEvaLithiumHydroxide(evaLithiumHydroxide);
		m.setHealthEquipment(healthEquipment);
		m.setHealthConsumables(healthConsumables);
		m.setSafetyEquipment(safetyEquipment);
		m.setCommEquipment(commEquipment);
		m.setComputerEquipment(computerEquipment);
		m.setTrashBagRate(trashBagRate);
		m.setWasteContainmentRate(wasteContainmentRate);
		return m;
	}
}
