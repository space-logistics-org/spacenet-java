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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;

public class Resource implements Cloneable {
  protected UUID resource;
  protected Integer classOfSupply; // for generic only
  protected String environment; // for generic only
  protected Double amount;

  public static Resource createFrom(edu.mit.spacenet.domain.resource.Demand demand,
      Context context) {
    Resource d = new Resource();
    if (demand.getResource()
        .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
      d.classOfSupply = demand.getResource().getClassOfSupply().getId();
      d.environment = demand.getResource().getEnvironment().getName();
    } else {
      d.resource = context.getJsonIdFromJavaObject(demand.getResource());
    }
    d.amount = demand.getAmount();
    return d;
  }

  public static List<Resource> createFrom(edu.mit.spacenet.domain.resource.DemandSet demands,
      Context context) {
    List<Resource> rs = new ArrayList<Resource>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      rs.add(Resource.createFrom(d, context));
    }
    return rs;
  }

  public static List<Resource> createFrom(
      Collection<edu.mit.spacenet.domain.resource.Demand> demands, Context context) {
    List<Resource> rs = new ArrayList<Resource>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      rs.add(Resource.createFrom(d, context));
    }
    return rs;
  }

  public static List<Resource> createFrom(Map<I_Resource, Double> resources, Context context) {
    List<Resource> rs = new ArrayList<Resource>();
    for (edu.mit.spacenet.domain.resource.I_Resource r : resources.keySet()) {
      rs.add(Resource.createFrom(new edu.mit.spacenet.domain.resource.Demand(r, resources.get(r)),
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
    d.setAmount(amount);
    return d;
  }

  public static SortedSet<edu.mit.spacenet.domain.resource.Demand> toSpaceNetSet(
      Collection<Resource> resources, Context context) {
    SortedSet<edu.mit.spacenet.domain.resource.Demand> ds =
        new TreeSet<edu.mit.spacenet.domain.resource.Demand>();
    if (resources != null) {
      for (Resource r : resources) {
        ds.add(r.toSpaceNet(context));
      }
    }
    return ds;
  }

  public static edu.mit.spacenet.domain.resource.DemandSet toSpaceNet(
      Collection<Resource> resources, Context context) {
    edu.mit.spacenet.domain.resource.DemandSet ds =
        new edu.mit.spacenet.domain.resource.DemandSet();
    if (resources != null) {
      for (Resource r : resources) {
        ds.add(r.toSpaceNet(context));
      }
    }
    return ds;
  }

  public static SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> toSpaceNetMap(
      Collection<Resource> resources, Context context) {
    SortedMap<edu.mit.spacenet.domain.resource.I_Resource, Double> rs =
        new TreeMap<edu.mit.spacenet.domain.resource.I_Resource, Double>();
    if (resources != null) {
      for (Resource r : resources) {
        edu.mit.spacenet.domain.resource.Demand d = r.toSpaceNet(context);
        rs.put(d.getResource(), d.getAmount());
      }
    }
    return rs;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Resource)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Resource other = (Resource) obj;
    return new EqualsBuilder().append(resource, other.resource)
        .append(classOfSupply, other.classOfSupply).append(environment, other.environment)
        .append(amount, other.amount).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(resource).append(classOfSupply).append(environment)
        .append(amount).toHashCode();
  }

  @Override
  public Resource clone() {
    Resource r = new Resource();
    r.resource = resource;
    r.classOfSupply = classOfSupply;
    r.environment = environment;
    r.amount = amount;
    return r;
  }

  public static List<Resource> clone(Collection<Resource> resources) {
    if (resources == null) {
      return null;
    }
    List<Resource> rs = new ArrayList<Resource>();
    for (Resource r : resources) {
      rs.add(r.clone());
    }
    return rs;
  }
}
