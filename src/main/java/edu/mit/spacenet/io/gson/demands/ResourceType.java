package edu.mit.spacenet.io.gson.demands;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class ResourceType {
  public static final BiMap<String, edu.mit.spacenet.domain.resource.ResourceType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, edu.mit.spacenet.domain.resource.ResourceType>()
          .put("Generic", edu.mit.spacenet.domain.resource.ResourceType.GENERIC)
          .put("Continuous", edu.mit.spacenet.domain.resource.ResourceType.RESOURCE)
          .put("Discrete", edu.mit.spacenet.domain.resource.ResourceType.ITEM).build();
  
  protected String type;
  protected String name;
  protected Integer classOfSupply;
  protected String environment;
  protected String units;
  protected Double unitMass;
  protected Double unitVolume;
  protected Double packingFactor;

  public static ResourceType createFrom(edu.mit.spacenet.domain.resource.I_Resource resource) {
    ResourceType r = new ResourceType();
    r.classOfSupply = resource.getClassOfSupply().getId();
    r.type = TYPE_MAP.inverse().get(resource.getResourceType());
    r.name = resource.getName();
    r.environment = resource.getEnvironment().getName();
    r.units = resource.getUnits();
    r.unitMass = resource.getUnitMass();
    r.unitVolume = resource.getUnitVolume();
    r.packingFactor = resource.getPackingFactor();
    return r;
  }
}
