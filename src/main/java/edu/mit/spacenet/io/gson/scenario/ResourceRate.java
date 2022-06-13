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

public class ResourceRate implements Cloneable {
  protected UUID resource;
  protected Integer classOfSupply; // for generic only
  protected String environment; // for generic only
  protected Double rate;

  public static ResourceRate createFrom(edu.mit.spacenet.domain.resource.Demand demand,
      Context context) {
    ResourceRate d = new ResourceRate();
    if (demand.getResource()
        .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
      d.classOfSupply = demand.getResource().getClassOfSupply().getId();
      d.environment = demand.getResource().getEnvironment().getName();
    } else {
      d.resource = context.getJsonIdFromJavaObject(demand.getResource());
    }
    d.rate = demand.getAmount();
    return d;
  }

  public static List<ResourceRate> createFrom(edu.mit.spacenet.domain.resource.DemandSet demands,
      Context context) {
    List<ResourceRate> rs = new ArrayList<ResourceRate>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      rs.add(ResourceRate.createFrom(d, context));
    }
    return rs;
  }

  public static List<ResourceRate> createFrom(
      Collection<edu.mit.spacenet.domain.resource.Demand> demands, Context context) {
    List<ResourceRate> rs = new ArrayList<ResourceRate>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      rs.add(ResourceRate.createFrom(d, context));
    }
    return rs;
  }

  public static List<ResourceRate> createFrom(Map<I_Resource, Double> resources, Context context) {
    List<ResourceRate> rs = new ArrayList<ResourceRate>();
    for (edu.mit.spacenet.domain.resource.I_Resource r : resources.keySet()) {
      rs.add(ResourceRate.createFrom(new edu.mit.spacenet.domain.resource.Demand(r, resources.get(r)),
          context));
    }
    return rs;
  }

  public edu.mit.spacenet.domain.resource.Demand toSpaceNet(Context context) {
    edu.mit.spacenet.domain.resource.Demand d = new edu.mit.spacenet.domain.resource.Demand();
    if (resource == null) {
      d.setResource(new edu.mit.spacenet.domain.resource.GenericResource(
          ClassOfSupply.getInstance(classOfSupply), Environment.getInstance(environment)));
    } else {
      d.setResource((I_Resource) context.getJavaObjectFromJsonId(resource));
    }
    d.setAmount(rate);
    return d;
  }

  public static SortedSet<edu.mit.spacenet.domain.resource.Demand> toSpaceNetSet(
      Collection<ResourceRate> resources, Context context) {
    SortedSet<edu.mit.spacenet.domain.resource.Demand> ds =
        new TreeSet<edu.mit.spacenet.domain.resource.Demand>();
    if (resources != null) {
      for (ResourceRate r : resources) {
        ds.add(r.toSpaceNet(context));
      }
    }
    return ds;
  }

  public static edu.mit.spacenet.domain.resource.DemandSet toSpaceNet(
      Collection<ResourceRate> resources, Context context) {
    edu.mit.spacenet.domain.resource.DemandSet ds =
        new edu.mit.spacenet.domain.resource.DemandSet();
    if (resources != null) {
      for (ResourceRate r : resources) {
        ds.add(r.toSpaceNet(context));
      }
    }
    return ds;
  }

  public static SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> toSpaceNetMap(
      Collection<ResourceRate> resources, Context context) {
    SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> rs =
        new TreeMap<edu.mit.spacenet.domain.resource.I_Resource, Double>();
    if (resources != null) {
      for (ResourceRate r : resources) {
        edu.mit.spacenet.domain.resource.Demand d = r.toSpaceNet(context);
        rs.put(d.getResource(), d.getAmount());
      }
    }
    return rs;
  }

  @Override
  public ResourceRate clone() {
    ResourceRate r = new ResourceRate();
    r.resource = resource;
    r.classOfSupply = classOfSupply;
    r.environment = environment;
    r.rate = rate;
    return r;
  }

  public static List<ResourceRate> clone(Collection<ResourceRate> resources) {
    List<ResourceRate> rs = new ArrayList<ResourceRate>();
    for (ResourceRate r : resources) {
      rs.add(r.clone());
    }
    return rs;
  }
}
