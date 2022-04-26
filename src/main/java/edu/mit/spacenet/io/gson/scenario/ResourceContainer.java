package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.I_State;

public class ResourceContainer extends Element {
	public double maxCargoMass;
	public double maxCargoVolume;
	public String cargoEnvironment;
	public List<Resource> contents = new ArrayList<Resource>();

	public static ResourceContainer createFrom(edu.mit.spacenet.domain.element.ResourceContainer element, Context context) {
		ResourceContainer e = new ResourceContainer();
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
		e.maxCargoMass = element.getMaxCargoMass();
		e.maxCargoVolume = element.getMaxCargoVolume();
		e.cargoEnvironment = element.getCargoEnvironment().getName();
		e.contents = Resource.createFrom(element.getContents(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.ResourceContainer toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.ResourceContainer e = new edu.mit.spacenet.domain.element.ResourceContainer();
		e.setUid(context.getId(id, e));
		e.setName(name);
		e.setDescription(description);
		e.setAccommodationMass(accommodatationMass);
		e.setMass(mass);
		e.setVolume(volume);
		e.setClassOfSupply(ClassOfSupply.getInstance(classOfSupply));
		e.setEnvironment(Environment.getInstance(environment));
		e.setStates(State.toSpaceNet(states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.setMaxCargoMass(maxCargoMass);
		e.setMaxCargoVolume(maxCargoVolume);
		e.setCargoEnvironment(Environment.getInstance(cargoEnvironment));
		e.getContents().putAll(Resource.toSpaceNetMap(contents, context));
		return e;
	}
}
