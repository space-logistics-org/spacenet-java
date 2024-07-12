package edu.mit.spacenet.io.gson.demands;

public class ManifestDemand {
  protected ResourceType resource;
  protected Double amount;
  protected Double mass;
  protected Double volume;
  protected Location node;
  protected Double time;

  public static ManifestDemand createFrom(
    edu.mit.spacenet.domain.resource.Demand demand, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    ManifestDemand d = new ManifestDemand();
    d.resource = ResourceType.createFrom(demand.getResource());
    d.amount = Math.abs(demand.getAmount());
    d.mass = demand.getMass();
    d.volume = demand.getVolume();
    edu.mit.spacenet.scenario.SupplyPoint point = manifest.getSupplyPoint(manifest.getDemand(demand));
    d.node = Location.createFrom(point.getNode());
    d.time = point.getTime();
    return d;
  }
}
