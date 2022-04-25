package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;

public class CrewMember extends Element {
	public double availableTimeFraction;

	public static CrewMember createFrom(edu.mit.spacenet.domain.element.CrewMember element, Context context) {
		CrewMember e = new CrewMember();
		e.id = context.getUUID(element);
		e.type = TYPE_MAP.inverse().get(ElementType.RESOURCE_CONTAINER);
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
		e.availableTimeFraction = element.getAvailableTimeFraction();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.CrewMember toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.CrewMember e = new edu.mit.spacenet.domain.element.CrewMember();
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
		e.setAvailableTimeFraction(availableTimeFraction);
		return e;
	}
}
