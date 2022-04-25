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
		for(edu.mit.spacenet.domain.resource.I_Resource d : resources.keySet()) {
			Resource r = new Resource();
			r.type = context.getUUID(d);
			r.amount = resources.get(d);
			rs.add(r);
		}
		return rs;
	}

	public edu.mit.spacenet.domain.resource.Demand toSpaceNet(Context context) {
		edu.mit.spacenet.domain.resource.Demand d = new edu.mit.spacenet.domain.resource.Demand();
		d.setResource((I_Resource) context.getObjectViaId(type));
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
				ds.put((I_Resource) context.getObject(r.type), r.amount);
			}
		}
		return ds;
	}
}