package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdgeDemand {
  protected Double startTime;
  protected Double endTime;
  protected Location origin;
  protected Location destination;
  protected Location location;
  protected List<Resource> demands = new ArrayList<Resource>();
  protected Double totalMass;
  protected Double totalVolume;
  protected Double maxCargoMass;
  protected Double netCargoMass;
  protected Double maxCargoVolume;
  protected Double netCargoVolume;

  public static List<EdgeDemand> createFrom(
      Map<edu.mit.spacenet.scenario.SupplyEdge, edu.mit.spacenet.domain.resource.DemandSet> demands) {
    List<EdgeDemand> ds = new ArrayList<EdgeDemand>();
    for (edu.mit.spacenet.scenario.SupplyEdge edge : demands.keySet()) {
      EdgeDemand d = new EdgeDemand();
      d.startTime = edge.getStartTime();
      d.endTime = edge.getEndTime();
      d.origin = Location.createFrom(edge.getEdge().getOrigin());
      d.destination = Location.createFrom(edge.getEdge().getDestination());
      d.location = Location.createFrom(edge.getEdge());
      d.demands = Resource.createFrom(demands.get(edge));
      d.totalMass = demands.get(edge).getTotalMass();
      d.totalVolume = demands.get(edge).getTotalVolume();
      d.maxCargoMass = edge.getMaxCargoMass();
      d.netCargoMass = edge.getNetCargoMass();
      d.maxCargoVolume = edge.getMaxCargoVolume();
      d.netCargoVolume = edge.getNetCargoVolume();
      ds.add(d);
    }
    return ds;
  }
}
