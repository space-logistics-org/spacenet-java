package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.network.edge.EdgeType;

public class SurfaceEdge extends Edge {
	public String type = TYPE_MAP.inverse().get(EdgeType.SURFACE);
	public double distance;

	public static SurfaceEdge createFrom(edu.mit.spacenet.domain.network.edge.SurfaceEdge edge, Context context) {
		SurfaceEdge e = new SurfaceEdge();
		e.id = context.getUUID(edge);
		e.name = edge.getName();
		e.description = edge.getDescription();
		e.origin = context.getUUID(edge.getOrigin());
		e.destination = context.getUUID(edge.getDestination());
		e.distance = edge.getDistance();
		return e;
	}
	
	public edu.mit.spacenet.domain.network.edge.SurfaceEdge toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.SurfaceEdge e = new edu.mit.spacenet.domain.network.edge.SurfaceEdge();
		e.setTid(context.getId(id));
		e.setName(name);
		e.setDescription(description);
		e.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObject(origin));
		e.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getObject(destination));
		e.setDistance(distance);
		return e;
	}
}
