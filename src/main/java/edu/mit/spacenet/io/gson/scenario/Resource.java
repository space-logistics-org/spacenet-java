package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;

public class Resource implements Cloneable {
	public UUID type;
	public Integer classOfSupply; // for generic only
	public String environment; // for generic only
	public double amount;
	
	public static Resource createFrom(edu.mit.spacenet.domain.resource.Demand demand, Context context) {
		Resource d = new Resource();
		if(demand.getResource().getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
			d.classOfSupply = demand.getResource().getClassOfSupply().getId();
			d.environment = demand.getResource().getEnvironment().getName();
		} else {
			d.type = context.getJsonIdFromJavaObject(demand.getResource());
		}
		d.amount = demand.getAmount();
		return d;
	}
	
	public static List<Resource> createFrom(edu.mit.spacenet.domain.resource.DemandSet demands, Context context) {
		List<Resource> rs = new ArrayList<Resource>();
		for(edu.mit.spacenet.domain.resource.Demand d : demands) {
			rs.add(Resource.createFrom(d, context));
		}
		return rs;
	}
	
	public static List<Resource> createFrom(Collection<edu.mit.spacenet.domain.resource.Demand> demands, Context context) {
		List<Resource> rs = new ArrayList<Resource>();
		for(edu.mit.spacenet.domain.resource.Demand d : demands) {
			rs.add(Resource.createFrom(d, context));
		}
		return rs;
	}
	
	public static List<Resource> createFrom(Map<I_Resource, Double> resources, Context context) {
		List<Resource> rs = new ArrayList<Resource>();
		for(edu.mit.spacenet.domain.resource.I_Resource r : resources.keySet()) {
			rs.add(Resource.createFrom(new edu.mit.spacenet.domain.resource.Demand(r, resources.get(r)), context));
		}
		return rs;
	}

	public edu.mit.spacenet.domain.resource.Demand toSpaceNet(Context context) {
		edu.mit.spacenet.domain.resource.Demand d = new edu.mit.spacenet.domain.resource.Demand();
		if(type == null) {
			d.setResource(
					new edu.mit.spacenet.domain.resource.GenericResource(
						ClassOfSupply.getInstance(classOfSupply), 
						Environment.getInstance(environment)
					)
				);
		} else {
			d.setResource((I_Resource) context.getJavaObjectFromJsonId(type));
		}
		d.setAmount(amount);
		return d;
	}
	
	public static SortedSet<edu.mit.spacenet.domain.resource.Demand> toSpaceNetSet(Collection<Resource> resources, Context context) {
		SortedSet<edu.mit.spacenet.domain.resource.Demand> ds = new TreeSet<edu.mit.spacenet.domain.resource.Demand>();
		if(resources != null) {
			for(Resource r : resources) {
				ds.add(r.toSpaceNet(context));
			}
		}
		return ds;
	}
	
	public static edu.mit.spacenet.domain.resource.DemandSet toSpaceNet(Collection<Resource> resources, Context context) {
		edu.mit.spacenet.domain.resource.DemandSet ds = new edu.mit.spacenet.domain.resource.DemandSet();
		if(resources != null) {
			for(Resource r : resources) {
				ds.add(r.toSpaceNet(context));
			}
		}
		return ds;
	}
	
	public static SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> toSpaceNetMap(Collection<Resource> resources, Context context) {
		SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> ds = new TreeMap<edu.mit.spacenet.domain.resource.I_Resource, Double>();
		if(resources != null) {
			for(Resource r : resources) {
				ds.put((I_Resource) context.getJavaObjectFromJsonId(r.type), r.amount);
			}
		}
		return ds;
	}
	
	@Override
	public Resource clone() {
		Resource r = new Resource();
		r.type = type;
		r.classOfSupply = classOfSupply;
		r.environment = environment;
		r.amount = amount;
		return r;
	}
	
	public static List<Resource> clone(Collection<Resource> resources) {
		List<Resource> rs = new ArrayList<Resource>();
		for(Resource r : resources) {
			rs.add(r.clone());
		}
		return rs;
	}
}