package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodeDemand {
  protected Double time;
  protected Location location;
  protected List<Resource> consumption = new ArrayList<Resource>();
  protected List<Resource> production = new ArrayList<Resource>();
  protected Double totalConsumptionMass = 0.0;
  protected Double totalConsumptionVolume = 0.0;
  protected Double totalProductionMass = 0.0;
  protected Double totalProductionVolume = 0.0;

  public static List<NodeDemand> createFrom(
      Map<edu.mit.spacenet.scenario.SupplyPoint, edu.mit.spacenet.domain.resource.DemandSet> demands) {
    List<NodeDemand> ds = new ArrayList<NodeDemand>();
    for (edu.mit.spacenet.scenario.SupplyPoint point : demands.keySet()) {
      NodeDemand d = new NodeDemand();
      d.time = point.getTime();
      d.location = Location.createFrom(point.getNode());
      d.consumption = Resource.createFrom(demands.get(point), true);
      d.production = Resource.createFrom(demands.get(point), false);
      for (Resource r : d.consumption) {
        d.totalConsumptionMass += r.mass;
        d.totalConsumptionVolume += r.volume;
      }
      for (Resource r : d.production) {
        d.totalProductionMass += r.mass;
        d.totalProductionVolume += r.volume;
      }
      ds.add(d);
    }
    return ds;
  }
}
