package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

public abstract class Location {
	public UUID id;
	public String name;
	public String description;
	
	public static Location createFrom(edu.mit.spacenet.domain.network.Location location, Context context) {
		if(location instanceof edu.mit.spacenet.domain.network.node.Node) {
			return Node.createFrom((edu.mit.spacenet.domain.network.node.Node) location, context);
		} else if(location instanceof edu.mit.spacenet.domain.network.edge.Edge) {
			return Edge.createFrom((edu.mit.spacenet.domain.network.edge.Edge) location, context);
		} else {
			throw new UnsupportedOperationException("unknown location type: " + location);
		}
	}
	
	public abstract edu.mit.spacenet.domain.network.Location toSpaceNet(Context context);
}
