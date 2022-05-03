package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;

public class ResourceType {
	public static final BiMap<String, edu.mit.spacenet.domain.resource.ResourceType> TYPE_MAP = new ImmutableBiMap.Builder<String, edu.mit.spacenet.domain.resource.ResourceType>()
			.put("Generic", edu.mit.spacenet.domain.resource.ResourceType.GENERIC)
			.put("Resource", edu.mit.spacenet.domain.resource.ResourceType.RESOURCE)
			.put("Item", edu.mit.spacenet.domain.resource.ResourceType.ITEM)
			.build();
	
	protected UUID id;
	protected String type;
	protected String name;
	protected String description;
	protected Integer classOfSupply;
	protected String environment;
	protected String units;
	protected Double unitMass;
	protected Double unitVolume;
	protected Double packingFactor;

	public static ResourceType createFrom(I_Resource resource, Context context) {
		ResourceType r = new ResourceType();
		r.id = UUID.randomUUID();
		context.put(resource, r.id, r);
		r.type = TYPE_MAP.inverse().get(resource.getResourceType());
		r.name = resource.getName();
		r.description = resource.getDescription();
		r.classOfSupply = resource.getClassOfSupply().getId();
		r.environment = resource.getEnvironment().getName();
		r.units = resource.getUnits();
		r.unitMass = resource.getUnitMass();
		r.unitVolume = resource.getUnitVolume();
		r.packingFactor = resource.getPackingFactor();
		return r;
	}
	
	public static List<ResourceType> createFrom(Collection<? extends I_Resource> resources, Context context) {
		List<ResourceType> rs = new ArrayList<ResourceType>();
		for(I_Resource r : resources) {
			rs.add(ResourceType.createFrom(r, context));
		}
		return rs;
	}
	
	public I_Resource toSpaceNet(Context context) {
		edu.mit.spacenet.domain.resource.I_Resource r;
		if(TYPE_MAP.get(type) == edu.mit.spacenet.domain.resource.ResourceType.ITEM) {
			r = new edu.mit.spacenet.domain.resource.Item();
		} else {
			r = new edu.mit.spacenet.domain.resource.Resource();
		}
		context.put(r, id, this);
		r.setTid(context.getJavaId(id));
		r.setName(name);
		r.setDescription(description);
		r.setClassOfSupply(ClassOfSupply.getInstance(classOfSupply));
		r.setEnvironment(Environment.getInstance(environment));
		r.setUnits(units);
		r.setUnitMass(unitMass);
		r.setUnitVolume(unitVolume);
		r.setPackingFactor(packingFactor);
		return r;
	}
	
	public static List<I_Resource> toSpaceNet(Collection<ResourceType> resources, Context context) {
		List<I_Resource> rs = new ArrayList<I_Resource>();
		for(ResourceType r : resources) {
			rs.add(r.toSpaceNet(context));
		}
		return rs;
	}
}
