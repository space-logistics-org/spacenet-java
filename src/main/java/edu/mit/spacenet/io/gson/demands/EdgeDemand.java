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
  protected List<Resource> consumption = new ArrayList<Resource>();
  protected List<Resource> production = new ArrayList<Resource>();
  protected Double totalConsumptionMass = 0.0;
  protected Double totalConsumptionVolume = 0.0;
  protected Double totalProductionMass = 0.0;
  protected Double totalProductionVolume = 0.0;
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
      d.consumption = Resource.createFrom(demands.get(edge), true);
      d.production = Resource.createFrom(demands.get(edge), false);
      for (Resource r : d.consumption) {
        d.totalConsumptionMass += r.mass;
        d.totalConsumptionVolume += r.volume;
      }
      for (Resource r : d.production) {
        d.totalProductionMass += r.mass;
        d.totalProductionVolume += r.volume;
      }
      d.maxCargoMass = edge.getMaxCargoMass();
      d.netCargoMass = edge.getNetCargoMass();
      d.maxCargoVolume = edge.getMaxCargoVolume();
      d.netCargoVolume = edge.getNetCargoVolume();
      ds.add(d);
    }
    return ds;
  }
}
