package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodeDemand {
  protected Double time;
  protected Location location;
  protected List<Resource> consumptionDemands = new ArrayList<Resource>();
  protected List<Resource> productionDemands = new ArrayList<Resource>();
  protected Double totalMass;
  protected Double totalVolume;

  public static List<NodeDemand> createFrom(
      Map<edu.mit.spacenet.scenario.SupplyPoint, edu.mit.spacenet.domain.resource.DemandSet> demands) {
    List<NodeDemand> ds = new ArrayList<NodeDemand>();
    for (edu.mit.spacenet.scenario.SupplyPoint point : demands.keySet()) {
      NodeDemand d = new NodeDemand();
      d.time = point.getTime();
      d.location = Location.createFrom(point.getNode());
      d.consumptionDemands = Resource.createFrom(demands.get(point), true);
      d.productionDemands = Resource.createFrom(demands.get(point), false);
      d.totalMass = demands.get(point).getTotalMass();
      d.totalVolume = demands.get(point).getTotalVolume();
      ds.add(d);
    }
    return ds;
  }
}
