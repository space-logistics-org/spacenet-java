package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.network.edge.EdgeType;

public class SurfaceEdge extends Edge {
	public double distance;

	public static SurfaceEdge createFrom(edu.mit.spacenet.domain.network.edge.SurfaceEdge edge, Context context) {
		SurfaceEdge e = new SurfaceEdge();
		e.id = context.getUUID(edge);
		e.name = edge.getName();
		e.description = edge.getDescription();
		e.type = TYPE_MAP.inverse().get(EdgeType.SURFACE);
		e.origin_id = context.getUUID(edge.getOrigin());
		e.destination_id = context.getUUID(edge.getDestination());
		e.distance = edge.getDistance();
		e.contents = context.getUUIDs(edge.getContents());
		return e;
	}
	
	public edu.mit.spacenet.domain.network.edge.SurfaceEdge toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.SurfaceEdge e = new edu.mit.spacenet.domain.network.edge.SurfaceEdge();
		e.setTid(context.getId(id));
		e.setName(name);
		e.setDescription(description);
		e.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObject(origin_id));
		e.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getObject(destination_id));
		e.setDistance(distance);
		e.getContents().addAll(Element.toSpaceNet(contents, context));
		return e;
	}
}
