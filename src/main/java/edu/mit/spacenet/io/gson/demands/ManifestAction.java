package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class ManifestAction {
  protected Double time;
  protected Location node;
  protected List<ManifestCarrier> carriers;

  public static ManifestAction createFrom(
    edu.mit.spacenet.scenario.SupplyEdge edge, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    ManifestAction a = new ManifestAction();
    a.time = edge.getStartTime();
    a.node = Location.createFrom(edge.getOrigin());
    a.carriers = ManifestCarrier.createFrom(edge, manifest);
    return a;
  }

  public static List<ManifestAction> createFrom(
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    List<ManifestAction> as = new ArrayList<ManifestAction>();
    for (edu.mit.spacenet.scenario.SupplyEdge edge : manifest.getSupplyEdges()) {
      as.add(ManifestAction.createFrom(edge, manifest));
    }
    return as;
  }
}
