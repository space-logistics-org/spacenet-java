package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class ManifestAnalysis {
  protected List<ManifestContainer> containers = new ArrayList<ManifestContainer>();
  protected List<ManifestAction> actions = new ArrayList<ManifestAction>();
  protected List<ManifestGap> gaps = new ArrayList<ManifestGap>();
  
  public static ManifestAnalysis createFrom(
      edu.mit.spacenet.scenario.Manifest manifest) {
    ManifestAnalysis o = new ManifestAnalysis();
    o.containers = ManifestContainer.createFrom(manifest);
    o.actions = ManifestAction.createFrom(manifest);
    o.gaps = ManifestGap.createFrom(manifest);
    return o;
  }
}
