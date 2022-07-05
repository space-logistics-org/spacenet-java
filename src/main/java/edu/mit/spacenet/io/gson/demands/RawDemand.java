package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class RawDemand {
  protected Double time;
  protected Location location;
  protected Element element;
  protected List<Resource> consumption = new ArrayList<Resource>();
  protected List<Resource> production = new ArrayList<Resource>();
  protected Double totalConsumptionMass = 0.0;
  protected Double totalConsumptionVolume = 0.0;
  protected Double totalProductionMass = 0.0;
  protected Double totalProductionVolume = 0.0;

  public static RawDemand createFrom(edu.mit.spacenet.simulator.SimDemand demands) {
    RawDemand d = new RawDemand();
    d.time = demands.getTime();
    d.location = Location.createFrom(demands.getLocation());
    d.element = Element.createFrom(demands.getElement());
    d.consumption = Resource.createFrom(demands.getDemands(), true);
    d.production = Resource.createFrom(demands.getDemands(), false);
    for(Resource r : d.consumption) {
      d.totalConsumptionMass += r.mass;
      d.totalConsumptionVolume += r.volume;
    }
    for(Resource r : d.production) {
      d.totalProductionMass += r.mass;
      d.totalProductionVolume += r.volume;
    }
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
