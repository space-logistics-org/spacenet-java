package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class ManifestCarrier extends Element {
  protected String environment;
  protected Double mass;
  protected Double volume;
  protected String cargoEnvironment;
  protected Double maxCargoMass;
  protected Double maxCargoVolume;
  protected Double manifestedMass;
  protected Double manifestedVolume;
  protected List<ManifestContainer> manifestedContainers;

  public static ManifestCarrier createFrom(
    edu.mit.spacenet.domain.element.I_Carrier carrier, 
    edu.mit.spacenet.scenario.SupplyEdge edge, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    ManifestCarrier c = new ManifestCarrier();
    c.id = carrier.getUid();
    c.name = carrier.getName();
    c.environment = carrier.getEnvironment().toString();
    c.mass = carrier.getMass();
    c.volume = carrier.getVolume();
    c.cargoEnvironment = carrier.getCargoEnvironment().toString();
    c.maxCargoMass = carrier.getMaxCargoMass();
    c.maxCargoVolume = carrier.getMaxCargoVolume();
    c.manifestedMass = 0d;
    c.manifestedVolume = 0d;
    c.manifestedContainers = new ArrayList<ManifestContainer>();
    for (edu.mit.spacenet.domain.element.I_ResourceContainer container : manifest.getManifestedContainers(edge, carrier)) {
      c.manifestedMass += container.getMass();
      c.manifestedVolume += container.getVolume();
      for (edu.mit.spacenet.domain.resource.Demand demand : manifest.getPackedDemands(container)) {
        c.manifestedMass += demand.getMass();
      }
      c.manifestedContainers.add(ManifestContainer.createFrom(container, manifest));
    }
    return c;
  }
  
  public static List<ManifestCarrier> createFrom(
    edu.mit.spacenet.scenario.SupplyEdge edge, 
    edu.mit.spacenet.scenario.Manifest manifest
  ) {
    List<ManifestCarrier> carriers = new ArrayList<ManifestCarrier>();
    for (edu.mit.spacenet.domain.element.I_Carrier carrier : edge.getCarriers()) {
      carriers.add(ManifestCarrier.createFrom(carrier, edge, manifest));
    }
    return carriers;
  }
}
