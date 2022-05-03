package edu.mit.spacenet.io.gson.scenario;

public class ConsumablesDemandModel extends DemandModel {	
	public Double reservesDuration;
	public Double waterRecoveryRate;
	public Double clothingLifetime;
	public Boolean transitDemandsOmitted;
	public Double waterRate;
	public Double evaWaterRate;
	public Double foodSupportRate;
	public Double ambientFoodRate;
	public Double rfFoodRate;
	public Double oxygenRate;
	public Double evaOxygenRate;
	public Double nitrogenRate;
	public Double hygieneRate;
	public Double hygieneKit;
	public Double clothingRate;
	public Double personalItems;
	public Double officeEquipment;
	public Double evaSuit;
	public Double evaLithiumHydroxide;
	public Double healthEquipment;
	public Double healthConsumables;
	public Double safetyEquipment;
	public Double commEquipment;
	public Double computerEquipment;
	public Double trashBagRate;
	public Double wasteContainmentRate;

	public static ConsumablesDemandModel createFrom(edu.mit.spacenet.domain.model.CrewConsumablesDemandModel model, Context context) {
		ConsumablesDemandModel m = new ConsumablesDemandModel();
		context.getUUID(model); // register object
		m.templateId = context.getModelTemplateUUID(model);
		ConsumablesDemandModel template = (ConsumablesDemandModel) context.getObject(m.templateId);
		if(template == null) {
			m.name = model.getName();
			m.description = model.getDescription();
			m.reservesDuration = model.getReservesDuration();
			m.waterRecoveryRate = model.getWaterRecoveryRate();
			m.clothingLifetime = model.getClothingLifetime();
			m.transitDemandsOmitted = model.isTransitDemandsOmitted();
			m.waterRate = model.getWaterRate();
			m.evaWaterRate = model.getEvaWaterRate();
			m.foodSupportRate = model.getFoodSupportRate();
			m.ambientFoodRate = model.getAmbientFoodRate();
			m.rfFoodRate = model.getRfFoodRate();
			m.oxygenRate = model.getOxygenRate();
			m.evaOxygenRate = model.getEvaOxygenRate();
			m.nitrogenRate = model.getNitrogenRate();
			m.hygieneRate = model.getHygieneRate();
			m.hygieneKit = model.getHygieneKit();
			m.clothingRate = model.getClothingRate();
			m.personalItems = model.getPersonalItems();
			m.officeEquipment = model.getOfficeEquipment();
			m.evaSuit = model.getEvaSuit();
			m.evaLithiumHydroxide = model.getEvaLithiumHydroxide();
			m.healthEquipment = model.getHealthEquipment();
			m.healthConsumables = model.getHealthConsumables();
			m.safetyEquipment = model.getSafetyEquipment();
			m.commEquipment = model.getCommEquipment();
			m.computerEquipment = model.getComputerEquipment();
			m.trashBagRate = model.getTrashBagRate();
			m.wasteContainmentRate = model.getWasteContainmentRate();
		} else {
			if(!template.name.equals(model.getName())) {
				m.name = model.getName();
			}
			if(!template.description.equals(model.getDescription())) {
				m.description = model.getDescription();
			}
			if(!template.reservesDuration.equals(model.getReservesDuration())) {
				m.reservesDuration = model.getReservesDuration();
			}
			if(!template.waterRecoveryRate.equals(model.getWaterRecoveryRate())) {
				m.waterRecoveryRate = model.getWaterRecoveryRate();
			}
			if(!template.clothingLifetime.equals(model.getClothingLifetime())) {
				m.clothingLifetime = model.getClothingLifetime();
			}
			if(!template.transitDemandsOmitted.equals(model.isTransitDemandsOmitted())) {
				m.transitDemandsOmitted = model.isTransitDemandsOmitted();
			}
			if(!template.waterRate.equals(model.getWaterRate())) {
				m.waterRate = model.getWaterRate();
			}
			if(!template.evaWaterRate.equals(model.getEvaWaterRate())) {
				m.evaWaterRate = model.getEvaWaterRate();
			}
			if(!template.foodSupportRate.equals(model.getFoodSupportRate())) {
				m.foodSupportRate = model.getFoodSupportRate();
			}
			if(!template.ambientFoodRate.equals(model.getAmbientFoodRate())) {
				m.ambientFoodRate = model.getAmbientFoodRate();
			}
			if(!template.rfFoodRate.equals(model.getRfFoodRate())) {
				m.rfFoodRate = model.getRfFoodRate();
			}
			if(!template.oxygenRate.equals(model.getOxygenRate())) {
				m.oxygenRate = model.getOxygenRate();
			}
			if(!template.evaOxygenRate.equals(model.getEvaOxygenRate())) {
				m.evaOxygenRate = model.getEvaOxygenRate();
			}
			if(!template.nitrogenRate.equals(model.getNitrogenRate())) {
				m.nitrogenRate = model.getNitrogenRate();
			}
			if(!template.hygieneRate.equals(model.getHygieneRate())) {
				m.hygieneRate = model.getHygieneRate();
			}
			if(!template.hygieneKit.equals(model.getHygieneKit())) {
				m.hygieneKit = model.getHygieneKit();
			}
			if(!template.clothingRate.equals(model.getClothingRate())) {
				m.clothingRate = model.getClothingRate();
			}
			if(!template.personalItems.equals(model.getPersonalItems())) {
				m.personalItems = model.getPersonalItems();
			}
			if(!template.officeEquipment.equals(model.getOfficeEquipment())) {
				m.officeEquipment = model.getOfficeEquipment();
			}
			if(!template.evaSuit.equals(model.getEvaSuit())) {
				m.evaSuit = model.getEvaSuit();
			}
			if(!template.evaLithiumHydroxide.equals(model.getEvaLithiumHydroxide())) {
				m.evaLithiumHydroxide = model.getEvaLithiumHydroxide();
			}
			if(!template.healthEquipment.equals(model.getHealthEquipment())) {
				m.healthEquipment = model.getHealthEquipment();
			}
			if(!template.healthConsumables.equals(model.getHealthConsumables())) {
				m.healthConsumables = model.getHealthConsumables();
			}
			if(!template.safetyEquipment.equals(model.getSafetyEquipment())) {
				m.safetyEquipment = model.getSafetyEquipment();
			}
			if(!template.commEquipment.equals(model.getCommEquipment())) {
				m.commEquipment = model.getCommEquipment();
			}
			if(!template.computerEquipment.equals(model.getComputerEquipment())) {
				m.computerEquipment = model.getComputerEquipment();
			}
			if(!template.trashBagRate.equals(model.getTrashBagRate())) {
				m.trashBagRate = model.getTrashBagRate();
			}
			if(!template.wasteContainmentRate.equals(model.getWasteContainmentRate())) {
				m.wasteContainmentRate = model.getWasteContainmentRate();
			}
		}
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.CrewConsumablesDemandModel toSpaceNet(Object source, Context context) {
		edu.mit.spacenet.domain.model.CrewConsumablesDemandModel m = new edu.mit.spacenet.domain.model.CrewConsumablesDemandModel((edu.mit.spacenet.scenario.Mission) source);
		m.setTid(templateId == null ? context.getId(id, m) : context.getId(templateId, context.getObject(templateId)));
		edu.mit.spacenet.domain.model.CrewConsumablesDemandModel template = (edu.mit.spacenet.domain.model.CrewConsumablesDemandModel) context.getObject(templateId);
		m.setName(name == null ? template.getName() : name);
		m.setDescription(description == null ? template.getDescription() : description);
		m.setReservesDuration(reservesDuration == null ? template.getReservesDuration() : reservesDuration);
		m.setWaterRecoveryRate(waterRecoveryRate == null ? template.getWaterRecoveryRate() : waterRecoveryRate);
		m.setClothingLifetime(clothingLifetime == null ? template.getClothingLifetime() : clothingLifetime);
		m.setTransitDemandsOmitted(transitDemandsOmitted == null ? template.isTransitDemandsOmitted() : transitDemandsOmitted);
		m.setWaterRate(waterRate == null ? template.getWaterRate() : waterRate);
		m.setEvaWaterRate(evaWaterRate == null ? template.getEvaWaterRate() : evaWaterRate);
		m.setFoodSupportRate(foodSupportRate == null ? template.getFoodSupportRate() : foodSupportRate);
		m.setAmbientFoodRate(ambientFoodRate == null ? template.getAmbientFoodRate() : ambientFoodRate);
		m.setRfFoodRate(rfFoodRate == null ? template.getRfFoodRate() : rfFoodRate);
		m.setOxygenRate(oxygenRate == null ? template.getOxygenRate() : oxygenRate);
		m.setEvaOxygenRate(evaOxygenRate == null ? template.getEvaOxygenRate() : evaOxygenRate);
		m.setNitrogenRate(nitrogenRate == null ? template.getNitrogenRate() : nitrogenRate);
		m.setHygieneRate(hygieneRate == null ? template.getHygieneRate() : hygieneRate);
		m.setHygieneKit(hygieneKit == null ? template.getHygieneKit() : hygieneKit);
		m.setClothingRate(clothingRate == null ? template.getClothingRate() : clothingRate);
		m.setPersonalItems(personalItems == null ? template.getPersonalItems() : personalItems);
		m.setOfficeEquipment(officeEquipment == null ? template.getOfficeEquipment() : officeEquipment);
		m.setEvaSuit(evaSuit == null ? template.getEvaSuit() : evaSuit);
		m.setEvaLithiumHydroxide(evaLithiumHydroxide == null ? template.getEvaLithiumHydroxide() : evaLithiumHydroxide);
		m.setHealthEquipment(healthEquipment == null ? template.getHealthEquipment() : healthEquipment);
		m.setHealthConsumables(healthConsumables == null ? template.getHealthConsumables() : healthConsumables);
		m.setSafetyEquipment(safetyEquipment == null ? template.getSafetyEquipment() : safetyEquipment);
		m.setCommEquipment(commEquipment == null ? template.getCommEquipment() : commEquipment);
		m.setComputerEquipment(computerEquipment == null ? template.getComputerEquipment() : computerEquipment);
		m.setTrashBagRate(trashBagRate == null ? template.getTrashBagRate() : trashBagRate);
		m.setWasteContainmentRate(wasteContainmentRate == null ? template.getWasteContainmentRate() : wasteContainmentRate);
		return m;
	}
	
	@Override
	public ConsumablesDemandModel clone() {
		ConsumablesDemandModel m = new ConsumablesDemandModel();
		m.id = UUID.randomUUID();
		m.name = name;
		m.description = description;
		m.reservesDuration = reservesDuration;
		m.waterRecoveryRate = waterRecoveryRate;
		m.clothingLifetime = clothingLifetime;
		m.transitDemandsOmitted = transitDemandsOmitted;
		m.waterRate = waterRate;
		m.evaWaterRate = evaWaterRate;
		m.foodSupportRate = foodSupportRate;
		m.ambientFoodRate = ambientFoodRate;
		m.rfFoodRate = rfFoodRate;
		m.oxygenRate = oxygenRate;
		m.evaOxygenRate = evaOxygenRate;
		m.nitrogenRate = nitrogenRate;
		m.hygieneRate = hygieneRate;
		m.hygieneKit = hygieneKit;
		m.clothingRate = clothingRate;
		m.personalItems = personalItems;
		m.officeEquipment = officeEquipment;
		m.evaSuit = evaSuit;
		m.evaLithiumHydroxide = evaLithiumHydroxide;
		m.healthEquipment = healthEquipment;
		m.healthConsumables = healthConsumables;
		m.safetyEquipment = safetyEquipment;
		m.commEquipment = commEquipment;
		m.computerEquipment = computerEquipment;
		m.trashBagRate = trashBagRate;
		m.wasteContainmentRate = wasteContainmentRate;
		return m;
	}
}
