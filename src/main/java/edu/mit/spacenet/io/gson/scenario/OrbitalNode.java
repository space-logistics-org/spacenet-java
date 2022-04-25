package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.NodeType;

public class OrbitalNode extends Node {
	public double inclination;
	public double periapsis;
	public double apoapsis;
	
	public static OrbitalNode createFrom(edu.mit.spacenet.domain.network.node.OrbitalNode node, Context context) {
		OrbitalNode n = new OrbitalNode();
		n.id = context.getUUID(node);
		n.name = node.getName();
		n.description = node.getDescription();
		n.type = TYPE_MAP.inverse().get(NodeType.ORBITAL);
		n.body_1 = node.getBody().getName();
		n.inclination = node.getInclination();
		n.periapsis = node.getPeriapsis();
		n.apoapsis = node.getApoapsis();
		n.contents = context.getUUIDs(node.getContents());
		return n;
	}
	
	public edu.mit.spacenet.domain.network.node.OrbitalNode toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.node.OrbitalNode n = new edu.mit.spacenet.domain.network.node.OrbitalNode();
		n.setTid(context.getId(id));
		n.setName(name);
		n.setDescription(description);
		n.setBody(Body.getInstance(body_1));
		n.setInclination(inclination);
		n.setPeriapsis(periapsis);
		n.setApoapsis(apoapsis);
		n.getContents().addAll(Element.toSpaceNet(contents, context));
		return n;
	}
}
