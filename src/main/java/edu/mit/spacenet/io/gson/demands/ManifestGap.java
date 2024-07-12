package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class ManifestGap {
  protected Double time;
  protected Location node;
  protected List<ManifestContainer> containers;

  public static ManifestGap createFrom(
    edu.mit.spacenet.scenario.SupplyPoint point, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    ManifestGap g = new ManifestGap();
    g.time = point.getTime();
    g.node = Location.createFrom(point.getNode());
    g.containers = new ArrayList<ManifestContainer>();
    // iterate over all resource containers
    for (edu.mit.spacenet.domain.element.I_ResourceContainer container : manifest.getPackedDemands().keySet()) {
      boolean isContainerManifested = true;
      // iterate over all packed demands
      for (edu.mit.spacenet.domain.resource.Demand demand : manifest.getPackedDemands(container)) {
        // check if the demand is required at /this/ supply point
        if (manifest.getSupplyPoint(manifest.getDemand(demand)).equals(point)) {
          boolean isDemandManifested = false;
          // iterate over all of the supply edges to /this/ supply point
          for (edu.mit.spacenet.scenario.SupplyEdge edge : manifest.getSupplyEdges(point)) {
            // iterate over all carriers on this edge
            for (edu.mit.spacenet.domain.element.I_Carrier carrier : edge.getAllCarriers()) {
              // check if the container containing this demand is manifested
              if (manifest.getManifestedContainers(edge, carrier).contains(container)) {
                isDemandManifested = true;
              }
            }
          }
          if (!isDemandManifested) {
            isContainerManifested = false;
          }
        }
      }
      if (!isContainerManifested) {
        g.containers.add(ManifestContainer.createFrom(container, manifest));
      }
    }
    
    return g;
  }

  public static List<ManifestGap> createFrom(
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    List<ManifestGap> as = new ArrayList<ManifestGap>();
    for (edu.mit.spacenet.scenario.SupplyPoint point : manifest.getSupplyPoints()) {
      as.add(ManifestGap.createFrom(point, manifest));
    }
    return as;
  }
}
