package edu.mit.spacenet.io.gson.scenario;

public class Configuration {
  protected Double timePrecision;
  protected Double demandPrecision;
  protected Double massPrecision;
  protected Double volumePrecision;
  protected Boolean volumeConstrained;
  protected String itemDiscretization;
  protected Double itemAggregation;
  protected Boolean scavangeSpares;
  // TODO add repaired items
  protected Boolean detailedEva;
  protected Boolean detailedExploration;

  public static Configuration createFrom(edu.mit.spacenet.scenario.Scenario scenario,
      Context context) {
    Configuration c = new Configuration();
    c.timePrecision = scenario.getTimePrecision();
    c.demandPrecision = scenario.getDemandPrecision();
    c.massPrecision = scenario.getMassPrecision();
    c.volumePrecision = scenario.getVolumePrecision();
    c.volumeConstrained = scenario.isVolumeConstrained();
    c.itemDiscretization = scenario.getItemDiscretization().getName();
    c.itemAggregation = scenario.getItemAggregation();
    c.scavangeSpares = scenario.isScavengeSpares();
    // TODO add repaired items
    c.detailedEva = scenario.isDetailedEva();
    c.detailedExploration = scenario.isDetailedExploration();
    return c;
  }
}
