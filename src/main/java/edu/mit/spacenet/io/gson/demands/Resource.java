package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class Resource {
  protected ResourceType resource;
  protected Double amount;
  protected Double mass;
  protected Double volume;

  public static Resource createFrom(edu.mit.spacenet.domain.resource.Demand demand) {
    Resource d = new Resource();
    d.resource = ResourceType.createFrom(demand.getResource());
    d.amount = demand.getAmount();
    d.mass = demand.getMass();
    d.volume = demand.getVolume();
    return d;
  }

  public static List<Resource> createFrom(edu.mit.spacenet.domain.resource.DemandSet demands,
      boolean isConsumption) {
    List<Resource> ds = new ArrayList<Resource>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      if ((isConsumption && d.getAmount() > 0) || (!isConsumption && d.getAmount() < 0)) {
        ds.add(Resource.createFrom(d));
      }
    }
    return ds;
  }
}
