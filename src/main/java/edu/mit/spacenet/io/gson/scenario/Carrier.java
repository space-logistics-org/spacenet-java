package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;

public class Carrier extends Element {
	public Double maxCargoMass;
	public Double maxCargoVolume;
	public String cargoEnvironment;
	public Integer maxCrewSize;
	public List<UUID> contents;

	public static Carrier createFrom(edu.mit.spacenet.domain.element.Carrier element, Context context) {
		Carrier e = new Carrier();
		e.id = context.getUUID(element);
		e.templateId = context.getElementTemplateUUID(element);
		Carrier template = (Carrier) context.getObject(e.templateId);
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
		}
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		e.contents = context.getUUIDs(element.getContents());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.Carrier toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.Carrier e = new edu.mit.spacenet.domain.element.Carrier();
		e.setUid(context.getId(id, e));
		e.setTid(templateId == null ? context.getId(id, e) : context.getId(templateId));
		edu.mit.spacenet.domain.element.Carrier template = (edu.mit.spacenet.domain.element.Carrier) context.getObject(templateId);
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
		
		e.setStates(State.toSpaceNet(e, states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		return e;
	}
	
	@Override
	public ElementPreview getPreview(Context context) {
		return new ElementPreview(context.getId(id), name, ElementType.CARRIER, ElementIcon.getInstance(icon));
	}
}
