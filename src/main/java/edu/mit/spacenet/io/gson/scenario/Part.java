package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.Item;

public class Part implements Cloneable {
	public UUID resource;
	public Double meanTimeToFailure;
	public Double meanTimeToRepair;
	public Double massToRepair;
	public Double quantity;
	public Double dutyCycle;

	public static Part createFrom(edu.mit.spacenet.domain.element.PartApplication part, Context context) {
		Part p = new Part();
		p.resource = context.getJsonIdFromJavaObject(part.getPart());
		p.meanTimeToFailure = part.getMeanTimeToFailure();
		p.meanTimeToRepair = part.getMeanTimeToRepair();
		p.massToRepair = part.getMassToRepair();
		p.quantity = part.getQuantity();
		p.dutyCycle = part.getDutyCycle();
		return p;
	}
	
	public static List<Part> createFrom(Collection<edu.mit.spacenet.domain.element.PartApplication> parts, Context context) {
		List<Part> ps = new ArrayList<Part>();
		for(edu.mit.spacenet.domain.element.PartApplication p : parts) {
			ps.add(Part.createFrom(p, context));
		}
		return ps;
	}
	
	public edu.mit.spacenet.domain.element.PartApplication toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.PartApplication p = new edu.mit.spacenet.domain.element.PartApplication();
		p.setPart((Item) context.getJavaObjectFromJsonId(resource));
		p.setMeanTimeToFailure(meanTimeToFailure);
		p.setMeanTimeToRepair(meanTimeToRepair);
		p.setMassToRepair(massToRepair);
		p.setQuantity(quantity);
		p.setDutyCycle(dutyCycle);
		return p;
	}

	public static SortedSet<PartApplication> toSpaceNet(Collection<Part> parts, Context context) {
		SortedSet<PartApplication> ps = new TreeSet<PartApplication>();
		if(parts != null) {
			for(Part part : parts) {
				ps.add(part.toSpaceNet(context));
			}
		}
		return ps;
	}
	
	@Override
	public Part clone() {
		Part p = new Part();
		p.resource = resource;
		p.meanTimeToFailure = meanTimeToFailure;
		p.meanTimeToRepair = meanTimeToRepair;
		p.massToRepair = massToRepair;
		p.quantity = quantity;
		p.dutyCycle = dutyCycle;
		return p;
	}
	
	public static List<Part> clone(Collection<Part> parts) {
		List<Part> ps = new ArrayList<Part>();
		for(Part p : parts) {
			ps.add(p.clone());
		}
		return ps;
	}
}
