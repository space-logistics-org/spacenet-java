package edu.mit.spacenet.io.gson.scenario;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class PropulsiveVehicle extends Element {
	public double isp;
	public UUID fuelType;
	public double fuelMaxAmount;
	public double fuelAmount;

	public static PropulsiveVehicle createFrom(edu.mit.spacenet.domain.element.PropulsiveVehicle element, Context context) {
		PropulsiveVehicle e = new PropulsiveVehicle();
		e.id = context.getUUID(element);
		e.type = TYPE_MAP.inverse().get(ElementType.PROPULSIVE_VEHICLE);
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
		e.isp = element.getOmsIsp();
		e.fuelType = context.getUUID(element.getOmsFuelTank().getResource());
		e.fuelMaxAmount = element.getOmsFuelTank().getMaxAmount();
		e.fuelAmount = element.getOmsFuelTank().getAmount();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.PropulsiveVehicle toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.PropulsiveVehicle e = new edu.mit.spacenet.domain.element.PropulsiveVehicle();
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
		e.setOmsIsp(isp);
		edu.mit.spacenet.domain.element.ResourceTank t = new edu.mit.spacenet.domain.element.ResourceTank();
		t.setResource((I_Resource) context.getObject(fuelType));
		t.setMaxAmount(fuelMaxAmount);
		t.setAmount(fuelAmount);
		e.setOmsFuelTank(t);
		return e;
	}
}
