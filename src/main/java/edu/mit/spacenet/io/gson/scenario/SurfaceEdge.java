package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

public class SurfaceEdge extends Edge {
	public double distance;

	public static SurfaceEdge createFrom(edu.mit.spacenet.domain.network.edge.SurfaceEdge edge, Context context) {
		SurfaceEdge e = new SurfaceEdge();
		e.id = UUID.randomUUID();
		context.put(edge, e.id, e);
		e.name = edge.getName();
		e.description = edge.getDescription();
		e.origin_id = context.getJsonIdFromJavaObject(edge.getOrigin());
		e.destination_id = context.getJsonIdFromJavaObject(edge.getDestination());
		e.distance = edge.getDistance();
		e.contents = context.getJsonIdsFromJavaObjects(edge.getContents());
		return e;
	}
	
	public edu.mit.spacenet.domain.network.edge.SurfaceEdge toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.SurfaceEdge e = new edu.mit.spacenet.domain.network.edge.SurfaceEdge();
		context.put(e, id, this);
		e.setTid(context.getJavaId(id));
		e.setName(name);
		e.setDescription(description);
		e.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(origin_id));
		e.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(destination_id));
		e.setDistance(distance);
		e.getContents().addAll(Element.toSpaceNetViaId(contents, context));
		return e;
	}
}
