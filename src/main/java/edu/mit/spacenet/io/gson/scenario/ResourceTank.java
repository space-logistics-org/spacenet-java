package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class ResourceTank extends Element {
	public UUID resource;
	public double maxAmount;
	public double amount;

	public static ResourceTank createFrom(edu.mit.spacenet.domain.element.ResourceTank element, Context context) {
		ResourceTank e = new ResourceTank();
		e.id = context.getUUID(element);
		e.name = element.getName();
		e.description = element.getDescription();
		e.accommodatationMass = element.getAccommodationMass();
		e.mass = element.getMass();
		e.volume = element.getVolume();
		e.classOfSupply = element.getClassOfSupply().getId();
		e.environment = element.getEnvironment().getName();
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		e.resource = context.getUUID(element.getResource());
		e.maxAmount = element.getMaxAmount();
		e.amount = element.getAmount();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.ResourceTank toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.ResourceTank e = new edu.mit.spacenet.domain.element.ResourceTank();
		e.setUid(context.getId(id, e));
		e.setName(name);
		e.setDescription(description);
		e.setAccommodationMass(accommodatationMass);
		e.setMass(mass);
		e.setVolume(volume);
		e.setClassOfSupply(ClassOfSupply.getInstance(classOfSupply));
		e.setEnvironment(Environment.getInstance(environment));
		e.setStates(State.toSpaceNet(states, context));
		e.setCurrentState((I_State) context.getObjectViaId(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.setResource((I_Resource) context.getObjectViaId(resource));
		e.setMaxAmount(maxAmount);
		e.setAmount(amount);
		return e;
	}
}
