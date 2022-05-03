package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdgeDemand {
  public double startTime;
  public double endTime;
  public Location origin;
  public Location destination;
  public Location location;
  public List<Demand> demands = new ArrayList<Demand>();
  public double totalMass;
  public double totalVolume;
  public double maxCargoMass;
  public double netCargoMass;
  public double maxCargoVolume;
  public double netCargoVolume;

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
      d.demands = Demand.createFrom(demands.get(edge));
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
