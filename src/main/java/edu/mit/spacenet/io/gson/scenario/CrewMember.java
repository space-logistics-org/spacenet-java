package edu.mit.spacenet.io.gson.scenario;

import java.util.SortedSet;
import java.util.TreeSet;

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
		for(edu.mit.spacenet.domain.element.I_State state : element.getStates()) {
			e.states.add(State.createFrom(state, context));
		}
		e.currentState = context.getUUID(element.getCurrentState());
		for(edu.mit.spacenet.domain.element.PartApplication part : element.getParts()) {
			e.parts.add(Part.createFrom(part, context));
		}
		e.availableTimeFraction = element.getAvailableTimeFraction();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.CrewMember toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.CrewMember e = new edu.mit.spacenet.domain.element.CrewMember();
		e.setUid(context.getId(id));
		e.setName(name);
		e.setDescription(description);
		e.setAccommodationMass(accommodatationMass);
		e.setMass(mass);
		e.setVolume(volume);
		e.setClassOfSupply(ClassOfSupply.getInstance(classOfSupply));
		e.setEnvironment(Environment.getInstance(environment));
		SortedSet<edu.mit.spacenet.domain.element.I_State> ss = new TreeSet<edu.mit.spacenet.domain.element.I_State>();
		for(State state : states) {
			ss.add(state.toSpaceNet(context));
		}
		e.setStates(ss);
		e.setCurrentState((I_State) context.getObject(currentState));
		SortedSet<edu.mit.spacenet.domain.element.PartApplication> ps = new TreeSet<edu.mit.spacenet.domain.element.PartApplication>();
		for(Part part : parts) {
			ps.add(part.toSpaceNet(context));
		}
		e.setParts(ps);
		e.setAvailableTimeFraction(availableTimeFraction);
		return e;
	}
}
