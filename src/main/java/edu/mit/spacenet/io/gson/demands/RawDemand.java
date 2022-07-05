package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class RawDemand {
  protected Double time;
  protected Location location;
  protected Element element;
  protected List<Demand> demands = new ArrayList<Demand>();
  protected Double totalMass;
  protected Double totalVolume;

  public static RawDemand createFrom(edu.mit.spacenet.simulator.SimDemand demands) {
    RawDemand d = new RawDemand();
    d.time = demands.getTime();
    d.location = Location.createFrom(demands.getLocation());
    d.element = Element.createFrom(demands.getElement());
    d.demands = Demand.createFrom(demands.getDemands());
    d.totalMass = demands.getDemands().getTotalMass();
    d.totalVolume = demands.getDemands().getTotalVolume();
    return d;
  }

  public static List<RawDemand> createFrom(List<edu.mit.spacenet.simulator.SimDemand> demandList) {
    List<RawDemand> ds = new ArrayList<RawDemand>();
    for (edu.mit.spacenet.simulator.SimDemand demands : demandList) {
      ds.add(RawDemand.createFrom(demands));
    }
    return ds;
  }
}
