package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.NodeType;

public class SurfaceNode extends Node {
	public double latitude;
	public double longitude;
	
	public static SurfaceNode createFrom(edu.mit.spacenet.domain.network.node.SurfaceNode node, Context context) {
		SurfaceNode n = new SurfaceNode();
		n.id = context.getUUID(node);
		n.name = node.getName();
		n.description = node.getDescription();
		n.type = TYPE_MAP.inverse().get(NodeType.SURFACE);
		n.body_1 = node.getBody().getName();
		n.latitude = node.getLatitude();
		n.longitude = node.getLongitude();
		n.contents = context.getUUIDs(node.getContents());
		return n;
	}
	
	public edu.mit.spacenet.domain.network.node.SurfaceNode toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.node.SurfaceNode n = new edu.mit.spacenet.domain.network.node.SurfaceNode();
		n.setTid(context.getId(id));
		n.setName(name);
		n.setDescription(description);
		n.setBody(Body.getInstance(body_1));
		n.setLatitude(latitude);
		n.setLongitude(longitude);
		n.getContents().addAll(Element.toSpaceNet(contents, context));
		return n;
	}
}
