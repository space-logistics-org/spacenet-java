package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.Item;

public class Part {
	public UUID resource;
	private double meanTimeToFailure;
	private double meanTimeToRepair;
	private double massToRepair;
	private double quantity;
	private double dutyCycle;

	public static Part createFrom(edu.mit.spacenet.domain.element.PartApplication part, Context context) {
		Part p = new Part();
		p.resource = context.getUUID(part);
		p.meanTimeToFailure = part.getMeanTimeToFailure();
		p.meanTimeToRepair = part.getMeanTimeToRepair();
		p.massToRepair = part.getMassToRepair();
		p.quantity = part.getQuantity();
		p.dutyCycle = part.getDutyCycle();
		return p;
	}
	
	public static List<Part> createFrom(SortedSet<edu.mit.spacenet.domain.element.PartApplication> parts, Context context) {
		List<Part> ps = new ArrayList<Part>();
		for(edu.mit.spacenet.domain.element.PartApplication p : parts) {
			ps.add(Part.createFrom(p, context));
		}
		return ps;
	}
	
	public edu.mit.spacenet.domain.element.PartApplication toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.PartApplication p = new edu.mit.spacenet.domain.element.PartApplication();
		p.setPart((Item) context.getObject(resource));
		p.setMeanTimeToFailure(meanTimeToFailure);
		p.setMeanTimeToRepair(meanTimeToRepair);
		p.setMassToRepair(massToRepair);
		p.setQuantity(quantity);
		p.setDutyCycle(dutyCycle);
		return p;
	}

	public static SortedSet<PartApplication> toSpaceNet(List<Part> parts, Context context) {
		SortedSet<PartApplication> ps = new TreeSet<PartApplication>();
		if(parts != null) {
			for(Part part : parts) {
				ps.add(part.toSpaceNet(context));
			}
		}
		return ps;
	}
}
