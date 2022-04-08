package edu.mit.spacenet.io.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdgeDemand {
	public double startTime;
	public double endTime;
	public Location location;
	public List<Demand> demands = new ArrayList<Demand>();

	public static List<EdgeDemand> createFrom(Map<edu.mit.spacenet.scenario.SupplyEdge, edu.mit.spacenet.domain.resource.DemandSet> demands) {
		List<EdgeDemand> ds = new ArrayList<EdgeDemand>();
		for(edu.mit.spacenet.scenario.SupplyEdge edge : demands.keySet()) {
			if(demands.get(edge).getTotalMass() > 0) { // TODO does not consider zero mass resources
				EdgeDemand d = new EdgeDemand();
				d.startTime = edge.getStartTime();
				d.endTime = edge.getEndTime();
				d.location = Location.createFrom(edge.getEdge());
				d.demands = Demand.createFrom(demands.get(edge));
				ds.add(d);
			}
		}
		return ds;
	}
}