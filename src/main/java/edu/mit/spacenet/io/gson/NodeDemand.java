package edu.mit.spacenet.io.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.mit.spacenet.scenario.SupplyPoint;

public class NodeDemand {
	public double time;
	public Location location;
	public List<Demand> demands = new ArrayList<Demand>();
	public double totalMass;
	public double totalVolume;
	
	public static List<NodeDemand> createFrom(Map<edu.mit.spacenet.scenario.SupplyPoint, edu.mit.spacenet.domain.resource.DemandSet> demands) {
		List<NodeDemand> ds = new ArrayList<NodeDemand>();
		for(edu.mit.spacenet.scenario.SupplyPoint point : demands.keySet()) {
			if(demands.get(point).getTotalMass() > 0) { // TODO does not consider zero mass resources
				NodeDemand d = new NodeDemand();
				d.time = point.getTime();
				d.location = Location.createFrom(point.getNode());
				d.demands = Demand.createFrom(demands.get(point));
				d.totalMass = demands.get(point).getTotalMass();
				d.totalVolume = demands.get(point).getTotalVolume();
				ds.add(d);
			}
		}
		return ds;
	}
}