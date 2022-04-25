package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class PropulsiveVehicle extends Carrier {
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
		e.states = State.createFrom(element.getStates(), context);
		e.currentState = context.getUUID(element.getCurrentState());
		e.parts = Part.createFrom(element.getParts(), context);
		e.maxCargoMass = element.getMaxCargoMass();
		e.maxCargoVolume = element.getMaxCargoVolume();
		e.cargoEnvironment = element.getCargoEnvironment().getName();
		e.contents = context.getUUIDs(element.getContents());
		e.isp = element.getOmsIsp();
		if(element.getOmsFuelTank() != null) {
			e.fuelType = context.getUUID(element.getOmsFuelTank().getResource());
			e.fuelMaxAmount = element.getOmsFuelTank().getMaxAmount();
			e.fuelAmount = element.getOmsFuelTank().getAmount();
		} else if(element.getRcsFuelTank() != null) {
			e.fuelType = context.getUUID(element.getRcsFuelTank().getResource());
			e.fuelMaxAmount = element.getRcsFuelTank().getMaxAmount();
			e.fuelAmount = element.getRcsFuelTank().getAmount();
		}
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
		e.setStates(State.toSpaceNet(states, context));
		e.setCurrentState((I_State) context.getObject(currentState));
		e.setParts(Part.toSpaceNet(parts, context));
		e.setMaxCargoMass(maxCargoMass);
		e.setMaxCargoVolume(maxCargoVolume);
		e.setCargoEnvironment(Environment.getInstance(cargoEnvironment));
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		e.setOmsIsp(isp);
		edu.mit.spacenet.domain.element.ResourceTank t = new edu.mit.spacenet.domain.element.ResourceTank();
		t.setResource((I_Resource) context.getObject(fuelType));
		t.setMaxAmount(fuelMaxAmount);
		t.setAmount(fuelAmount);
		e.setOmsFuelTank(t);
		return e;
	}
}
