package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class Location {
	public UUID id;
	public String name;
	public String description;
	public List<UUID> contents;
	
	public static Location createFrom(edu.mit.spacenet.domain.network.Location location, Context context) {
		if(location instanceof edu.mit.spacenet.domain.network.node.Node) {
			return Node.createFrom((edu.mit.spacenet.domain.network.node.Node) location, context);
		} else if(location instanceof edu.mit.spacenet.domain.network.edge.Edge) {
			return Edge.createFrom((edu.mit.spacenet.domain.network.edge.Edge) location, context);
		} else {
			throw new UnsupportedOperationException("unknown location type: " + location);
		}
	}
	
	/* TODO remove due to type conflict with node/edge methods
	public static List<Location> createFrom(Collection<? extends edu.mit.spacenet.domain.network.Location> locations, Context context) {
		List<Location> ls = new ArrayList<Location>();
		for(edu.mit.spacenet.domain.network.Location l : locations) {
			ls.add(Location.createFrom(l, context));
		}
		return ls;
	}
	*/
	
	public abstract edu.mit.spacenet.domain.network.Location toSpaceNet(Context context);
	
	public static List<edu.mit.spacenet.domain.network.Location> toSpaceNet(Collection<Location> locations, Context context) {
		List<edu.mit.spacenet.domain.network.Location> ls = new ArrayList<edu.mit.spacenet.domain.network.Location>();
		if(locations != null) {
			for(Location l : locations) {
				ls.add(l.toSpaceNet(context));
			}
		}
		return ls;
	}
}
