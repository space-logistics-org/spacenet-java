package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class ManifestContainer extends Element {
  protected String environment;
  protected Double mass;
  protected Double volume;
  protected String cargoEnvironment;
  protected Double maxCargoMass;
  protected Double maxCargoVolume;
  protected Double packedMass;
  protected Double packedVolume;
  protected List<ManifestDemand> packedDemands;

  public static ManifestContainer createFrom(
    edu.mit.spacenet.domain.element.I_ResourceContainer container, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    ManifestContainer c = new ManifestContainer();
    c.id = container.getUid();
    c.name = container.getName();
    c.environment = container.getEnvironment().toString();
    c.mass = container.getMass();
    c.volume = container.getVolume();
    c.cargoEnvironment = container.getCargoEnvironment().toString();
    c.maxCargoMass = container.getMaxCargoMass();
    c.maxCargoVolume = container.getMaxCargoVolume();
    c.packedMass = 0d;
    c.packedVolume = 0d;
    c.packedDemands = new ArrayList<ManifestDemand>();
    for (edu.mit.spacenet.domain.resource.Demand demand : manifest.getPackedDemands(container)) {
      c.packedMass += demand.getMass();
      c.packedVolume += demand.getVolume();
      c.packedDemands.add(ManifestDemand.createFrom(demand, manifest));
    }
    return c;
  }
 
  public static List<ManifestContainer> createFrom(
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    List<ManifestContainer> cs = new ArrayList<ManifestContainer>();
    for (edu.mit.spacenet.domain.element.I_ResourceContainer container : manifest.getPackedDemands().keySet()) {
      cs.add(ManifestContainer.createFrom(container, manifest));
    }
    return cs;
  }
}
