package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;

public class Element {
	public static final BiMap<String, ElementType> TYPE_MAP = new ImmutableBiMap.Builder<String, ElementType>()
			.put("Element", ElementType.ELEMENT)
			.put("Crew Member", ElementType.CREW_MEMBER)
			.put("Resource Container", ElementType.RESOURCE_CONTAINER)
			.put("Resource Tank", ElementType.RESOURCE_TANK)
			.put("Carrier", ElementType.CARRIER)
			.put("Propulsive Vehicle", ElementType.PROPULSIVE_VEHICLE)
			.put("Surface Vehicle", ElementType.SURFACE_VEHICLE)
			.build();

	public UUID id;
	public String type;
	public String name;
	public String description;
	public double accommodatationMass;
	public double mass;
	public double volume;
	public int classOfSupply;
	public String environment;
	public List<State> states = new ArrayList<State>();
	public UUID currentState;
	public List<Part> parts = new ArrayList<Part>();

	public static Element createFrom(edu.mit.spacenet.domain.element.I_Element element, Context context) {
		if(element.getElementType() == ElementType.ELEMENT) {
			Element e = new Element();
			e.id = context.getUUID(element);
			e.type = TYPE_MAP.inverse().get(ElementType.ELEMENT);
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
			return e;
		} else if(element.getElementType() == ElementType.RESOURCE_CONTAINER) {
			return ResourceContainer.createFrom((edu.mit.spacenet.domain.element.ResourceContainer) element, context);
		} else if(element.getElementType() == ElementType.RESOURCE_TANK) {
			return ResourceTank.createFrom((edu.mit.spacenet.domain.element.ResourceTank) element, context);
		} else if(element.getElementType() == ElementType.CARRIER) {
			return Carrier.createFrom((edu.mit.spacenet.domain.element.Carrier) element, context);
		} else if(element.getElementType() == ElementType.PROPULSIVE_VEHICLE) {
			return PropulsiveVehicle.createFrom((edu.mit.spacenet.domain.element.PropulsiveVehicle) element, context);
		} else if(element.getElementType() == ElementType.SURFACE_VEHICLE) {
			return SurfaceVehicle.createFrom((edu.mit.spacenet.domain.element.SurfaceVehicle) element, context);
		} else if(element.getElementType() == ElementType.CREW_MEMBER) {
			return CrewMember.createFrom((edu.mit.spacenet.domain.element.CrewMember) element, context);
		} else {
			throw new UnsupportedOperationException("unknown element type: " + element.getElementType());
		}
	}
	
	public edu.mit.spacenet.domain.element.Element toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.Element e = new edu.mit.spacenet.domain.element.Element();
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
		return e;
	}
}
