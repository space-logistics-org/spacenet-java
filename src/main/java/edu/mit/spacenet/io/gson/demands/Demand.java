package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class Demand {
  protected ResourceType resource;
  protected Double amount;
  protected Double mass;
  protected Double volume;

  public static Demand createFrom(edu.mit.spacenet.domain.resource.Demand demand) {
    Demand d = new Demand();
    d.resource = ResourceType.createFrom(demand.getResource());
    d.amount = demand.getAmount();
    d.mass = demand.getMass();
    d.volume = demand.getVolume();
    return d;
  }

  public static List<Demand> createFrom(edu.mit.spacenet.domain.resource.DemandSet demands) {
    List<Demand> ds = new ArrayList<Demand>();
    for (edu.mit.spacenet.domain.resource.Demand d : demands) {
      ds.add(Demand.createFrom(d));
    }
    return ds;
  }
}
