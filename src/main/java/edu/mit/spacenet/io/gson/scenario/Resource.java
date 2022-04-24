package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.resource.I_Resource;

public class Resource {
	public UUID type;
	public double amount;
	
	public static Resource createFrom(edu.mit.spacenet.domain.resource.Demand demand, Context context) {
		Resource d = new Resource();
		d.type = context.getUUID(demand.getResource());
		d.amount = demand.getAmount();
		return d;
	}

	public edu.mit.spacenet.domain.resource.Demand toSpaceNet(Context context) {
		edu.mit.spacenet.domain.resource.Demand d = new edu.mit.spacenet.domain.resource.Demand();
		d.setResource((I_Resource) context.getObject(type));
		d.setAmount(amount);
		return d;
	}
}