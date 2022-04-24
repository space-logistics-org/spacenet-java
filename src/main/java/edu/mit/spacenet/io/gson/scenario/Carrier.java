package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class Carrier extends Element {
	public double maxCargoMass;
	public double maxCargoVolume;
	public String cargoEnvironment;
	public int maxCrewSize;
	public List<UUID> contents = new ArrayList<UUID>();

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
		for(edu.mit.spacenet.domain.element.I_State state : element.getStates()) {
			e.states.add(State.createFrom(state, context));
		}
		e.currentState = context.getUUID(element.getCurrentState());
		for(edu.mit.spacenet.domain.element.PartApplication part : element.getParts()) {
			e.parts.add(Part.createFrom(part, context));
		}
		e.maxCargoMass = element.getMaxCargoMass();
		e.maxCargoVolume = element.getMaxCargoVolume();
		e.cargoEnvironment = element.getCargoEnvironment().getName();
		for(I_Element c : element.getContents()) {
			e.contents.add(context.getUUID(c));
		}
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
		e.setMaxCargoMass(maxCargoMass);
		e.setMaxCargoVolume(maxCargoVolume);
		e.setCargoEnvironment(Environment.getInstance(cargoEnvironment));
		for(UUID c : contents) {
			e.getContents().add((I_Element) context.getObject(c));
		}
		return e;
	}
}
