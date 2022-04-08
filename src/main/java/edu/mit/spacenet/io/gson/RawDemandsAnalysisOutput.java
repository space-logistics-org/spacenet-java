package edu.mit.spacenet.io.gson;

import java.util.ArrayList;
import java.util.List;

public class RawDemandsAnalysisOutput {
	public List<RawDemand> demands = new ArrayList<RawDemand>();
	
	public static RawDemandsAnalysisOutput createFrom(edu.mit.spacenet.simulator.DemandSimulator sim) {
		RawDemandsAnalysisOutput o = new RawDemandsAnalysisOutput();
		o.demands = RawDemand.createFrom(sim.getUnsatisfiedDemands());
		return o;
	}
}
