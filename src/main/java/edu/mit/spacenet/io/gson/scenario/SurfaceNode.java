package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.network.node.Body;

public class SurfaceNode extends Node {
	protected Double latitude;
	protected Double longitude;
	
	public static SurfaceNode createFrom(edu.mit.spacenet.domain.network.node.SurfaceNode node, Context context) {
		SurfaceNode n = new SurfaceNode();
		n.id = UUID.randomUUID();
		context.put(node, n.id, n);
		n.name = node.getName();
		n.description = node.getDescription();
		n.body_1 = node.getBody().getName();
		n.latitude = node.getLatitude();
		n.longitude = node.getLongitude();
		n.contents = context.getJsonIdsFromJavaObjects(node.getContents());
		return n;
	}
	
	public edu.mit.spacenet.domain.network.node.SurfaceNode toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.node.SurfaceNode n = new edu.mit.spacenet.domain.network.node.SurfaceNode();
		context.put(n, id, this);
		n.setTid(context.getJavaId(id));
		n.setName(name);
		n.setDescription(description);
		n.setBody(Body.getInstance(body_1));
		n.setLatitude(latitude);
		n.setLongitude(longitude);
		n.getContents().addAll(Element.toSpaceNetViaId(contents, context));
		return n;
	}
}
