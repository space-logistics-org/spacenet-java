package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;

public class ResourceContainer extends Element {
	public Double maxCargoMass;
	public Double maxCargoVolume;
	public String cargoEnvironment;
	public List<Resource> contents = new ArrayList<Resource>();

	public static ResourceContainer createFrom(edu.mit.spacenet.domain.element.ResourceContainer element, Context context) {
		ResourceContainer e = new ResourceContainer();
		e.id = context.getUUID(element);
		e.templateId = context.getElementTemplateUUID(element);
		ResourceContainer template = (ResourceContainer) context.getObject(e.templateId);
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
		}
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		e.contents = Resource.createFrom(element.getContents(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.ResourceContainer toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.ResourceContainer e = new edu.mit.spacenet.domain.element.ResourceContainer();
		e.setUid(context.getId(id, e));
		e.setTid(templateId == null ? context.getId(id, e) : context.getId(templateId));
		edu.mit.spacenet.domain.element.ResourceContainer template = (edu.mit.spacenet.domain.element.ResourceContainer) context.getObject(templateId);
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

		e.setStates(State.toSpaceNet(e, states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.getContents().putAll(Resource.toSpaceNetMap(contents, context));
		return e;
	}
	
	@Override
	public ElementPreview getPreview(Context context) {
		return new ElementPreview(context.getId(id), name, ElementType.RESOURCE_CONTAINER, ElementIcon.getInstance(icon));
	}
}
