package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class ResourceTank extends Element {
	public UUID resource;
	public Double maxAmount;
	public Double amount;

	public static ResourceTank createFrom(edu.mit.spacenet.domain.element.ResourceTank element, Context context) {
		ResourceTank e = new ResourceTank();
		e.id = context.getUUID(element);
		e.templateId = context.getElementTemplateUUID(element);
		ResourceTank template = (ResourceTank) context.getObject(e.templateId);
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
			e.resource = context.getUUID(element.getResource());
			e.maxAmount = element.getMaxAmount();
			e.amount = element.getAmount();
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
			if(!template.resource.equals(context.getUUID(element.getResource()))) {
				e.resource = context.getUUID(element.getResource());
			}
			if(!template.maxAmount.equals(element.getMaxAmount())) {
				e.maxAmount = element.getMaxAmount();
			}
			if(!template.amount.equals(element.getAmount())) {
				e.amount = element.getAmount();
			}
		}
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.ResourceTank toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.ResourceTank e = new edu.mit.spacenet.domain.element.ResourceTank();
		e.setUid(context.getId(id, e));
		e.setTid(templateId == null ? context.getId(id, e) : context.getId(templateId));
		edu.mit.spacenet.domain.element.ResourceTank template = (edu.mit.spacenet.domain.element.ResourceTank) context.getObject(templateId);
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
		e.setResource(resource == null ? template.getResource() : (I_Resource) context.getObject(resource));
		e.setMaxAmount(maxAmount == null ? template.getMaxAmount() : maxAmount);
		e.setAmount(amount == null ? template.getAmount() : amount);

		e.setStates(State.toSpaceNet(e, states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		return e;
	}
	
	@Override
	public ElementPreview getPreview(Context context) {
		return new ElementPreview(context.getId(id), name, ElementType.RESOURCE_TANK, ElementIcon.getInstance(icon));
	}
}
