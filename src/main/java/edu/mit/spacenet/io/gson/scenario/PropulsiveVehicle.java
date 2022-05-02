package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class PropulsiveVehicle extends Carrier {
	public Double isp;
	public UUID fuelType;
	public Double fuelMaxAmount;
	public Double fuelAmount;

	public static PropulsiveVehicle createFrom(edu.mit.spacenet.domain.element.PropulsiveVehicle element, Context context) {
		PropulsiveVehicle e = new PropulsiveVehicle();
		e.id = context.getUUID(element);
		e.templateId = context.getElementTemplateUUID(element);
		PropulsiveVehicle template = (PropulsiveVehicle) context.getObject(e.templateId);
		if(template == null) {
			e.name = element.getName();
			e.description = element.getDescription();
			e.accommodatationMass = element.getAccommodationMass();
			e.mass = element.getMass();
			e.volume = element.getVolume();
			e.classOfSupply = element.getClassOfSupply().getId();
			e.environment = element.getEnvironment().getName();
			if(element.getIconType() != element.getElementType().getIconType()) {
				e.icon = element.getIconType().getName();
			}
			e.maxCargoMass = element.getMaxCargoMass();
			e.maxCargoVolume = element.getMaxCargoVolume();
			e.cargoEnvironment = element.getCargoEnvironment().getName();
			e.maxCrewSize = element.getMaxCrewSize();
			if(element.getOmsFuelTank() != null) {
				e.isp = element.getOmsIsp();
				e.fuelType = context.getUUID(element.getOmsFuelTank().getResource());
				e.fuelMaxAmount = element.getOmsFuelTank().getMaxAmount();
				e.fuelAmount = element.getOmsFuelTank().getAmount();
			} else if(element.getRcsFuelTank() != null) {
				e.isp = element.getRcsIsp();
				e.fuelType = context.getUUID(element.getRcsFuelTank().getResource());
				e.fuelMaxAmount = element.getRcsFuelTank().getMaxAmount();
				e.fuelAmount = element.getRcsFuelTank().getAmount();
			}
		} else {
			if(!template.name.equals(element.getName())) {
				e.name = element.getName();
			}
			if(!template.description.equals(element.getDescription())) {
				e.description = element.getDescription();
			}
			if(!template.accommodatationMass.equals(element.getAccommodationMass())) {
				e.accommodatationMass = element.getAccommodationMass();
			}
			if(!template.mass.equals(element.getMass())) {
				e.mass = element.getMass();
			}
			if(!template.volume.equals(element.getVolume())) {
				e.volume = element.getVolume();
			}
			if(!template.classOfSupply.equals(element.getClassOfSupply().getId())) {
				e.classOfSupply = element.getClassOfSupply().getId();
			}
			if(!template.environment.equals(element.getEnvironment().getName())) {
				e.environment = element.getEnvironment().getName();
			}
			if((template.icon == null && element.getIconType() != element.getElementType().getIconType()) 
					|| (template.icon != null && !template.icon.equals(element.getIconType().getName()))) {
				e.icon = element.getIconType().getName();
			}
			if(!template.maxCargoMass.equals(element.getMaxCargoMass())) {
				e.maxCargoMass = element.getMaxCargoMass();
			}
			if(!template.maxCargoVolume.equals(element.getMaxCargoVolume())) {
				e.maxCargoVolume = element.getMaxCargoVolume();
			}
			if(!template.cargoEnvironment.equals(element.getCargoEnvironment().getName())) {
				e.cargoEnvironment = element.getCargoEnvironment().getName();
			}
			if(!template.maxCrewSize.equals(element.getMaxCrewSize())) {
				e.maxCrewSize = element.getMaxCrewSize();
			}
			if(!template.isp.equals(element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp())) {
				e.isp = element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp();
			}
			if(!template.fuelType.equals(element.getOmsFuelTank() != null ? context.getUUID(element.getOmsFuelTank().getResource()) : context.getUUID(element.getRcsFuelTank().getResource()))) {
				e.fuelType = element.getOmsFuelTank() != null ? context.getUUID(element.getOmsFuelTank().getResource()) : context.getUUID(element.getRcsFuelTank().getResource());
			}
			if(!template.fuelMaxAmount.equals(element.getOmsFuelTank() != null ? element.getOmsFuelTank().getMaxAmount() : element.getRcsFuelTank().getMaxAmount())) {
				e.fuelMaxAmount = element.getOmsFuelTank() != null ? element.getOmsFuelTank().getMaxAmount() : element.getRcsFuelTank().getMaxAmount();
			}
			if(!template.fuelAmount.equals(element.getOmsFuelTank() != null ? element.getOmsFuelTank().getAmount() : element.getRcsFuelTank().getAmount())) {
				e.fuelAmount = element.getOmsFuelTank() != null ? element.getOmsFuelTank().getAmount() : element.getRcsFuelTank().getAmount();
			}
		}
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		e.contents = context.getUUIDs(element.getContents());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.PropulsiveVehicle toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.PropulsiveVehicle e = new edu.mit.spacenet.domain.element.PropulsiveVehicle();
		e.setUid(context.getId(id, e));
		e.setTid(templateId == null ? context.getId(id, e) : context.getId(templateId));
		edu.mit.spacenet.domain.element.PropulsiveVehicle template = (edu.mit.spacenet.domain.element.PropulsiveVehicle) context.getObject(templateId);
		e.setName(name == null ? template.getName() : name);
		e.setDescription(description == null ? template.getDescription() : description);
		e.setAccommodationMass(accommodatationMass == null ? template.getAccommodationMass() : accommodatationMass);
		e.setMass(mass == null ? template.getMass() : mass);
		e.setVolume(volume == null ? template.getVolume() : volume);
		e.setClassOfSupply(classOfSupply == null ? template.getClassOfSupply() : ClassOfSupply.getInstance(classOfSupply));
		e.setEnvironment(environment == null ? template.getEnvironment() : Environment.getInstance(environment));
		if(icon == null && template != null && template.getIconType() != template.getElementType().getIconType()) {
			e.setIconType(template.getIconType());
		}
		e.setMaxCargoMass(maxCargoMass == null ? template.getMaxCargoMass() : maxCargoMass);
		e.setMaxCargoVolume(maxCargoVolume == null ? template.getMaxCargoVolume() : maxCargoVolume);
		e.setCargoEnvironment(cargoEnvironment == null ? template.getCargoEnvironment() : Environment.getInstance(cargoEnvironment));
		e.setMaxCrewSize(maxCrewSize == null ? template.getMaxCrewSize() : maxCrewSize);
		e.setOmsIsp(isp == null ? template.getOmsIsp() : isp);
		edu.mit.spacenet.domain.element.ResourceTank t = new edu.mit.spacenet.domain.element.ResourceTank();
		t.setResource(fuelType == null ? template.getOmsFuelTank().getResource() : (I_Resource) context.getObject(fuelType));
		t.setMaxAmount(fuelMaxAmount == null ? template.getOmsFuelTank().getMaxAmount() : fuelMaxAmount);
		t.setAmount(fuelAmount == null ? template.getOmsFuelTank().getAmount() : fuelAmount);
		e.setOmsFuelTank(t);

		e.setStates(State.toSpaceNet(e, states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		return e;
	}
	
	@Override
	public ElementPreview getPreview(Context context) {
		return new ElementPreview(context.getId(id), name, ElementType.PROPULSIVE_VEHICLE, ElementIcon.getInstance(icon));
	}
}
