package edu.mit.spacenet.io.gson;

import java.util.ArrayList;
import java.util.List;

public class AggregatedDemandsAnalysisOutput {
	public List<NodeDemand> nodeDemands = new ArrayList<NodeDemand>();
	public List<EdgeDemand> edgeDemands = new ArrayList<EdgeDemand>();
	
	public static AggregatedDemandsAnalysisOutput createFrom(edu.mit.spacenet.simulator.DemandSimulator sim) {
		AggregatedDemandsAnalysisOutput o = new AggregatedDemandsAnalysisOutput();
		o.nodeDemands = NodeDemand.createFrom(sim.getAggregatedNodeDemands());
		o.edgeDemands = EdgeDemand.createFrom(sim.getAggregatedEdgeDemands());
		return o;
	}
}
