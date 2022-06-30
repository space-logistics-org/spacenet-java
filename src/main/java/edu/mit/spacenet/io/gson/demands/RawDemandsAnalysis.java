package edu.mit.spacenet.io.gson.demands;

import java.util.ArrayList;
import java.util.List;

public class RawDemandsAnalysis {
  protected List<RawDemand> demands = new ArrayList<RawDemand>();

  public static RawDemandsAnalysis createFrom(edu.mit.spacenet.simulator.DemandSimulator sim) {
    RawDemandsAnalysis o = new RawDemandsAnalysis();
    o.demands = RawDemand.createFrom(sim.getUnsatisfiedDemands());
    return o;
  }
}
