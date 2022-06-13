package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ConsumablesDemandModel extends DemandModel {
  protected Double reservesDuration;
  protected Double waterRecoveryRate;
  protected Double clothingLifetime;
  protected Boolean transitDemandsOmitted;
  protected Double waterRate;
  protected Double evaWaterRate;
  protected Double foodSupportRate;
  protected Double ambientFoodRate;
  protected Double rfFoodRate;
  protected Double oxygenRate;
  protected Double evaOxygenRate;
  protected Double nitrogenRate;
  protected Double hygieneRate;
  protected Double hygieneKit;
  protected Double clothingRate;
  protected Double personalItems;
  protected Double officeEquipment;
  protected Double evaSuit;
  protected Double evaLithiumHydroxide;
  protected Double healthEquipment;
  protected Double healthConsumables;
  protected Double safetyEquipment;
  protected Double commEquipment;
  protected Double computerEquipment;
  protected Double trashBagRate;
  protected Double wasteContainmentRate;

  public static ConsumablesDemandModel createFrom(
      edu.mit.spacenet.domain.model.CrewConsumablesDemandModel model, Context context) {
    ConsumablesDemandModel m = new ConsumablesDemandModel();
    m.templateId = context.getModelTemplate(model.getTid());
    ConsumablesDemandModel template = (ConsumablesDemandModel) context.getJsonObject(m.templateId);
    if (template == null) {
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
      if (!template.name.equals(model.getName())) {
        m.name = model.getName();
      }
      if (!template.description.equals(model.getDescription())) {
        m.description = model.getDescription();
      }
      if (!template.reservesDuration.equals(model.getReservesDuration())) {
        m.reservesDuration = model.getReservesDuration();
      }
      if (!template.waterRecoveryRate.equals(model.getWaterRecoveryRate())) {
        m.waterRecoveryRate = model.getWaterRecoveryRate();
      }
      if (!template.clothingLifetime.equals(model.getClothingLifetime())) {
        m.clothingLifetime = model.getClothingLifetime();
      }
      if (!template.transitDemandsOmitted.equals(model.isTransitDemandsOmitted())) {
        m.transitDemandsOmitted = model.isTransitDemandsOmitted();
      }
      if (!template.waterRate.equals(model.getWaterRate())) {
        m.waterRate = model.getWaterRate();
      }
      if (!template.evaWaterRate.equals(model.getEvaWaterRate())) {
        m.evaWaterRate = model.getEvaWaterRate();
      }
      if (!template.foodSupportRate.equals(model.getFoodSupportRate())) {
        m.foodSupportRate = model.getFoodSupportRate();
      }
      if (!template.ambientFoodRate.equals(model.getAmbientFoodRate())) {
        m.ambientFoodRate = model.getAmbientFoodRate();
      }
      if (!template.rfFoodRate.equals(model.getRfFoodRate())) {
        m.rfFoodRate = model.getRfFoodRate();
      }
      if (!template.oxygenRate.equals(model.getOxygenRate())) {
        m.oxygenRate = model.getOxygenRate();
      }
      if (!template.evaOxygenRate.equals(model.getEvaOxygenRate())) {
        m.evaOxygenRate = model.getEvaOxygenRate();
      }
      if (!template.nitrogenRate.equals(model.getNitrogenRate())) {
        m.nitrogenRate = model.getNitrogenRate();
      }
      if (!template.hygieneRate.equals(model.getHygieneRate())) {
        m.hygieneRate = model.getHygieneRate();
      }
      if (!template.hygieneKit.equals(model.getHygieneKit())) {
        m.hygieneKit = model.getHygieneKit();
      }
      if (!template.clothingRate.equals(model.getClothingRate())) {
        m.clothingRate = model.getClothingRate();
      }
      if (!template.personalItems.equals(model.getPersonalItems())) {
        m.personalItems = model.getPersonalItems();
      }
      if (!template.officeEquipment.equals(model.getOfficeEquipment())) {
        m.officeEquipment = model.getOfficeEquipment();
      }
      if (!template.evaSuit.equals(model.getEvaSuit())) {
        m.evaSuit = model.getEvaSuit();
      }
      if (!template.evaLithiumHydroxide.equals(model.getEvaLithiumHydroxide())) {
        m.evaLithiumHydroxide = model.getEvaLithiumHydroxide();
      }
      if (!template.healthEquipment.equals(model.getHealthEquipment())) {
        m.healthEquipment = model.getHealthEquipment();
      }
      if (!template.healthConsumables.equals(model.getHealthConsumables())) {
        m.healthConsumables = model.getHealthConsumables();
      }
      if (!template.safetyEquipment.equals(model.getSafetyEquipment())) {
        m.safetyEquipment = model.getSafetyEquipment();
      }
      if (!template.commEquipment.equals(model.getCommEquipment())) {
        m.commEquipment = model.getCommEquipment();
      }
      if (!template.computerEquipment.equals(model.getComputerEquipment())) {
        m.computerEquipment = model.getComputerEquipment();
      }
      if (!template.trashBagRate.equals(model.getTrashBagRate())) {
        m.trashBagRate = model.getTrashBagRate();
      }
      if (!template.wasteContainmentRate.equals(model.getWasteContainmentRate())) {
        m.wasteContainmentRate = model.getWasteContainmentRate();
      }
    }
    return m;
  }

  @Override
  public edu.mit.spacenet.domain.model.CrewConsumablesDemandModel toSpaceNet(Object source,
      Context context) {
    edu.mit.spacenet.domain.model.CrewConsumablesDemandModel m =
        new edu.mit.spacenet.domain.model.CrewConsumablesDemandModel(
            (edu.mit.spacenet.scenario.Mission) source);
    if (id != null) {
      context.putModelTemplate(m, id, this);
    }
    m.setTid(context.getJavaId(templateId == null ? id : templateId));
    ConsumablesDemandModel template = (ConsumablesDemandModel) context.getJsonObject(templateId);
    m.setName(name == null ? template.name : name);
    m.setDescription(description == null ? template.description : description);
    m.setReservesDuration(reservesDuration == null ? template.reservesDuration : reservesDuration);
    m.setWaterRecoveryRate(
        waterRecoveryRate == null ? template.waterRecoveryRate : waterRecoveryRate);
    m.setClothingLifetime(clothingLifetime == null ? template.clothingLifetime : clothingLifetime);
    m.setTransitDemandsOmitted(
        transitDemandsOmitted == null ? template.transitDemandsOmitted : transitDemandsOmitted);
    m.setWaterRate(waterRate == null ? template.waterRate : waterRate);
    m.setEvaWaterRate(evaWaterRate == null ? template.evaWaterRate : evaWaterRate);
    m.setFoodSupportRate(foodSupportRate == null ? template.foodSupportRate : foodSupportRate);
    m.setAmbientFoodRate(ambientFoodRate == null ? template.ambientFoodRate : ambientFoodRate);
    m.setRfFoodRate(rfFoodRate == null ? template.rfFoodRate : rfFoodRate);
    m.setOxygenRate(oxygenRate == null ? template.oxygenRate : oxygenRate);
    m.setEvaOxygenRate(evaOxygenRate == null ? template.evaOxygenRate : evaOxygenRate);
    m.setNitrogenRate(nitrogenRate == null ? template.nitrogenRate : nitrogenRate);
    m.setHygieneRate(hygieneRate == null ? template.hygieneRate : hygieneRate);
    m.setHygieneKit(hygieneKit == null ? template.hygieneKit : hygieneKit);
    m.setClothingRate(clothingRate == null ? template.clothingRate : clothingRate);
    m.setPersonalItems(personalItems == null ? template.personalItems : personalItems);
    m.setOfficeEquipment(officeEquipment == null ? template.officeEquipment : officeEquipment);
    m.setEvaSuit(evaSuit == null ? template.evaSuit : evaSuit);
    m.setEvaLithiumHydroxide(
        evaLithiumHydroxide == null ? template.evaLithiumHydroxide : evaLithiumHydroxide);
    m.setHealthEquipment(healthEquipment == null ? template.healthEquipment : healthEquipment);
    m.setHealthConsumables(
        healthConsumables == null ? template.healthConsumables : healthConsumables);
    m.setSafetyEquipment(safetyEquipment == null ? template.safetyEquipment : safetyEquipment);
    m.setCommEquipment(commEquipment == null ? template.commEquipment : commEquipment);
    m.setComputerEquipment(
        computerEquipment == null ? template.computerEquipment : computerEquipment);
    m.setTrashBagRate(trashBagRate == null ? template.trashBagRate : trashBagRate);
    m.setWasteContainmentRate(
        wasteContainmentRate == null ? template.wasteContainmentRate : wasteContainmentRate);
    return m;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ConsumablesDemandModel)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final ConsumablesDemandModel other = (ConsumablesDemandModel) obj;
    return new EqualsBuilder().appendSuper(super.equals(other))
        .append(reservesDuration, other.reservesDuration)
        .append(waterRecoveryRate, other.waterRecoveryRate)
        .append(clothingLifetime, other.clothingLifetime)
        .append(transitDemandsOmitted, other.transitDemandsOmitted)
        .append(waterRate, other.waterRate).append(evaWaterRate, other.evaWaterRate)
        .append(foodSupportRate, other.foodSupportRate)
        .append(ambientFoodRate, other.ambientFoodRate).append(rfFoodRate, other.rfFoodRate)
        .append(oxygenRate, other.oxygenRate).append(evaOxygenRate, other.evaOxygenRate)
        .append(nitrogenRate, other.nitrogenRate).append(hygieneRate, other.hygieneRate)
        .append(hygieneKit, other.hygieneKit).append(clothingRate, other.clothingRate)
        .append(personalItems, other.personalItems).append(officeEquipment, other.officeEquipment)
        .append(evaSuit, other.evaSuit).append(evaLithiumHydroxide, other.evaLithiumHydroxide)
        .append(healthEquipment, other.healthEquipment)
        .append(healthConsumables, other.healthConsumables)
        .append(safetyEquipment, other.safetyEquipment).append(commEquipment, other.commEquipment)
        .append(computerEquipment, other.computerEquipment).append(trashBagRate, other.trashBagRate)
        .append(wasteContainmentRate, other.wasteContainmentRate).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(id).append(templateId).append(name)
        .append(description).append(reservesDuration).append(waterRecoveryRate)
        .append(clothingLifetime).append(transitDemandsOmitted).append(waterRate)
        .append(evaWaterRate).append(foodSupportRate).append(ambientFoodRate).append(rfFoodRate)
        .append(oxygenRate).append(evaOxygenRate).append(nitrogenRate).append(hygieneRate)
        .append(hygieneKit).append(clothingRate).append(personalItems).append(officeEquipment)
        .append(evaSuit).append(evaLithiumHydroxide).append(healthEquipment)
        .append(healthConsumables).append(safetyEquipment).append(commEquipment)
        .append(computerEquipment).append(trashBagRate).append(wasteContainmentRate).toHashCode();
  }

  @Override
  public ConsumablesDemandModel clone() {
    ConsumablesDemandModel m = new ConsumablesDemandModel();
    if (id != null) {
      m.id = UUID.randomUUID();
    }
    m.templateId = templateId;
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
