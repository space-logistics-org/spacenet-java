package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;

public class Carrier extends Element {
	public double maxCargoMass;
	public double maxCargoVolume;
	public String cargoEnvironment;
	public int maxCrewSize;
	public List<UUID> contents;

	public static Carrier createFrom(edu.mit.spacenet.domain.element.Carrier element, Context context) {
		Carrier e = new Carrier();
		e.id = context.getUUID(element);
		e.type = TYPE_MAP.inverse().get(ElementType.CARRIER);
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
		e.contents = context.getUUIDs(element.getContents());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.Carrier toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.Carrier e = new edu.mit.spacenet.domain.element.Carrier();
		e.setUid(context.getId(id));
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
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		return e;
	}
}
