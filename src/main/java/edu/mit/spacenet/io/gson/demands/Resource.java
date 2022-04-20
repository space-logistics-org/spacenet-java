package edu.mit.spacenet.io.gson.demands;

public class Resource {
	public int classOfSupply;
	public String name;
	public double unitMass;
	public double unitVolume;
	
	public static Resource createFrom(edu.mit.spacenet.domain.resource.I_Resource resource) {
		Resource r = new Resource();
		r.classOfSupply = resource.getClassOfSupply().getId();
		r.name = resource.getName();
		r.unitMass = resource.getUnitMass();
		r.unitVolume = resource.getUnitVolume();
		return r;
	}
}