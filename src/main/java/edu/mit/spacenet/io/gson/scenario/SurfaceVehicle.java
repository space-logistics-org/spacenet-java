package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class SurfaceVehicle extends Carrier {
	public double maxSpeed;
	public UUID fuelType;
	public double fuelMaxAmount;
	public double fuelAmount;

	public static SurfaceVehicle createFrom(edu.mit.spacenet.domain.element.SurfaceVehicle element, Context context) {
		SurfaceVehicle e = new SurfaceVehicle();
		e.id = context.getUUID(element);
		e.type = TYPE_MAP.inverse().get(ElementType.SURFACE_VEHICLE);
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
		e.maxSpeed = element.getMaxSpeed();
		e.fuelType = context.getUUID(element.getFuelTank().getResource());
		e.fuelMaxAmount = element.getFuelTank().getMaxAmount();
		e.fuelAmount = element.getFuelTank().getAmount();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.domain.element.SurfaceVehicle toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.SurfaceVehicle e = new edu.mit.spacenet.domain.element.SurfaceVehicle();
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
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		e.setMaxSpeed(maxSpeed);
		edu.mit.spacenet.domain.element.ResourceTank t = new edu.mit.spacenet.domain.element.ResourceTank();
		t.setResource((I_Resource) context.getObject(fuelType));
		t.setMaxAmount(fuelMaxAmount);
		t.setAmount(fuelAmount);
		e.setFuelTank(t);
		return e;
	}
}
